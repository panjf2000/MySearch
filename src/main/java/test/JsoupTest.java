package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupTest {

	public static void main(String[] args) {
		String htmlCode = "<h1>1-call_I9300</h1>"+
				"\r\n"+"<img src='ed3a0ae95b1bdefb2174d109a4b8f86c.jpg'>";
		testParsedHtml(htmlCode);
	}

	private static void testParsedHtml(String htmlCode) {
		Document document = Jsoup.parse(htmlCode);
		String h1 = document.select("h1").first().text();
		String imgUrl = document.select("div.largerPic img").first().attr("src");
		System.out.println("h1:"+h1+"\n"+
				"imgUrl:"+imgUrl);
	}
}
