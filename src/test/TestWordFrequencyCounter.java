package test;

import static org.junit.Assert.*;
import indexer.Frequency;
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
		
		List <Frequency> expected = new ArrayList <Frequency>();
		Frequency f1 = new Frequency("sentence", 2);
		Frequency f2 = new Frequency("repeats", 1);
		Frequency f3 = new Frequency("the", 1);
		Frequency f4 = new Frequency("this", 1);
		Frequency f5 = new Frequency("word", 1);
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
		List <Frequency> expected = new ArrayList <Frequency>();
		
		verifyResult(words, expected);
	}
	

	/**
	 * @param words
	 * @param expected
	 */
	private void verifyResult(List<String> words, List<Frequency> expected) {
		List <Frequency> actual = WordFrequencyCounter.computeWordFrequencies(words);
		assertTrue("the number of elements in the list", actual.size() == expected.size());
		for (int i = 0; i < Math.min(expected.size(), actual.size()); i++) {
			assertTrue("comparing the elements..", expected.get(i).toString().equals(actual.get(i).toString()));
		}
	}

}
