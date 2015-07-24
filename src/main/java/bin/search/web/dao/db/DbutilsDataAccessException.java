package bin.search.web.dao.db;

import org.springframework.dao.DataAccessException;

@SuppressWarnings("serial")
public class DbutilsDataAccessException extends DataAccessException {
 
    public DbutilsDataAccessException(String msg) {
        super(msg);
    }
 
    public DbutilsDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
