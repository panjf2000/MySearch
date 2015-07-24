package bin.search.web.bo;

import java.util.ArrayList;

public class SearchResults {

	/**
	 * 存储ID的ArrayList
	 * **/
	private ArrayList<String> results = new ArrayList<String>();

	private int startindex;

	private int minpage;

	private int maxpage;
	
	private int hasnext;

	public int getHasnext() {
		return hasnext;
	}

	public void setHasnext(int hasnext) {
		this.hasnext = hasnext;
	}

	public int getMaxpage() {
		return maxpage;
	}

	public void setMaxpage(int maxpage) {
		this.maxpage = maxpage;
	}

	public int getMinpage() {
		return minpage;
	}

	public void setMinpage(int minpage) {
		this.minpage = minpage;
	}

	/**
	 * ArrayList<String>,存储的是ID
	 * **/
	public ArrayList<String> getResults() {
		return results;
	}

	public void setResults(ArrayList<String> results) {
		this.results = results;
	}

	public int getStartindex() {
		return startindex;
	}

	public void setStartindex(int startindex) {
		this.startindex = startindex;
	}

}
