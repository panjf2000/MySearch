package bin.search.web.dao.db;

import java.sql.Connection;
import java.sql.SQLException;
 
public interface DbutilsCallback<T> {
    T execute(Connection connection) throws SQLException;
}
