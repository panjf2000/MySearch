package bin.index.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ProductDao {
	
	private Connection con = null;

	private Statement stmt = null;

	private ResultSet rs = null;

//	private PreparedStatement pstmt = null;
//
//	private boolean autoCommit = true;
	
	/**
	 * 执行INSERT/UPDATE/DELETE语句,之后close Conn
	 * **/
	public int addProduct(Product p) throws Exception{
//		int nextid = getNextId();

//		if (nextid < 0) {
//			throw new Exception("Can't get next id.");
//		}

		// since we get the next id, add the info to db
		String content = p.getContent();
		String summary = p.getSummary();
		String imageURI = p.getImageURI();
		String originalUrl = p.getOriginalUrl();
//		String category = p.getCategory();
		String name = p.getName();
		String type = p.getType();
		String updatedtime = p.getUpdatedtime();
		
		String sql = "insert into product (content, abstractcontent, url, imageurl, name, type, updatedtime) values(?,?,?,?,?,?,?)";
		if (JDBCHelper.update
				(sql,content,summary,originalUrl,imageURI,name,type,updatedtime)) {
//			return nextid;
			return getLastId();
		}
		return -1;
	}
	
	private int getLastId() throws Exception{
		String sql = "select max(id) from product";
		int result = -1;
//		result = (Integer)JDBCHelper.readObjectArray(sql, null)[0];

		con = JDBCHelper.getConn();
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			result = rs.getInt(1);
		}
		rs.close();
		stmt.close();
		con.close();
		return result;
	}
	
	private int getNextId() throws Exception {
//		select max(id)+1 from product,returns null where no data
//		but conut(*)+1 will returns 1;
		String sql="select conut(*)+1 from product";
//		String sql = "select max(id)+1 from product";
		int result = -1;
//		result = (Integer)JDBCHelper.readObjectArray(sql, null)[0];

		con = JDBCHelper.getConn();
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			result = rs.getInt(1);
		}
		rs.close();
		stmt.close();
		con.close();
		return result;
	}

}
