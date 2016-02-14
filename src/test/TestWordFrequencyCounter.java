package test;

import static org.junit.Assert.*;
import indexer.TermFrequencyPerDocument;
import indexer.WordFrequencyCounter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestWordFrequencyCounter {


	@Test
	public void testProvided() {
		List <String> words = new ArrayList <String>();
		words.add("this");
		words.add("sentence");
		words.add("repeats");
		words.add("the");
		words.add("word");
		words.add("sentence");
		
		List <TermFrequencyPerDocument> expected = new ArrayList <TermFrequencyPerDocument>();
		TermFrequencyPerDocument f1 = new TermFrequencyPerDocument("sentence", 2);
		TermFrequencyPerDocument f2 = new TermFrequencyPerDocument("repeats", 1);
		TermFrequencyPerDocument f3 = new TermFrequencyPerDocument("the", 1);
		TermFrequencyPerDocument f4 = new TermFrequencyPerDocument("this", 1);
		TermFrequencyPerDocument f5 = new TermFrequencyPerDocument("word", 1);
		expected.add(f1);
		expected.add(f2);
		expected.add(f3);
		expected.add(f4);
		expected.add(f5);
		
		verifyResult(words, expected);
	}
	
	@Test
	public void testEmpty() {
		List <String> words = new ArrayList <String>();	
		List <TermFrequencyPerDocument> expected = new ArrayList <TermFrequencyPerDocument>();
		
		verifyResult(words, expected);
	}
	

	/**
	 * @param words
	 * @param expected
	 */
	private void verifyResult(List<String> words, List<TermFrequencyPerDocument> expected) {
		List <TermFrequencyPerDocument> actual = WordFrequencyCounter.getObject().computeWordFrequencies(words);
		assertTrue("the number of elements in the list", actual.size() == expected.size());
		for (int i = 0; i < Math.min(expected.size(), actual.size()); i++) {
			assertTrue("comparing the elements..", expected.get(i).toString().equals(actual.get(i).toString()));
		}
	}

}
