package bin.index.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class JsoupExtractor {
	
	protected static final String lineSeparator = System
			.getProperty("line.separator", "\n");
	protected static final String tSeparator = "\t";
	
	
//	static int count = 0;
	/**
	 * 表示所有结果的输出路径
	 */
	private String outputPath = "";

	/**
	 * 表示当前正在被处理的文件
	 */
	private String inuputFilePath;

	/**
	 * 表示当前所有被抓取的网页的镜象根目录 在Heritrix用mirror目录表示
	 */
	private String mirrorDir = "";

	/**
	 * 用于存放被处理过后的产口的图片的目录
	 */
	private String imageDir = "D:/data/dst/pic/";
	/**
	 * 对图片路径进行哈希的算法，这里采用MD5算法
	 */
	protected static final String HASH_ALGORITHM = "md5";
	/**
	 * 分隔符
	 */
	public static final String SEPARATOR = "======================";
	
	/**
	 * 抽象方法，从指定路径
	 */
	public abstract Document loadDocFromFile(String path);
	
	/**
	 * 抽象方法， 其功能是遍历当前路径下的所有文件和文件夹,
	 * 然后执行extract
	 * **/
	public abstract void traverse(String path);
	
//	/**
//	 * 装载需要的网页文件
//	 * 
//	 */
//	public Document loadPcOnlineFile(String path) {
//		try {
//			setInuputFilePath(path);
//			//先处理文件，转换为对应的文件名和URL
//			String url = "http://"+path.substring(path.indexOf("pconline_mobile/")+16);
//			System.out.println(url);
//			File input = new File(path);
//			Document doc = Jsoup.parse(input, "GBK", url);
//			return doc;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	public Document load163File(String path) {
//		try {
//			setInuputFilePath(path);
//			//先处理文件，转换为对应的文件名和URL
//			String url = "http://"+path.substring(path.indexOf("mirror")+7);
//			System.out.println(url);
//			File input = new File(path);
//			Document doc = Jsoup.parse(input, "GBK", url);
//			return doc;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	/**
	 * 抽象方法，用于供子类实现。 其功能主要是
	 * 解析网页文件、得到产品信息保存到固定的路径
	 */
	public abstract void extract(String uri);
	
	/** jdk5
	 * 从mirror目录下拷贝文件至所设定的图片目录
	 * 该方法可能需要被改变
	 * @throws UnsupportedEncodingException 
	 */
	protected boolean copyImage(String image_url, String new_image_file) throws UnsupportedEncodingException {
//		image_url = "http://s.cimg.163.com/i/img6.ph.126.net/5lJ-v-xYywjwRt_Ef2_WyQ=="
//				+ "/6598246244097724976.jpg.10000x120.auto.jpg";
		//对URL进行解码，去掉%2F等特殊字符
		String decodeUrl = URLDecoder.decode(image_url, "UTF-8");
		//7,去掉http://
		String dirs = decodeUrl.substring(7);
		try {
			// instance the File as file_in and file_out
			File file_in = new File(new File(mirrorDir), dirs);
			System.out.println(file_in.getAbsolutePath());
			if (file_in == null || !file_in.exists()) {
				file_in = new File("E:/APP/noimage.jpg");
			}
			
			File file_out = new File(new File(imageDir), new_image_file);
			
			FileInputStream in1 = new FileInputStream(file_in);
			FileOutputStream out1 = new FileOutputStream(file_out);

			byte[] bytes = new byte[1024];
			int c;
			while ((c = in1.read(bytes)) != -1)
				out1.write(bytes, 0, c);

			// close
			in1.close();
			out1.close();
			return (true); // if success then return true
		} catch (Exception e) {
			e.printStackTrace();
			return (false); // if fail then return false
		}
	}
	
	/**复制图片，从路径image_url复制到new_image_file
	 * 注意：文件已经存在的情况下，直接返回true
	 * **/
	protected boolean nioCopyImage(String image_url, String new_image_file) throws UnsupportedEncodingException {
		Path to = Paths.get(imageDir,new_image_file);
		if(to.toFile().exists())//已经存在，则直接返回true
			return true;
		String decodeUrl = URLDecoder.decode(image_url, "UTF-8");
		//7,去掉http://
		String dirs = decodeUrl.substring(7);
		Path from = Paths.get(mirrorDir,dirs);
		if (from == null || !from.toFile().exists()) {
			from = Paths.get("E:/APP/noimage.jpg");
		}		
		try {
			Files.copy(from, to);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

//	public static void dir163(JsoupExtractor extractor, File f){
//	     //获得当前路径下的所有文件和文件夹
//	      File[] allFiles = f.listFiles();
//	     //循环所有路径
//	      for(int i = 0;i < allFiles.length;i++){
//	           //如果是文件夹
//	            if(allFiles[i].isDirectory()){
//	              //递归调用
//	                dir163(extractor,allFiles[i]);             
//	           }else{ //文件
//	                 //执行操作，例如输出文件名
//	        	   String filePath = allFiles[i].getName();
//	        	   if ( filePath.endsWith("img.html") 
//	       				||filePath.endsWith("param.html") 
//	       				||(filePath.endsWith("index.html") && filePath.endsWith(".html"))
//	       					) {
//	        		   	count++;
//	        		   	String linuxPath = f.getAbsolutePath()+"\\"+filePath;
//	        			linuxPath = linuxPath.replace("\\", "/")
//	        					.replace("（", "(")
//	        					.replace("）", ")");
//	   					extractor.load163File(linuxPath);
//	   					extractor.extract();
//	        	   }	        	   
////	                 System.out.println(f.getAbsolutePath()+"\\"+allFiles[i].getName());
//	           }
//	      }
//	}
//	
//	public static void traverse163(JsoupExtractor extractor, File path)
//			throws Exception {
//		if (path == null) {
//			return;
//		}
//
//		if (path.isDirectory()) {
//			String[] files = path.list();
//			for (int i = 0; i < files.length; i++) {
//				traverse163(extractor, new File(path, files[i]));
//			}
//		} else {
//			String pathname = path.getAbsolutePath();
//			System.out.println(pathname);
//			
//			String name = path.getName();
//			if ( path.getAbsolutePath().endsWith("img.html") 
//				||path.getAbsolutePath().endsWith("param.html") 
//				||(!path.getAbsolutePath().endsWith("index.html") && path.getAbsolutePath().endsWith(".html"))
//					) {
//				count++;
//				extractor.load163File(path.getAbsolutePath());
//				extractor.extract();
//			}
//		}
//	}
//	
//	public static void traversePcOnline(JsoupExtractor extractor, File path)
//			throws Exception {
//		if (path == null) {
//			return;
//		}
//
//		if (path.isDirectory()) {
//			String[] files = path.list();
//			for (int i = 0; i < files.length; i++) {
//				traversePcOnline(extractor, new File(path, files[i]));
//			}
//		} else {
//			String pathname = path.getAbsolutePath();
//			System.out.println(pathname);
//			
//			String name = path.getName();
//			if ( path.getAbsolutePath().endsWith("detail.html") 
//				||path.getAbsolutePath().endsWith("param.html") 
//				||(!path.getAbsolutePath().endsWith("index.html") && path.getAbsolutePath().endsWith(".html")&& path.getName().indexOf("_") == -1 )
//					) {
//				System.out.println(path);
//				count++;
//				extractor.loadPcOnlineFile(path.getAbsolutePath());
//				extractor.extract();
//			}
//		}
//	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		File file = new File(outputPath);
		if(!file.exists())
			file.mkdirs();
	}

	public String getInuputFilePath() {
		return inuputFilePath;
	}

	public void setInuputFilePath(String inuputFilePath) {
		this.inuputFilePath = inuputFilePath;
	}

	public String getMirrorDir() {
		return mirrorDir;
	}

	public void setMirrorDir(String mirrorDir) {
		this.mirrorDir = mirrorDir;
		File file = new File(mirrorDir);
		if(!file.exists())
			file.mkdirs();
	}

	public String getImageDir() {
		return imageDir;
	}

	public void setImageDir(String imageDir) {
		this.imageDir = imageDir;
		File file = new File(imageDir);
		if(!file.exists())
			file.mkdirs();
	}	
	
}
