package bin.search.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PropertyConfiguration {

	/* property file identifier */
	private static String CONFIG_FILE = "app";

	private static ResourceBundle bundle;

	static {
		try {
			bundle = ResourceBundle.getBundle(CONFIG_FILE);
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}
	}

	private static String getValue(String key) {
		return bundle.getString(key);
	}

	public static String getDBUrl() {
		String val = getValue("jdbc.url");
		return val;
	}

	public static String getDBUsr() {
		String val = getValue("jdbc.username");
		return val;
	}

	public static String getDBPwd() {
		String val = getValue("jdbc.password");
		return val;
	}

	public static String getProductDir() {
		String val = getValue("product.directory");
		return val;
	}

	public static String getIndexStorePath() {
		String val = getValue("product.index.directory");
		return val;
	}

	public static String getProductMobileDir() {
		String val = getValue("product.mobile.directory");
		return val;
	}

	public static String getProductImageDir() {
		String val = getValue("product.image.directory");
		return val;
	}

	public static String getWordDictionary() {
		String val = getValue("word.dictionary.file");
		return val;
	}
}
