package bin.index.dic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import bin.index.db.JDBCHelper;
import bin.index.db.Product;
import bin.index.db.ProductDao;
import bin.index.extractor.JsoupExtractor;
import bin.index.indexer.ProductDocument;
import bin.index.indexer.ProductIndexer;
import bin.search.util.PropertyConfiguration;

public class ProductTextFileProcessor {

	private String[] directories;
	
	private ProductIndexer indexer = null;
	
//	private IndexWriter writer = null;
	
	public final static int SUMMARY_LENGTH = 80;
	//设置日期格式
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String indexPath = PropertyConfiguration.getIndexStorePath();
	
	private ProductDao productDao = new ProductDao();
	
	/**
	 * Default constructor
	 * 
	 */
	public ProductTextFileProcessor() {
		initialize();
	}
	
	public void initialize() {
		try {
			indexer = new ProductIndexer(indexPath);
		}catch(Exception e){
			indexer.close();
			e.printStackTrace();
		}
	}
	
	public void setDirectories(String[] directories) {
		this.directories = directories;
	}
	
	public String getIndexPath() {
		return indexPath;
	}
	
	private void optimizeIndex() throws Exception {
		indexer.optimizeIndex();
	}
	
	private void closeIndex() throws Exception {
		indexer.close();
	}
	
	private void closeDB() {
		JDBCHelper.shutDown();
	}
	
	protected int insert2DB(Product p) throws Exception {
		return productDao.addProduct(p);
	}

	protected void buildIndex(Product p, int nextid) throws Exception {
		indexer.addProduct(p, nextid);
	}
	
	protected void process() throws Exception {

		if (directories == null || directories.length == 0) {
			closeIndex();
			return;
		}

		try {
			for (String dir : directories) {
				File f = new File(dir);
				traverse(f);
			}
			optimizeIndex();
			closeDB();
//			Thread.sleep(10);
			closeIndex();
		} catch (Exception e) {
			closeIndex();
			e.printStackTrace();
		}
	}
	
	private void traverse(File dir) throws Exception {		
		File[] files = dir.listFiles();
		BufferedReader reader = null;
		for (File file : files) {
			String fname = file.getName();
			System.out.println(fname);
			reader = new BufferedReader(new FileReader(file));
						
			String phoneInfo = reader.readLine();//line 1
			String name = phoneInfo.replace("_", " ");//重新换回空格
//			String name,type;
//			if (phoneInfo.contains("_")){
//				name = phoneInfo.substring(0, phoneInfo.indexOf("_"));
//				type = phoneInfo.substring(phoneInfo.indexOf("_")+1, phoneInfo.length())
//						.replace("_", " ");//重新换回空格
//			}else {
//				name = phoneInfo;
////				type = phoneInfo;
//			}
			String imageURI = reader.readLine();//line 2
			reader.readLine();//line 3 seperator
			String url = reader.readLine();//line 4	
			String updatedtime = String.valueOf(df.format(new Date()));
			
			//content start from line 5
			StringBuffer content = new StringBuffer();
			
			String type = reader.readLine();//line 5 型号							
			content.append(type).append("\r\n");//将型号加入content中
			
			String line = reader.readLine();
			while (line != null){
				content.append(line).append("\r\n");
				line = reader.readLine();
			}		
			
			// make the Product object
			Product p = new Product();
//			p.setCategory("手机");
			p.setName(name);
			p.setType(type.replace("型号:", ""));
			p.setImageURI(imageURI);
			p.setOriginalUrl(url);
			
			String contentstr = content.toString();
			p.setContent(contentstr);
			if (contentstr.length() > SUMMARY_LENGTH) {
				p.setSummary(contentstr.substring(0,SUMMARY_LENGTH-1));
			}
			else
				p.setSummary(contentstr);
			
			p.setUpdatedtime(updatedtime);

			int id = insert2DB(p);
			if(id>0){
				// now we are trying to build Lucene document
				buildIndex(p, id);
			}
			
//			Thread.sleep(10);
		}
		reader.close();
	}
	
	public static void main (String [] args) throws Exception {
		ProductTextFileProcessor pro = new ProductTextFileProcessor();
		pro.initialize();
		
		String path1 = PropertyConfiguration.getProductMobileDir();
//		String path1 = "d:\\product\\tom\\mobile\\";
		//String path1 = "d:\\product\\pconline\\mobile\\";
		pro.setDirectories(new String[]{path1});
		pro.process();
	}
}
