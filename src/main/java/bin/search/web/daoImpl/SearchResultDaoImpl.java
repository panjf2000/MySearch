package bin.search.web.daoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.dbutils.ResultSetHandler;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import bin.search.web.bo.SearchResult;
import bin.search.web.dao.SearchResultDao;
import bin.search.web.dao.db.DbutilsTemplate;

@Repository("searchResultDao")
public class SearchResultDaoImpl implements SearchResultDao {
	
	@Resource(name="dbutils")
	private DbutilsTemplate template;
	
//	private DataSource dataSource;
//
//	public DataSource getDataSource() {
//		return dataSource;
//	}
//
//	public void setDataSource(DataSource dataSource) {
//		this.dataSource = dataSource;
//	}

	public DbutilsTemplate getTemplate() {
		return template;
	}
//
//	public void setTemplate(DbutilsTemplate template) {
//		this.template = template;
//	}

	public SearchResult getSearchResultById(int id) {
		
		SearchResult sr = template.query("select * from product where id=?"
				,new ResultSetHandler<SearchResult>() {
			@Override
			public SearchResult handle(ResultSet rs) throws SQLException {
				SearchResult sr = new SearchResult();
				
				try{
					if (rs.next()) {					
						sr.setAbstractContent(rs.getString("abstractcontent"));
						sr.setContent(rs.getString("content"));
						sr.setImageUrl(rs.getString("imageurl"));
						sr.setUrl(rs.getString("url"));
						sr.setName(rs.getString("name"));
						sr.setType(rs.getString("type"));
						sr.setId(rs.getInt("id"));
						sr.setIndexCreateTime(rs.getString("updatedtime"));
	//					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	//					String time = rs.getString("updatedtime");
	//
	//					if (time != null && !time.trim().equals("")) {
	//						Date d = new Date(Long.parseLong(time));
	//						String timestr = sf.format(d);
	//						sr.setIndexCreateTime(timestr);
	//					}
	//					else {
	//						sr.setIndexCreateTime("undefined");
	//					}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				return sr;
			}
		},id);
//		final int id_db = id;
//		final SearchResult sr = new SearchResult();
//		JdbcTemplate template = new JdbcTemplate(dataSource);
//		template.query("select * from product where id=?",
//				new PreparedStatementSetter() {
//
//					public void setValues(PreparedStatement ps)
//							throws SQLException {
//						ps.setInt(1, id_db);
//
//					}
//				}, new RowCallbackHandler() {
//
//					public void processRow(ResultSet rs) throws SQLException {
//						try{
//							sr.setAbstractContent(rs.getString("abstractcontent"));
//							sr.setContent(rs.getString("content"));
//							sr.setImageUrl(rs.getString("imageurl"));
//							sr.setUrl(rs.getString("url"));
//							sr.setName(rs.getString("name"));
//							sr.setType(rs.getString("type"));
//							sr.setId(rs.getInt("id"));
//							
//							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//							String time = rs.getString("updatedtime");
//							
//							if (time != null && !time.trim().equals("")) {
//								Date d = new Date(Long.parseLong(time));
//								String timestr = sf.format(d);
//								sr.setIndexCreateTime(timestr);
//							}
//							else {
//								sr.setIndexCreateTime("undefined");
//							}
//							
//						}catch(Exception e){
//							e.printStackTrace();
//						}
//						
//						
//					}
//				});
		return sr;
	}

}
