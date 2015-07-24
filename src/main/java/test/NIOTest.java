package test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class NIOTest {

	public static void main(String[] args) {
		String uri = "E:\\aaa";
		String uri2 = "aa.txt";
		testPath(uri,uri2);
	}

	private static void testPath(String uri,String uri2) {
		Path from = Paths.get(uri,uri2);
		System.out.println(from.toString());
		if (from == null || !from.toFile().exists()) {
			System.out.println("the path does not exists");
			from = Paths.get("d:/data/noimage.jpg");
		}
	}
	
	
}
