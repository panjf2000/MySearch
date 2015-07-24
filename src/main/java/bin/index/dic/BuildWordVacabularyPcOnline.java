package bin.index.dic;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bin.search.util.PropertyConfiguration;

public class BuildWordVacabularyPcOnline extends BuildWordVacabulary{
	
	final Logger logger = LoggerFactory.getLogger(BuildWordVacabularyPcOnline.class);	
	

	@Override
	public void loadProductFromDirectory(String[] dirs) throws IOException {
		String fullname = null;
		try {
			if (wordvacabularypath == null) {
				throw new IOException("词库存放的文件没有指定，无法生成！");
			}
			int length = dirs.length;
			if (length<=0) {
				logger.error("the length od stringArray <=0");
				return;
			}
			for (String folder : dirs) {
				File foo = new File(folder);
				File[] files = foo.listFiles();
				for(File f:files){
					fullname = f.getName().split("\\.")[0];
					if (fullname.contains("_")) {
						fullname = fullname.substring(0, fullname.indexOf("_"));
					}
//					if (fullname.contains("_")) {
//						String name = fullname.substring(0, fullname.indexOf("_"));
//						String type = fullname.substring(fullname.indexOf("_"),
//								fullname.length());
//						logger.info(name+"_"+type);
//						if (!wordlist.contains(name+type)) {
//							wordlist.add(name+type);
//						}
//					}else {						
						if (!wordlist.contains(fullname)) {
							wordlist.add(fullname);
							logger.info(fullname);
						}
//					}
				}

			}
			Collections.sort(wordlist);
			writeToFile();
		} catch (Exception e) {
			System.out.println(fullname);
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		BuildWordVacabularyPcOnline builder = new BuildWordVacabularyPcOnline();
		builder.wordvacabularypath = (PropertyConfiguration.getWordDictionary());
		builder.loadProductFromDirectory(new String[] { PropertyConfiguration.getProductMobileDir() });
//		builder.setWordvacabularyPath("d:\\data\\word.txt");
//		builder.loadProductFromDirectory(new String[] { "d:\\product\\tom\\mobile\\" });
	}

}
