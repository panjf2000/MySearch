package bin.index.extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bin.search.util.PropertyConfiguration;
import bin.search.util.StringUtils;

public class PconlineExtractor extends JsoupExtractor {

	final Logger logger = LoggerFactory.getLogger(PconlineExtractor.class);
	final static String mirrorDir = PropertyConfiguration.getProductDir();

	public static void main(String[] agrs) throws Exception {

		PconlineExtractor ex = new PconlineExtractor();
		// ex.setOutputPath("D:/product/tom/");
		ex.setMirrorDir(mirrorDir);
		ex.setOutputPath(PropertyConfiguration.getProductMobileDir());
		// test
		// ex.setOutputPath("D:/data/dst/");
		ex.setImageDir(PropertyConfiguration.getProductImageDir());
		ex.traverse(mirrorDir + "product.pconline.com.cn/mobile/");
		// ex.traverse(mirrorDir+"/product.pconline.com.cn/mobile/100+");
		// test
		// ex.loadFile("D:/data/163_mobile/mirror/product.mobile.163.com/Apple/00000YYX/index.html");
	}

	@Override
	public Document loadDocFromFile(String path) {
		try {
			// setInuputFilePath(path);
			logger.info(path);
			// 先处理文件，转换为对应的文件名和URL
			String url = "http://"
					+ path.replace("\\", "/").substring(mirrorDir.length());
			logger.info("httpUri is:{}", url);
			File input = new File(path);
			Document doc = Jsoup.parse(input, "utf8", url);
			return doc;
		} catch (Exception e) {
			logger.error(e.getMessage());
			// e.printStackTrace();
		}
		return null;
	}

	@Override
	public void traverse(String path) {
		// try {
		// Files.walkFileTree(Paths.get(path),new PcOnlineFileVisitor());
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		File root = new File(path);
		File[] files = root.listFiles();
		if (null == files) {
			if (root.isDirectory()) {
				logger.info("only a dir,not sub dir");
				traverseDirs(root);
			} else if (root.isFile()) {
				logger.info("only a file");
				String fpath = root.getAbsolutePath();
				if (!fpath.endsWith(".shtml") && !fpath.endsWith("index.html"))
					extract(fpath);
			} else {
				logger.info(root.getAbsolutePath());
			}
		} else {
			logger.info("creat a pool with size:{}", files.length);
			final ExecutorService service = Executors
					.newFixedThreadPool(files.length);
			for (final File file : files) {
				if (file.isDirectory()) {
					service.submit(new Runnable() {

						@Override
						public void run() {
							traverseDirs(file);
						}
					});
				} else {// 不处理初始的index.html以及其下一页
					String fpath = file.getAbsolutePath();
					if (!fpath.endsWith(".shtml")
							&& !fpath.endsWith("index.html"))
						extract(fpath);
				}
			}
			service.shutdown();
		}
	}

	private void traverseDirs(File dir) {
		File[] htmls = dir.listFiles();
		for (File html : htmls) {
			String path = html.getAbsolutePath();
			if (!path.endsWith(".shtml") && !path.endsWith("index.html"))
				extract(path);
		}
	}

	@Override
	public void extract(String uri) {
		boolean isIndex = uri.matches(".*([\\w/.]+)\\d+.html");
		boolean isParam = uri.endsWith("detail.html");// param.html
		BufferedWriter bw = null;

		try {
			// 先处理文件，转换为对应的文件名和URL
			Document doc = loadDocFromFile(uri);
			if (null == doc) {
				return;
			}
			// 直接通过http解析网页
			// 1、解析标题，H1是大标题
			String h1 = doc.select("h1").first().text().replace(" ", "_")
					.replace("/", "_");

			// 创建要生成的文件
			File file = new File(getOutputPath() + h1 + ".txt");
			// true,append words into the file
			bw = new BufferedWriter(new FileWriter(file, true));
			if (isIndex) {
				bw.write(h1 + lineSeparator);
				// 处理单张大图
				// Element img = doc.select("div.largerPic img").first();
				Element img = doc.select("img").first();
				String image_url = img.attr("abs:src");
				String fileType = image_url.substring(image_url
						.lastIndexOf(".") + 1);
				// 生成新的图片的文件名
				String new_iamge_file = StringUtils.encodePassword(image_url,
						"md5") + "." + fileType;
				image_url = StringUtils.replace(image_url, "+", " ");
				// 利用mirror目录下的图片生成的新的图片
				nioCopyImage(image_url, new_iamge_file);
				bw.write(new_iamge_file + lineSeparator);
				bw.write(SEPARATOR + lineSeparator);
				// System.out.println(SEPARATOR);
				// System.out.println(new_iamge_file);
			} else if (isParam) {
				String url = "http://"
						+ uri.replace("\\", "/").substring(mirrorDir.length());
				bw.write(url + lineSeparator);
				// 2、解析表格，得到相关的配置属性
				Elements paramTrs = doc.select("table#JparamTable tbody tr");
				for (Element tr : paramTrs) {
					String name = tr.select("th").text();
					tr.select("p").remove();// 移除无关文字
					String data = tr.select("td").text();
					bw.write(name + ":" + data + lineSeparator);
					// System.out.println(name+":"+data+tSeparator);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		try {
			if (bw != null)
				bw.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	// class PcOnlineFileVisitor extends SimpleFileVisitor<Path> {
	//
	// private void find(final Path path){
	// if(path.endsWith("index.html")||path.endsWith(".shtml"))
	// return;
	// extract(path.toString());
	// }
	//
	// /**load file success*/
	// @Override
	// public FileVisitResult visitFile(Path file,BasicFileAttributes attrs){
	// logger.info("解析文件{}",file.getFileName());
	// find(file);
	// return FileVisitResult.CONTINUE;
	// }
	//
	// /**Dir*/
	// @Override
	// public FileVisitResult preVisitDirectory(Path dir,BasicFileAttributes
	// attrs){
	// logger.info("解析目录{}",dir.getFileName());
	// return FileVisitResult.CONTINUE;
	// }
	//
	// /**Loading failed*/
	// @Override
	// public FileVisitResult visitFileFailed(Path file,IOException e){
	// System.out.println(e);
	// return FileVisitResult.CONTINUE;
	// }
	// }

}
