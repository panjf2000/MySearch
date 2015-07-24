package bin.index.dic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class BuildWordVacabulary {
	
	protected String wordvacabularypath = null;

	protected ArrayList<String> wordlist = new ArrayList<String>();
	
//	protected Set<String> wordSet = new HashSet<String>();

	public abstract void loadProductFromDirectory(String[] dirs)throws IOException;
	
	protected void writeToFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				wordvacabularypath));
		for (String string : wordlist) {
			writer.write(string);
			writer.newLine();
		}
		writer.close();

	}

}
