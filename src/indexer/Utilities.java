package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A collection of utility methods for text processing.
 */
public class Utilities {
	/**
	 * Comparator used to sort the return list of NGramFrequencyCounter
	 * @author Jun
	 *
	 */
	public static class SorterNGrams implements Comparator <Frequency> {

		@Override
		public int compare(Frequency arg0, Frequency arg1) {
			// TODO Auto-generated method stub
			if (arg0.getFrequency() == arg1.getFrequency()) {
				return arg0.getText().compareTo(arg1.getText());
			} else if (arg0.getFrequency() < arg1.getFrequency()) {
				return 1;
			} else {
				return -1;
			}
		}
		
	}
	
	/**
	 * Comparator used to sort for PalindromeFrequencyCounter
	 * @author Jun
	 *
	 */
	public static class SorterPalindrome implements Comparator <Frequency> {

		@Override
		public int compare(Frequency arg0, Frequency arg1) {
			// TODO Auto-generated method stub
			if (arg0.getText().length() < arg1.getText().length()) {
				return 1;
			} else if (arg0.getText().length() > arg1.getText().length()) {
				return -1;
			}
			
			if (arg0.getFrequency() == arg1.getFrequency()) {
				return arg0.getText().compareTo(arg1.getText());
			} else if (arg0.getFrequency() < arg1.getFrequency()) {
				return 1;
			} else {
				return -1;
			}
		}
		
	}
	
	
	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * Example:
	 * 
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 */
	public static ArrayList<String> tokenizeFile(File input) {
		// TODO Write body!
		if (input == null) {
			return null;
		} else if (!input.isFile()) {
			return null;
		}
		
		ArrayList <String>  data = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(input))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        line = line.replaceAll("[^A-Za-z0-9 ]", "");
		        line = line.toLowerCase();
		        String[] tokens = line.split(" ");
		        for (String token : tokens) {
		        	token = token.trim();
		        	if (!token.isEmpty()) {
		        		data.add(token);	
		        	}
		        }
		    }
		    br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return data;
	}
	
	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique items.
	 * 
	 * Example one:
	 * 
	 * Given the input list of word frequencies
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total item count: 6
	 * Unique item count: 5
	 * 
	 * sentence	2
	 * the		1
	 * this		1
	 * repeats	1
	 * word		1
	 * 
	 * 
	 * Example two:
	 * 
	 * Given the input list of 2-gram frequencies
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total 2-gram count: 6
	 * Unique 2-gram count: 5
	 * 
	 * you think	2
	 * how you		1
	 * know how		1
	 * think you	1
	 * you know		1
	 * 
	 * @param frequencies A list of frequencies.
	 */
	public static void printFrequencies(List<Frequency> frequencies) {
		if (frequencies == null) {
			return;
		}
		
		// TODO Write body!
		int uniqueCount = frequencies.size();
		int totalCount = 0;
		
		int n = 0;
		for (Frequency frequency : frequencies) {
			String frequencyText = frequency.getText();
			int frequencyNum = frequency.getFrequency();
			
			if (n == 0) {
				n = frequencyText.split(" ").length;
			}
			
			totalCount += frequencyNum;
		}
		
		if (n == 1) {
			System.out.println("Total item count: " + totalCount);
			System.out.println("Unique item count: " + uniqueCount);
		} else {
			System.out.println("Total " + n + "-gram count: " + totalCount);
			System.out.println("Unique " + n + "-gram count: " + uniqueCount);
		}
		
		for (Frequency frequency : frequencies) {
			String frequencyText = frequency.getText();
			int frequencyNum = frequency.getFrequency();
			System.out.println(frequencyText + "\t" + frequencyNum);
		}
		
	}
}
