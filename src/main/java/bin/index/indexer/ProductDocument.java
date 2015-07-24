package bin.index.indexer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import bin.index.db.Product;

public class ProductDocument {

	private static final String PRODUCT_ID = "productid";

//	private static final String INDEX_TIME = "indextime";
	
//	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//	private static final String PRODUCT_URL = "producturl";

	private static final String PRODUCT_NAME = "name";
	private static final String PRODUCT_CONTENT="content";
	private static final String PRODUCT_TYPE = "type";
//	/**不索引、不分类，只存储，适用于完整匹配，如URI等**/
//	private static FieldType fieldType;
//	static{
//		fieldType = new FieldType();
//		fieldType.setIndexed(false);//set 是否索引
//		fieldType.setStored(true);//set 是否存储
//		fieldType.setTokenized(false);//set 是否分类
//	}
	
	/**
	 * StringField默认是不分词的，所以只能完整搜索。
	 * TextFiled默认分词，所以要索引字段使用。
	 * LongFiled主要用于存储时间和价格之类的数字
	 * 而FieldType默认是分词的，为了不分词，setIndexed(false)，setTokenized(false)
	 * URI要使用Filed,只存储就好
	 * **/
	public static Document buildProductDocument(Product product, int id) {
		Document doc = new Document();
		Field identifier = new IntField(PRODUCT_ID, id, Field.Store.YES);
		Field name = new TextField(PRODUCT_NAME, product.getName(),
				Field.Store.YES);
		name.setBoost(5.0f);//设置优先权重
		
		Field content=new TextField(PRODUCT_CONTENT, product.getContent(),
				Field.Store.YES);
		content.setBoost(3.0f);
		
		Field type = new TextField(PRODUCT_TYPE, product.getType(),
				Field.Store.YES);
		doc.add(identifier);
		doc.add(name);
		doc.add(type);
		doc.add(content);
		return doc;

	}
}
