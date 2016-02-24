/**
 * @author
 * Amy Yeung
 * Chan Jun Wei
 * Laureen Ma 
 * Matt Levin
 */

package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

/**
 * The Vector Space of the Vector Space Model
 * @author Jun
 *
 */
public class VectorSpace {
	private static final String STRING_DOCUMENT_WEIGHT_SEPARATOR = "|";

	private static final String STRING_TERM_SEPARATOR = ":==:";

	private static final String STRING_TERM_DOCUMENT = "termID -> docID, term frequency";

	private static final String STRING_DOCUMENT_TERM_SEPARATOR = "----------";

	private static final String STRING_DOCUMENT_VECTOR = "document vector:";

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
	private Vector <DocumentVector> _documentVectors = null;
	
	/**
	 * construct VectorSpace as a singleton object
	 */
	private static VectorSpace _vectorSpace = null;
	
	private VectorSpace() {
		_documentVectors = new Vector <DocumentVector>();
		_termDocumentMap = new HashMap <String, TreeSet<DocumentWeightPair>>();
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
			documentVector.setDocId(_documentVectors.size());
			_documentVectors.add(documentVector);
			
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
			documentVector.setDocId(_documentVectors.size());
			_documentVectors.add(documentVector);

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
	
	public void trainByIndexesFile(String filename) {
		boolean isDocumentVector = false;
		boolean isTermDocument = false;
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			for (String line; (line = br.readLine()) != null;) {
				if (line.equals(VectorSpace.STRING_DOCUMENT_VECTOR)) {
					isDocumentVector = true;
				} else if (line.equals(VectorSpace.STRING_DOCUMENT_TERM_SEPARATOR)) {
					isDocumentVector = false;
				} else if (line.equals(VectorSpace.STRING_TERM_DOCUMENT)) {
					isTermDocument = true;
				}
				
				if (isDocumentVector) {
					DocumentVector documentVector = DocumentVector.readDocumentVectorLine(line);
					_documentVectors.add(documentVector);
				} else if (isTermDocument) {
					addToTermDocumentMap(line);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * @param line
	 */
	private void addToTermDocumentMap(String line) {
		String[] tokens = line.split(STRING_TERM_SEPARATOR);
		String term = tokens[0];
		TreeSet<DocumentWeightPair> documentWeightPairs = new TreeSet<DocumentWeightPair>();
		String[] documentWeights = tokens[1].split(STRING_DOCUMENT_WEIGHT_SEPARATOR);
		for (String documentWeight : documentWeights) {
			documentWeight = documentWeight.trim();
			if (documentWeight.isEmpty()) {
				continue;
			}
			String[] pair = documentWeight.split(" ");
			int docId = Integer.parseInt(pair[0]);
			DocumentVector documentVector = _documentVectors.get(docId);
			double weight = Double.parseDouble(pair[1]);
			
			DocumentWeightPair dwp = new DocumentWeightPair();
			dwp.documentVector = documentVector;
			dwp.weight = weight;
			documentWeightPairs.add(dwp);
		}
		_termDocumentMap.put(term, documentWeightPairs);
	}
	
	public void saveTermDocumentMap(String filename) {
		System.out.print("number of documents: ");
		System.out.println(_documentVectors.size());
		System.out.print("number of [unique] words (without stop words): ");
		System.out.println(_termDocumentMap.size());
		
		writeToFile(filename, false, "");

		try (FileWriter fw = new FileWriter(filename, true)) {
			fw.write(VectorSpace.STRING_DOCUMENT_VECTOR + System.lineSeparator());
			for (DocumentVector documentVector : _documentVectors) {
				fw.write(documentVector.toString() + System.lineSeparator());
			}

			fw.write(VectorSpace.STRING_DOCUMENT_TERM_SEPARATOR + System.lineSeparator());
			fw.write(VectorSpace.STRING_TERM_DOCUMENT + System.lineSeparator());
			for (Map.Entry<String, TreeSet<DocumentWeightPair> > termDocuments : _termDocumentMap.entrySet()) {
			    String term = termDocuments.getKey();
			    TreeSet <DocumentWeightPair> pairs = termDocuments.getValue();
			    
			    fw.write(term + VectorSpace.STRING_TERM_SEPARATOR + VectorSpace.STRING_DOCUMENT_WEIGHT_SEPARATOR);
			    for (DocumentWeightPair pair : pairs) {
			    	fw.write(pair.documentVector.getDocId() + " " +
			    			 pair.weight + 
			    			 VectorSpace.STRING_DOCUMENT_WEIGHT_SEPARATOR);
			    }
			    
			    fw.write(System.lineSeparator());
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	/**
	 * helper method to write content to file
	 * 
	 * @param filename
	 *            the file you want to write into
	 * @param isAppend
	 *            whether to overwrite the file
	 * @param line
	 *            the content that you want to write into
	 * @return
	 */
	private boolean writeToFile(String filename, boolean isAppend,
			String line) {
		try (FileWriter fw = new FileWriter(filename, isAppend)) {
			fw.write(line);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
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
