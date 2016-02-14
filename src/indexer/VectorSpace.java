package indexer;

import java.io.File;
import java.util.HashMap;

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
	private int _corpusSize; 
	
	/**
	 * inverted index.
	 * The key is the terms, and they are mapped to the documents that contain them.
	 */
	private HashMap <String, DocumentVector> _termDocumentMap = null;
	
	private HashMap <String, Integer> _maxTermFrequencyInCorpus = null;
	
	/**
	 * file used to store stop words
	 */
	private final static String FILE_STOPWORDS = "StopWords.txt";
	
	public VectorSpace (File[] indexingFiles) {
		
	}
}
