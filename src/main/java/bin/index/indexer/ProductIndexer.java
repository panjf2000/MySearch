package bin.index.indexer;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.NoLockFactory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;
import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.chenlb.mmseg4j.analysis.MaxWordAnalyzer;

import bin.index.db.Product;
import bin.search.util.PropertyConfiguration;

public class ProductIndexer {
	
	Logger logger = LoggerFactory.getLogger(ProductIndexer.class);

	private String indexPath = "";
	
	private IndexWriter writer = null;
	
	private Analyzer analyzer = null;
	
	private Directory returnIndexDir = null;
	
//	private String dictionary_file = PropertyConfiguration.getWordDictionary();

	public ProductIndexer(String indexPath) throws Exception {
		this.indexPath = indexPath;
		initialize();
	}
	
	/**
	 * 使用mmseg4j分词器
	 * @throws Exception
	 */
	private void initialize() throws Exception {
//		returnIndexDir = FSDirectory.open(new File(indexPath),NoLockFactory.getNoLockFactory()); 
		returnIndexDir = MMapDirectory.open(new File(indexPath),NoLockFactory.getNoLockFactory());
//		returnIndexDir = FSDirectory.open(new File(indexPath));
//		unlock();
		//mmseg4j
		analyzer = new MaxWordAnalyzer();
//		analyzer = new ArabicAnalyzer(Version.LUCENE_48);
//		analyzer = new CJKAnalyzer(Version.LUCENE_48);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_48
				,analyzer);  
		iwc.setSimilarity(new BM25Similarity());
//		iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		writer = new IndexWriter(returnIndexDir, iwc);
	}
	
	public void unlock() {
		try {
			if(IndexWriter.isLocked(returnIndexDir)){
				IndexWriter.unlock(returnIndexDir);
				System.out.println("unlock");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try{
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
			writer = null;
		}
	}
	
	public void addProduct(Product product, int id) throws Exception {
//		Thread.sleep(10);
		Document doc = ProductDocument.buildProductDocument(product, id);
		writer.addDocument(doc);
		
//		Thread.sleep(10);
	}
	
	/**
	 * 优化索引文件，使用forceMerge(1)
	 * **/
	public void optimizeIndex() throws Exception {
		writer.forceMerge(1);
	}
	
}
