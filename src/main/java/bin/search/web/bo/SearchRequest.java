package bin.search.web.bo;

public class SearchRequest {

	private int page = 0;//总page数

	private int numofpage;//当前page

	private int startindex;

	private String query;

	/**总page数目**/
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getStartindex() {
		return startindex;
	}

	public void setStartindex(int startindex) {
		this.startindex = startindex;
	}

	/**当前pageNum**/
	public int getNumofpage() {
		return numofpage;
	}

	public void setNumofpage(int numofpage) {
		this.numofpage = numofpage;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String toString() {
		return "page:"+page+
				" numofpage:"+numofpage+
				" startindex:"+startindex+
				" query:"+query;
	}
	
}
