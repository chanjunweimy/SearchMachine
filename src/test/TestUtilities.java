/**
 * @author
 * Amy Yeung
 * Chan Jun Wei
 * Laureen Ma 
 * Matt Levin
 */

package test;

import static org.junit.Assert.*;
import indexer.TermFrequencyPerDocument;
import indexer.Utilities;
import indexer.WordFrequencyCounter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestUtilities {
	private final ByteArrayOutputStream _outContent = new ByteArrayOutputStream();
	private final String DIR_TEST = "test/";

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(_outContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}
	
	@Test
	public void testFileExtremeNull() {
		File file = null;
		
		ArrayList <String> words = Utilities.getObject().tokenizeFile(file);
		assertTrue("the list should be null as the file is null", words == null);
		
		List<TermFrequencyPerDocument> frequencies = WordFrequencyCounter.getObject().computeWordFrequencies(words);
		assertTrue("should have null Frequencies as the file is null", frequencies == null);
		
		
		
		Utilities.getObject().printFrequencies(frequencies);		
		String expectedOutput = "";
		assertTrue("the output is correct", expectedOutput.equals(_outContent.toString()));
		
	}

	@Test
	public void testFileExtremeEmpty() {
		final String filename = DIR_TEST + "file_extreme_empty.txt";
		File file = new File(filename);
		assertTrue ("check if the file is in the directory", file.exists() && file.isFile());
		
		ArrayList <String> words = Utilities.getObject().tokenizeFile(file);
		ArrayList <String> expectedArrayList = new ArrayList <String>();
		assertTrue("number of words is correct", words.size() == expectedArrayList.size());
		for (int i = 0; i < words.size(); i++) {
			assertTrue("word extracted from file is same with expected", words.get(i).equals(expectedArrayList.get(i)));
		}
		
		List<TermFrequencyPerDocument> frequencies = WordFrequencyCounter.getObject().computeWordFrequencies(words);
		Utilities.getObject().printFrequencies(frequencies);		
		String expectedOutput = "Total 0-gram count: 0" + System.lineSeparator() + "Unique 0-gram count: 0" + System.lineSeparator();
		assertTrue("the output is correct", expectedOutput.equals(_outContent.toString()));
		
	}
	
	@Test
	public void testFileNormalMultiline() {
		final String filename = DIR_TEST + "file_normal_multilines.txt";
		File file = new File(filename);
		assertTrue ("check if the file is in the directory", file.exists() && file.isFile());
		
		ArrayList <String> words = Utilities.getObject().tokenizeFile(file);
		ArrayList <String> expectedArrayList = new ArrayList <String>();
		expectedArrayList.add("an");
		expectedArrayList.add("input");
		expectedArrayList.add("string");
		expectedArrayList.add("this");
		expectedArrayList.add("is");
		expectedArrayList.add("or");
		expectedArrayList.add("is");
		expectedArrayList.add("it");
		
		expectedArrayList.add("this");
		expectedArrayList.add("sentence");
		expectedArrayList.add("repeats");
		expectedArrayList.add("the");
		expectedArrayList.add("word");
		expectedArrayList.add("sentence");
		
		expectedArrayList.add("you");
		expectedArrayList.add("think");
		expectedArrayList.add("you");
		expectedArrayList.add("know");
		expectedArrayList.add("how");
		expectedArrayList.add("you");
		expectedArrayList.add("think");
		
		expectedArrayList.add("do");
		expectedArrayList.add("geese");
		expectedArrayList.add("see");
		expectedArrayList.add("god");
		expectedArrayList.add("abba");
		expectedArrayList.add("bat");
		expectedArrayList.add("tab");
		
		assertTrue("number of words is correct", words.size() == expectedArrayList.size());
		for (int i = 0; i < words.size(); i++) {
			assertTrue("word extracted from file is same with expected", words.get(i).equals(expectedArrayList.get(i)));
		}
		
		List<TermFrequencyPerDocument> frequencies = WordFrequencyCounter.getObject().computeWordFrequencies(words);
		Utilities.getObject().printFrequencies(frequencies);		
		String expectedOutput = "Total item count: 28" + System.lineSeparator() + "Unique item count: 22" + System.lineSeparator()
				+ "you\t3" + System.lineSeparator() 
				+ "is\t2" + System.lineSeparator()
				+ "sentence\t2" + System.lineSeparator()
				+ "think\t2" + System.lineSeparator()
				+ "this\t2" + System.lineSeparator()
				+ "abba\t1" + System.lineSeparator()
				+ "an\t1" + System.lineSeparator()
				+ "bat\t1" + System.lineSeparator()
				+ "do\t1" + System.lineSeparator()
				+ "geese\t1" + System.lineSeparator()
				+ "god\t1" + System.lineSeparator()
				+ "how\t1" + System.lineSeparator()
				+ "input\t1" + System.lineSeparator()
				+ "it\t1" + System.lineSeparator()
				+ "know\t1" + System.lineSeparator()
				+ "or\t1" + System.lineSeparator()
				+ "repeats\t1" + System.lineSeparator()
				+ "see\t1" + System.lineSeparator()
				+ "string\t1" + System.lineSeparator()
				+ "tab\t1" + System.lineSeparator()
				+ "the\t1" + System.lineSeparator()
				+ "word\t1" + System.lineSeparator();

		assertTrue("the output is correct", expectedOutput.equals(_outContent.toString()));

		_outContent.reset();
		Collections.sort(frequencies, new Utilities.SorterNGrams());
		Utilities.getObject().printFrequencies(frequencies);
		assertTrue("the output is correct", expectedOutput.equals(_outContent.toString()));
	}

	@Test
	public void testFileNormalSingleLine4() {
		final String filename = DIR_TEST + "file_normal_singleline4.txt";
		File file = new File(filename);
		assertTrue ("check if the file is in the directory", file.exists() && file.isFile());
		
		ArrayList <String> words = Utilities.getObject().tokenizeFile(file);
		ArrayList <String> expectedArrayList = new ArrayList <String>();
		expectedArrayList.add("do");
		expectedArrayList.add("geese");
		expectedArrayList.add("see");
		expectedArrayList.add("god");
		expectedArrayList.add("abba");
		expectedArrayList.add("bat");
		expectedArrayList.add("tab");
		
		assertTrue("number of words is correct", words.size() == expectedArrayList.size());
		for (int i = 0; i < words.size(); i++) {
			assertTrue("word extracted from file is same with expected", words.get(i).equals(expectedArrayList.get(i)));
		}
		
		List<TermFrequencyPerDocument> frequencies = WordFrequencyCounter.getObject().computeWordFrequencies(words);
		Utilities.getObject().printFrequencies(frequencies);		
		String expectedOutput = "Total item count: 7" + System.lineSeparator()
				+ "Unique item count: 7" + System.lineSeparator()
				+ "abba\t1" + System.lineSeparator()
				+ "bat\t1" + System.lineSeparator()
				+ "do\t1" + System.lineSeparator()
				+ "geese\t1" + System.lineSeparator()
				+ "god\t1" + System.lineSeparator()
				+ "see\t1" + System.lineSeparator()
				+ "tab\t1" + System.lineSeparator();
				
		assertTrue("the output is correct", expectedOutput.equals(_outContent.toString()));

		_outContent.reset();
		
	}
}
