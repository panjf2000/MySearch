package bin.search.web.dao;

import bin.search.web.bo.SearchResult;

public interface SearchResultDao {
	
	public abstract SearchResult getSearchResultById(int id);
}
