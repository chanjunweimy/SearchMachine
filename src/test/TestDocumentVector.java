/**
 * @author
 * Amy Yeung
 * Chan Jun Wei
 * Laureen Ma 
 * Matt Levin
 */

package test;

import static org.junit.Assert.*;
import java.util.*;

import java.util.HashMap;
import indexer.DocumentVector;
import indexer.TermFrequencyPerDocument;
import org.junit.Test;

public class TestDocumentVector {

	@Test
	public void testWTF() {
		
		HashMap <String, Integer> termFreqMap = new HashMap<String,Integer>();
		termFreqMap.put("hello", 3);
		termFreqMap.put("test", 10);
		termFreqMap.put("how", 1);
		List<TermFrequencyPerDocument> termFreq = new ArrayList<TermFrequencyPerDocument>();
		termFreq.add(new TermFrequencyPerDocument("hello",3));
		termFreq.add(new TermFrequencyPerDocument("test",10));
		termFreq.add(new TermFrequencyPerDocument("how",1));
		DocumentVector DV = new DocumentVector("test","www.test.com", termFreq);
		assertTrue("in hashMap",termFreqMap.containsKey("hello"));
		assertTrue("Item not in hashMap thus have 0 WTF",DV.getWeightedTermFrequency("hi") == 0.0);
		assertTrue("Item in hashMap, thus has WTF", DV.getWeightedTermFrequency("hello") == (1.0+Math.log(3.0)));
	}
	
	@Test
	public void testhasTerm(){
		HashMap <String, Integer> termFreqMap = new HashMap<String,Integer>();
		termFreqMap.put("hello", 3);
		termFreqMap.put("test", 10);
		termFreqMap.put("how", 1);
		List<TermFrequencyPerDocument> termFreq = new ArrayList<TermFrequencyPerDocument>();
		termFreq.add(new TermFrequencyPerDocument("hello",3));
		termFreq.add(new TermFrequencyPerDocument("test",10));
		termFreq.add(new TermFrequencyPerDocument("how",1));
		DocumentVector DV = new DocumentVector("test","www.test.com", termFreq);
		assertTrue("item not in hashmap",DV.hasTerm("hello")== true);
		assertTrue("item not supposed to be in hashmap", DV.hasTerm("howdy") == false);
	}
	
	@Test
	public void testNTF(){
		HashMap <String, Integer> termFreqMap = new HashMap<String,Integer>();
		termFreqMap.put("hello", 3);
		termFreqMap.put("test", 10);
		termFreqMap.put("how", 1);
		List<TermFrequencyPerDocument> termFreq = new ArrayList<TermFrequencyPerDocument>();
		termFreq.add(new TermFrequencyPerDocument("hello",3));
		termFreq.add(new TermFrequencyPerDocument("test",10));
		termFreq.add(new TermFrequencyPerDocument("how",1));
		DocumentVector DV = new DocumentVector("test","www.test.com", termFreq);
		HashMap <String, Integer> corpFreqMap = new HashMap<String,Integer>();
		DV.setMaxTermFrequencyInCorpus(null);
		assertTrue("null corpus",DV.getNormalizedTermFrequency("no")==-1.0);
		corpFreqMap.put("hello", 15);
		corpFreqMap.put("test", 100);
		corpFreqMap.put("how", 5);
		DV.setMaxTermFrequencyInCorpus(corpFreqMap);
		assertTrue("NTF value",DV.getNormalizedTermFrequency("hello")==.52);
	}
	@Test
	public void testgetMethods(){
		HashMap <String, Integer> termFreqMap = new HashMap<String,Integer>();
		termFreqMap.put("hello", 3);
		termFreqMap.put("test", 10);
		termFreqMap.put("how", 1);
		List<TermFrequencyPerDocument> termFreq = new ArrayList<TermFrequencyPerDocument>();
		termFreq.add(new TermFrequencyPerDocument("hello",3));
		termFreq.add(new TermFrequencyPerDocument("test",10));
		termFreq.add(new TermFrequencyPerDocument("how",1));
		DocumentVector DV = new DocumentVector("test","www.test.com", termFreq);
		assertTrue("correct name",DV.getDocumentName()=="test");
		assertTrue("correct linK",DV.getUrl() == "www.test.com");
	}

}
