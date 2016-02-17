package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * The Vector Space of the Vector Space Model
 * @author Jun
 *
 */
public class VectorSpace {
	/**
	 * The number of documents that we indexed. 
	 * It is used in calculating idf.
	 */
	private int _corpusSize = -1; 
	
	/**
	 * inverted index.
	 * The key is the terms, and they are mapped to the documents that contain them.
	 */
	private HashMap <String, TreeSet<DocumentWeightPair>> _termDocumentMap = null;
	
	/**
	 * construct VectorSpace as a singleton object
	 */
	private static VectorSpace _vectorSpace = null;
	
	private VectorSpace() {
	}
	
	public static VectorSpace getObject() {
		if (_vectorSpace == null) {
			_vectorSpace = new VectorSpace();
		}
		return _vectorSpace;
	}

	
	/**
	 * Train by WeightedTermFrequency
	 * @param indexingFiles
	 * @return
	 */
	public void trainByWtf(File[] indexingFiles) {
		_termDocumentMap = new HashMap <String, TreeSet<DocumentWeightPair>>();

		_corpusSize = indexingFiles.length;
		
		for (File file : indexingFiles) {
			String url = getUrl(file);
			if (url == null) {
				continue;
			}

			List <String> words = Utilities.getObject().tokenizeFileWithoutStopWords(file);
			words.remove(0);
			
			String documentName = file.getAbsolutePath();
			List <TermFrequencyPerDocument> termFrequencies = WordFrequencyCounter.getObject().computeWordFrequencies(words);
			
			DocumentVector documentVector = new DocumentVector(documentName, url, termFrequencies);
			
			for (TermFrequencyPerDocument termFrequency : termFrequencies) {
				setupTermDocumentMapByWTF(documentVector, termFrequency);
			}
		}
	}

	/**
	 * @return
	 */
	private String getUrl(File file) {
		String url = null;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			for (String line; (line = br.readLine()) != null;) {
				url = line;
				break;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		if (url == null) {
			return null;
		}
		url.trim();
		return url;
	}
	
	/**
	 * Trained by norminalized term frequency
	 * @param indexingFiles
	 */
	public void trainByNtf(File[] indexingFiles) {
		_termDocumentMap = new HashMap <String, TreeSet<DocumentWeightPair>>();
		_corpusSize = indexingFiles.length;
		List <DocumentVector> documentVectorSpace = new ArrayList <DocumentVector>();
		TreeSet <String> corpusWords = new TreeSet <String>();
		HashMap <String, Integer> maxTermFrequencyInCorpus = new HashMap <String, Integer>();
		
		for (File file : indexingFiles) {
			String url = getUrl(file);
			if (url == null) {
				continue;
			}

			List <String> words = Utilities.getObject().tokenizeFileWithoutStopWords(file);
			words.remove(0);
			
			String documentName = file.getAbsolutePath();
			List <TermFrequencyPerDocument> termFrequencies = WordFrequencyCounter.getObject().computeWordFrequencies(words);
			DocumentVector documentVector = new DocumentVector(documentName, url, termFrequencies);
			documentVectorSpace.add(documentVector);
			for (TermFrequencyPerDocument termFrequency : termFrequencies) {
				maxTermFrequencyInCorpus = setupMaxFrequency(maxTermFrequencyInCorpus, termFrequency);	
				corpusWords.add(termFrequency.getText());
			}
		}
		
		setupNpWeightInTermDocumentMap(documentVectorSpace, corpusWords,
				maxTermFrequencyInCorpus);
	}
	
	/**
	 * get all documents that contain the term ordered by term frequencies.. (either WTF or NTF)
	 * @param term
	 * @return
	 */
	public TreeSet <DocumentWeightPair> retrieveAllDocuments(String term) {
		Comparator <DocumentWeightPair> comparator = new Utilities.SorterOfDocumentsByWeightedTermFrequency();
		
		TreeSet <DocumentWeightPair> termDocumentWeight = new TreeSet <DocumentWeightPair>(comparator);
		for (DocumentWeightPair pair : _termDocumentMap.get(term)) {
			termDocumentWeight.add(pair);
		}
		return termDocumentWeight;
	}

	/**
	 * get top k documents that contain the term ordered by term frequencies.. (either WTF or NTF)
	 * @param term
	 * @return
	 */
	public TreeSet <DocumentWeightPair> retrieveTopKDocuments(String term, int k) {
		Comparator <DocumentWeightPair> comparator = new Utilities.SorterOfDocumentsByWeightedTermFrequency();

		TreeSet <DocumentWeightPair> termDocumentWeight = new TreeSet <DocumentWeightPair>(comparator);
		if (!_termDocumentMap.containsKey(term)) {
			return termDocumentWeight;
		}
		
		for (DocumentWeightPair pair : _termDocumentMap.get(term)) {
			termDocumentWeight.add(pair);
			if (termDocumentWeight.size() >= k) {
				break;
			}
		}
		return termDocumentWeight;
	}
	
	/**
	 * calculating inverse document frequency
	 * @param term
	 * @return
	 */
	public double calculateIdf(String term) {
		if (_corpusSize == -1) {
			return -1.0;
		}
		
		double idf = (_corpusSize + 0.0) / (_termDocumentMap.get(term).size() + 0.0);
		idf = Math.log(idf);
		return idf;
	}
	
	public void printTermDocumentMap() {
		for (Map.Entry<String, TreeSet<DocumentWeightPair> > termDocuments : _termDocumentMap.entrySet()) {
		    String term = termDocuments.getKey();
		    TreeSet <DocumentWeightPair> pairs = termDocuments.getValue();
		    
		    System.out.print(term + ":");
		    for (DocumentWeightPair pair : pairs) {
		    	System.out.print(pair.documentVector.getUrl() + " " + pair.weight + ",");
		    }
		    System.out.println("");
		}
	}


	/**
	 * @param documentVectorSpace
	 * @param corpusWords
	 * @param maxTermFrequencyInCorpus
	 */
	private void setupNpWeightInTermDocumentMap(
			List<DocumentVector> documentVectorSpace,
			TreeSet<String> corpusWords,
			HashMap<String, Integer> maxTermFrequencyInCorpus) {
		for (DocumentVector documentVector : documentVectorSpace) {
			documentVector.setMaxTermFrequencyInCorpus(maxTermFrequencyInCorpus);
		}
		
		for (String word : corpusWords) {
			Comparator <DocumentWeightPair> ntfComparator = new Utilities.SorterOfDocumentsByWeightedTermFrequency();
			TreeSet <DocumentWeightPair> pairs = new TreeSet <DocumentWeightPair>(ntfComparator);
			for (DocumentVector documentVector : documentVectorSpace) {
				if (documentVector.hasTerm(word)) {
					DocumentWeightPair pair = new DocumentWeightPair();
					pair.documentVector = documentVector;
					pair.weight = documentVector.getNormalizedTermFrequency(word);
					pairs.add(pair);
				}
			}
			_termDocumentMap.put(word, pairs);
		}
	}


	/**
	 * We calculate max frequency map to count the ntf
	 * @param termFrequency
	 */
	private HashMap <String, Integer> setupMaxFrequency(
								   HashMap <String, Integer> maxTermFrequencyInCorpus,
						           TermFrequencyPerDocument termFrequency) {
		String term = termFrequency.getText();
		int frequency = termFrequency.getFrequency();
		if (!maxTermFrequencyInCorpus.containsKey(term)) {
			maxTermFrequencyInCorpus.put(term, frequency);
		} else if (frequency > maxTermFrequencyInCorpus.get(term).intValue()) {
			maxTermFrequencyInCorpus.put(term, frequency);
		}
		return maxTermFrequencyInCorpus;
	}


	/**
	 * @param documentVector
	 * @param term
	 */
	private void setupTermDocumentMapByWTF(DocumentVector documentVector, TermFrequencyPerDocument termFrequency) {
		String term = termFrequency.getText();

		DocumentWeightPair pair = new DocumentWeightPair();
		pair.documentVector = documentVector;
		pair.weight = documentVector.getWeightedTermFrequency(term);
		if (!_termDocumentMap.containsKey(term)) {
			Comparator <DocumentWeightPair> wtfComparator = new Utilities.SorterOfDocumentsByWeightedTermFrequency();
			TreeSet <DocumentWeightPair> pairs = new TreeSet <DocumentWeightPair>(wtfComparator);
			pairs.add(pair);
			_termDocumentMap.put(term, pairs);
		} else {
			_termDocumentMap.get(term).add(pair);
		}
	}
}
