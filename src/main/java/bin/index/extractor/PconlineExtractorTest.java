package bin.index.extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import bin.search.util.StringUtils;

public class PconlineExtractorTest {

	protected static final String lineSeparator = System
			.getProperty("line.separator", "\n");
	protected static final String tSeparator = "\t";
	public static final String SEPARATOR = "======================";
	
	public static void main(String[] args) {
		String url = "D:/data/pconline_mobile/product.pconline.com.cn/mobile/35phone/541422.html";
		testExtract(url);
	}
	
	public static Document loadDocFromFile(String path) {
		try {
//			setInuputFilePath(path);
			//先处理文件，转换为对应的文件名和URL
			String url = "http://"+path.substring(path.indexOf("pconline_mobile")+16);
			System.out.println(url);
			File input = new File(path);
			Document doc = Jsoup.parse(input, "GBK", url);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected static boolean nioCopyImage(String image_url, String new_image_file) throws UnsupportedEncodingException {
		String decodeUrl = URLDecoder.decode(image_url, "UTF-8");
		//7,去掉http://
		String dirs = decodeUrl.substring(7);
		Path from = Paths.get("d:/data/pconline_mobile/",dirs);
		if (from == null || !from.toFile().exists()) {
			from = Paths.get("d:/data/noimage.jpg");
		}
		Path to = Paths.get("d:/data/dst/pc_pic/",new_image_file);
		try {
			Files.copy(from, to);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void testExtract(String uri) {
		boolean isIndex = uri.matches(".*([\\w/.]+)\\d+.html");
		boolean isParam=uri.endsWith("detail.html");//param.html
//		BufferedWriter bw = null;
		try {
			//先处理文件，转换为对应的文件名和URL
			Document doc = loadDocFromFile(uri);
			if (null==doc) {
				return;
			}
			//直接通过http解析网页
			//1、解析标题，H1是大标题，可能包含文字，H2是小标题，不包含中文
			String h1 = doc.select("h1").first().text().replace(" ", "_").replace("/", "_");
//			bw.write(h1+lineSeparator);
			System.out.println(h1);
			String title = h1.replace(" ", "_")
					.replace("（", "(").replace("）", ")")
					.replace("/", "_");
//			title = title.replace(" ", "_");
//			if(!title.contains("_")){
//				return;
//			}else {
//				title = title+"_"+System.currentTimeMillis();
				int y,m,d;
//				int h,mi,s;
				Calendar cal=Calendar.getInstance();
				y=cal.get(Calendar.YEAR);
				m=cal.get(Calendar.MONTH);
				d=cal.get(Calendar.DATE);
				title = title+"_"+y+m+d;
//				h=cal.get(Calendar.HOUR_OF_DAY);
//				mi=cal.get(Calendar.MINUTE);
//				s=cal.get(Calendar.SECOND);
//				System.out.println("现在时刻是"+y+"年"+m+"月"+d+"日"+h+"时"+mi+"分"+s+"秒");
//			}
//			title = title.replace("（", "(").replace("）", ")")
//					.replace("/", "_");
//			System.out.println(title);
//			Elements h2 = doc.select("h2");
//			if(null!=h2){
//				title = h2.get(0).text();
//			}
			//创建要生成的文件
//			File  file = new File("d:/data/dst/pc_mobile/" + title + ".txt");
//			bw = new BufferedWriter(new FileWriter(file,true));
			if (isIndex) {
//				bw.write(title + lineSeparator);
//				System.out.println(title);
				//处理单张大图
				Element img = doc.select("div.largerPic img").first();
				String image_url = img.attr("abs:src");
				String fileType = image_url.substring(image_url
						.lastIndexOf(".") + 1);
				//生成新的图片的文件名
				String new_iamge_file = StringUtils.encodePassword(
						image_url, "md5")
						+ "." + fileType;
				image_url = StringUtils.replace(image_url, "+", " ");
				//利用mirror目录下的图片生成的新的图片
				nioCopyImage(image_url, new_iamge_file);
//				bw.write(SEPARATOR + lineSeparator);
//				bw.write(new_iamge_file + lineSeparator);	
//				System.out.println(SEPARATOR);
//				System.out.println(new_iamge_file);
//				System.out.println(image_url);
			}else if(isParam){
				//2、解析表格，得到相关的配置属性
				Elements paramTrs = doc.select("table#JparamTable tbody tr");
				for (Element tr:paramTrs) {
					String name = tr.select("th").text();
					tr.select("p").remove();//移除无关文字
					String data = tr.select("td").text();
//						bw.write(name+":"+data+lineSeparator);
//					bw.write(data+lineSeparator);
					System.out.println(name+":"+data+tSeparator);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try{
//			if (bw != null)
//				bw.close();
//		}catch(IOException e){
//			e.printStackTrace();
//		}
	}

}
