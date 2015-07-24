package bin.index.db;

public class Product {

//	private String category = null;

	private String name = null;

	private String type = null;

	private String content = null;

	private String summary = null;

	private String originalUrl = null;

	private String imageURI = null;

	private String updatedtime = null;

	public String getUpdatedtime() {
		return updatedtime;
	}

	public void setUpdatedtime(String updatedtime) {
		this.updatedtime = updatedtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageURI() {
		return imageURI;
	}

	public void setImageURI(String imageURI) {
		this.imageURI = imageURI;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

//	public String getCategory() {
//		return category;
//	}
//
//	public void setCategory(String category) {
//		this.category = category;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
