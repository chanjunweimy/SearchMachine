package indexer;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Counts the total number of words and their frequencies in a text file.
 */
public final class WordFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private WordFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique word in the original list. The frequency of each word
	 * is equal to the number of times that word occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied words sorted
	 * alphabetically.
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["this", "sentence", "repeats", "the", "word", "sentence"]
	 * 
	 * The output list of frequencies should be 
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of word frequencies, ordered by decreasing frequency.
	 */
	public static List<TermFrequencyPerDocument> computeWordFrequencies(List<String> words) {
		// TODO Write body!
		if (words == null) {
			return null;
		}
		List<TermFrequencyPerDocument> frequencies = new ArrayList<TermFrequencyPerDocument>();
		HashMap <String, Integer> hashEntry = new HashMap <String, Integer>();
		
		for (String word : words) {
			int index = frequencies.size();
			if (!hashEntry.containsKey(word)) {
				Integer indexInteger = new Integer(index);
				hashEntry.put(word, indexInteger);
				TermFrequencyPerDocument frequency = new TermFrequencyPerDocument(word);
				frequency.incrementFrequency();
				frequencies.add(frequency);
			} else {
				index = hashEntry.get(word).intValue();
				TermFrequencyPerDocument frequency = frequencies.get(index);
				frequency.incrementFrequency();
				frequencies.set(index, frequency);
			}
		}
		
		Collections.sort(frequencies, new Utilities.SorterNGrams());
		return frequencies;	
	}
	
	/**
	 * Runs the word frequency counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 */
	public static void main(String[] args) {
		File file = new File(args[0]);
		List<String> words = Utilities.tokenizeFile(file);
		List<TermFrequencyPerDocument> frequencies = computeWordFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
}
