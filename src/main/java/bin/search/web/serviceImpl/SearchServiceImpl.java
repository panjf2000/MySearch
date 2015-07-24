package bin.search.web.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.ReuseStrategy;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.NoLockFactory;
import org.apache.lucene.util.Version;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;
import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.chenlb.mmseg4j.analysis.MaxWordAnalyzer;

import bin.search.util.PropertyConfiguration;
import bin.search.web.bo.SearchRequest;
import bin.search.web.bo.SearchResult;
import bin.search.web.bo.SearchResults;
import bin.search.web.dao.SearchResultDao;
import bin.search.web.dao.db.DbutilsTemplate;
import bin.search.web.daoImpl.SearchResultDaoImpl;
import bin.search.web.service.SearchService;

@Service("searchService")
public class SearchServiceImpl implements SearchService {

	private static final String PRODUCT_ID = "productid";

	private static final String INDEX_TIME = "indextime";

	private static final String PRODUCT_URL = "producturl";

//	private static final String CATEGORY = "category";

	private static final String PRODUCT_NAME = "name";

	private static final String PRODUCT_TYPE = "type";

	private static final String SUMMARY = "summary";

	private static final String INDEX_STORE_PATH = PropertyConfiguration.getIndexStorePath();
	
	private IndexSearcher searcher = null;
	// 在这四个域中检索  
	private static final String[] fields = {"name","type","content"}; 
	//声明BooleanClause.Occur[]数组,它表示多个条件之间的关系
//	static BooleanClause.Occur[] flags = new BooleanClause.Occur[]
//			{BooleanClause.Occur.SHOULD,BooleanClause.Occur.SHOULD};
	
	private static final Analyzer ANALYZER = new MaxWordAnalyzer();
	
	private static final QueryParser parser = new MultiFieldQueryParser
			(Version.LUCENE_48, fields,ANALYZER);

	@Resource(name="searchResultDao")
	private SearchResultDao searchResultDao;
	
	public IndexSearcher getSearcher(){
		try{
			if (null==searcher) {
				//（1）创建IndexReader 
				Directory indexDir2 = MMapDirectory.open(new File(INDEX_STORE_PATH),NoLockFactory.getNoLockFactory());
//				Directory indexDir2 = FSDirectory.open(new File(INDEX_STORE_PATH), NoLockFactory.getNoLockFactory());
				//（2）使用IndexReader创建IndexSearcher  
				IndexReader ir = DirectoryReader.open(indexDir2);
				searcher = new IndexSearcher(ir);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return searcher;
	}
	
	public IndexSearcher getBM25Searcher() {
		try{
			if (null==searcher) {
				//（1）创建IndexReader 
				Directory indexDir2 = MMapDirectory.open(new File(INDEX_STORE_PATH),NoLockFactory.getNoLockFactory());
//				Directory indexDir2 = FSDirectory.open(new File(INDEX_STORE_PATH), NoLockFactory.getNoLockFactory());
				//（2）使用IndexReader创建IndexSearcher  
				IndexReader ir = DirectoryReader.open(indexDir2);
				searcher = new IndexSearcher(ir);
				searcher.setSimilarity(new BM25Similarity());
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		searcher.setSimilarity(new BM25Similarity());
		return searcher;
	}
	
	/**
     * 根据要获得的索引位置，获取上一次的最后一个ScoreDoc
     */
    private ScoreDoc getLastScoreDoc(int startindex,Query query,IndexSearcher searcher) throws IOException {
        if(startindex==1)return null;//如果是第一页就返回空
        int num = startindex-1;//获取上一页的数量
        TopDocs tds = searcher.search(query, num);
        return tds.scoreDocs[num-1];
    }
	
//	 /**
//     * 根据页码和分页大小获取上一次的最后一个ScoreDoc
//     */
//    private ScoreDoc getLastScoreDoc(int pageIndex,int pageSize,Query query,IndexSearcher searcher) throws IOException {
//        if(pageIndex==1)return null;//如果是第一页就返回空
//        int num = pageSize*(pageIndex-1);//获取上一页的数量
//        TopDocs tds = searcher.search(query, num);
//        return tds.scoreDocs[num-1];
//    }
    
    /***
     * 在使用时，searchAfter查询的是指定页数后面的数据，效率更高，推荐使用
     * @param query
     * @param pageIndex
     * @param pageSize
     */
    public SearchResults searchPageByAfter(SearchRequest request,int startindex,int pageSize) {
    	ArrayList<String> list = new ArrayList<String>();
    	SearchResults results = new SearchResults();
    	String query = request.getQuery();
    	int length;
        try {
//            IndexSearcher searcher = getSearcher();
        	IndexSearcher searcher = getBM25Searcher();
            Query q = null;
            try {
                q = parser.parse(query);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (startindex==1) {//初始search
            	request.setNumofpage(1);//当前为第一页
            	TopDocs tds = searcher.search(q,pageSize);
            	ScoreDoc[] scoreDoc = tds.scoreDocs;

            	System.out.println(tds.totalHits);
            	length = tds.totalHits;
//            	request.setPage(getPages(tds.totalHits, pageSize));
            	if (length==0)//返回一个空的results,不能返回null
        			return results;
            	for(ScoreDoc sd:scoreDoc) {
 	                Document doc = searcher.doc(sd.doc);
 	                String id = doc.get("productid");
 	                System.out.println(id);
 	                list.add(id);
            	}
            	results.setResults(list);
			}else{
				request.setNumofpage(request.getNumofpage()+1);//当前为第N页
	            //先获取上一页的最后一个元素
	            ScoreDoc lastSd = getLastScoreDoc(startindex, q, searcher);
	//            ScoreDoc lastSd = getLastScoreDoc(pageIndex, pageSize, q, searcher);
	            //通过最后一个元素搜索下页的pageSize个元素
	            TopDocs tds = searcher.searchAfter(lastSd,q, pageSize);
	            length = tds.totalHits;
	            for(ScoreDoc sd:tds.scoreDocs) {
	                Document doc = searcher.doc(sd.doc);
	                String id = doc.get("productid");
	                list.add(id);
	            }
	            results.setResults(list);
			}
            int startpage;
    		int endpage;
    		
    		if (startindex % 100 == 0) {
    			startpage = (startindex / 100 - 1) * 10 + 1;
    		}
    		else {
    			startpage = (startindex/100) * 10 + 1;
    		}
    		
    		int span;
    		int hasnext;
    		
    		float temp = ((float)(length - (startpage-1) * 10 ))/10;
    		if (temp > 10 )
    		{
    			span = 9;
    			hasnext = 1;
    		}
    		else if (temp == 10) {
    			span = 9;
    			hasnext = 0;
    		}
    		else {
    			hasnext = 0;
    			if ((int)temp < temp) {
    				span = (int)temp;
    			}
    			else {
    				span = (int)temp -1;
    			}
    			
    		}
    		endpage = startpage + span;
//    		System.out.println("startindex:"+startindex
//    				+" startpage:"+startpage
//    				+" endpage:"+endpage
//    				+" length:"+length
//    				+" temp:"+temp
//    				+" hasnext:"+hasnext
//    				+" span:"+span);
    		results.setMinpage(startpage);
    		results.setMaxpage(endpage);
    		results.setHasnext(hasnext);
    		results.setStartindex(startindex);
        } catch (IOException e) {
            e.printStackTrace();
        }
		return results;
    }
    
    private int getPages(int totalHits, int pageSize) {
        if (totalHits<=0)//找不到记录
			return 0;
        if(totalHits<=pageSize){//默认size为10
        	return 1;
        }else{
			float page = totalHits/(float)pageSize;
			int minpage = (int)page;
			System.out.println("page:"+page+" minpage:"+minpage);
			return  page>minpage?minpage+1:minpage;
        }
    }


    
	/**
	 * 通过request得到SearchResults
	 */
	public SearchResults getSearchResults(SearchRequest request) {
		int pagesize = 10;//默认一页显示10条数据，0--9
		return searchPageByAfter(request, request.getStartindex(), pagesize);
	}

	/**
	 * 
	 */
	public SearchResult getSearchResultById(int id) {
		return searchResultDao.getSearchResultById(id);
	}

	/**
	 * 
	 * @return SearchResultDao
	 */
	public SearchResultDao getSearchResultDao() {
		return searchResultDao;
	}

}
