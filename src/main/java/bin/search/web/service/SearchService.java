package bin.search.web.service;

import bin.search.web.bo.SearchRequest;
import bin.search.web.bo.SearchResult;
import bin.search.web.bo.SearchResults;

public interface SearchService {
	
	public abstract SearchResults getSearchResults(SearchRequest request);

	public abstract SearchResult getSearchResultById(int id);
	
}
