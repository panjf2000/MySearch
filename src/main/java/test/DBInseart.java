package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.ParseConversionEvent;

import bin.index.db.Product;
import bin.index.db.ProductDao;
import bin.index.extractor.JsoupExtractor;

public class DBInseart {
	
	public final static int SUMMARY_LENGTH = 80;
	static ProductDao dao = null;
	

	public static void main(String[] args) {
		String path = "D:/data/dst/pc_mobile/";
		traverse(path);
//		inseart(Parse(path));
	}
	
	private static void traverse(String path){
		File dir = new File(path);
		File[] files = dir.listFiles();
		dao = new ProductDao();
		for (final File file : files) {
			inseart(Parse(file));
		}
	}

	private static Product Parse(File file) {
		try {	
			String fname = file.getName();
			System.out.println(fname);
			BufferedReader reader = new BufferedReader(new FileReader(file));
						
			String phoneInfo = reader.readLine();//line 1
			String name,type;
			if (phoneInfo.contains("_")){
				name = phoneInfo.substring(0, phoneInfo.indexOf("_"));
				type = phoneInfo.substring(phoneInfo.indexOf("_")+1, phoneInfo.length());
			}else {
				name = phoneInfo;
				type = "";
			}
			String imageURI = reader.readLine();//line 2
			reader.readLine();//line 3 seperator
			String url = reader.readLine();//line 4	
			String updatedtime = String.valueOf(System.currentTimeMillis());
			//content start from line 5
			StringBuffer content = new StringBuffer();
			String line = reader.readLine();
			while (line != null && !line.equals(JsoupExtractor.SEPARATOR)){
				content.append(line).append("\r\n");
				line = reader.readLine();
			}		
			
			// make the Product object
			Product p = new Product();
	//		p.setCategory("手机");
			p.setName(name);
			p.setType(type);
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
			System.out.println("waiting for insert to db and getID");
			return p;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	private static Product Parse(String path) {
		try {	
			File file = new File(path);
			String fname = file.getName();
			System.out.println(fname);
			BufferedReader reader = new BufferedReader(new FileReader(file));
						
			String phoneInfo = reader.readLine();//line 1
			String name,type;
			if (phoneInfo.contains("_")){
				name = phoneInfo.substring(0, phoneInfo.indexOf("_"));
				type = phoneInfo.substring(phoneInfo.indexOf("_")+1, phoneInfo.length())
						.replace("_", " ");
			}else {
				name = phoneInfo;
				type = "";
			}
			String imageURI = reader.readLine();//line 2
			reader.readLine();//line 3 seperator
			String url = reader.readLine();//line 4	
			String updatedtime = String.valueOf(System.currentTimeMillis());
			//content start from line 5
			StringBuffer content = new StringBuffer();
			String line = reader.readLine();
			while (line != null && !line.equals(JsoupExtractor.SEPARATOR)){
				content.append(line).append("\r\n");
				line = reader.readLine();
			}		
			
			// make the Product object
			Product p = new Product();
	//		p.setCategory("手机");
			p.setName(name);
			p.setType(type);
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
			System.out.println("waiting for insert to db and getID");
			return p;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	private static void inseart(Product product) {
		try {
			 int id = dao.addProduct(product);
			 System.out.println(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
