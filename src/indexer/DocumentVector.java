/**
 * @author
 * Amy Yeung
 * Chan Jun Wei
 * Laureen Ma 
 * Matt Levin
 */

package indexer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Document Vector in the Vector Space Model.
 * @author Jun
 *
 */
public class DocumentVector {
	public static final String SEPARATOR = "::";

	/**
	 * The smoothing term used in ntf
	 */
	private static final double ALPHA = 0.4;
	
	/**
	 * Using a hashmap to save term frequency to increase efficiency
	 */
	private HashMap <String, Integer> _termFrequencyMap = null;
	
	private String _documentName = null;
	private String _url = null;
	private int _docId = 0;
	
	private HashMap <String, Integer> _maxTermFrequencyInCorpus = null;
	
	
	public static DocumentVector readDocumentVectorLine(String line) {
		if (line == null || line.isEmpty()) {
			return null;
		}
		
		line = line.trim();
		String[] tokens = line.split(SEPARATOR);
		
		if (tokens == null || tokens.length < 3) {
			return null;
		}
		
		int docId = Integer.parseInt(tokens[0]);
		String documentName = tokens[1];
		String url = tokens[2];
		HashMap <String, Integer> termFrequencyMap = new HashMap <String, Integer>();
		for (int i = 3; i < tokens.length; i++) {
			String[] termFreq = tokens[i].split(" ");
			if (termFreq == null || termFreq.length != 2) {
				continue;
			}
			String term = termFreq[0];
			int freq = Integer.parseInt(termFreq[1]);
			termFrequencyMap.put(term, freq);
		}
		return new DocumentVector(docId, documentName, url, termFrequencyMap);
	}
	
	public DocumentVector(String documentName, 
						  String url,
						  List <TermFrequencyPerDocument> termFrequencies) {
		initializeDocumentVector(documentName, url, termFrequencies);
	}
	
	private DocumentVector(int docId,
			  	           String documentName, 
			  			   String url,
			  		       HashMap <String, Integer> termFrequencyMap) {
		setDocId(docId);
		setDocumentName(documentName);
		setUrl(url);
		setTermFrequencyMap(termFrequencyMap);
		
	}
	
	private void setTermFrequencyMap(HashMap<String, Integer> termFrequencyMap) {
		_termFrequencyMap = termFrequencyMap;
	}

	private void setUrl(String url) {
		_url = url;
	}

	private void setDocumentName(String documentName) {
		_documentName = documentName;		
	}

	public void setMaxTermFrequencyInCorpus(HashMap <String, Integer> maxTermFrequencyInCorpus) {
		_maxTermFrequencyInCorpus = maxTermFrequencyInCorpus;
	}
	
	public boolean hasTerm(String term) {
		return _termFrequencyMap.containsKey(term);
	}

	/**
	 * TF normalization implementation in Java
	 * @param term
	 * @return
	 */
	public double getNormalizedTermFrequency(String term) {
		if (_maxTermFrequencyInCorpus == null) {
			return -1.0;
		} 
		
		int tf = _termFrequencyMap.get(term).intValue();
		int maxTf = _maxTermFrequencyInCorpus.get(term).intValue();
		
		double ntf = (tf + 0.0) / (maxTf + 0.0);
		ntf = ntf * (1 - ALPHA);
		ntf += ALPHA;
		return ntf;
	}
		
	/**
	 * WTF implementation in Java
	 * @param term
	 * @return
	 */
	public double getWeightedTermFrequency(String term) {
		double ans = 0.0;
		if (!_termFrequencyMap.containsKey(term)) {
			return ans;
		}
		
		int tf = _termFrequencyMap.get(term).intValue();
		ans = 1.0 + Math.log(tf + 0.0);
		return ans;
	}
	
	public String getDocumentName() {
		return _documentName;
	}
	
	public String getUrl() {
		return _url;
	}
	
	/**
	 * @param documentName
	 * @param termFrequencies
	 */
	private void initializeDocumentVector(String documentName,
			String url,
			List<TermFrequencyPerDocument> termFrequencies) {
		_documentName = documentName;
		_url = url;
		initializeTermFrequencyMap(termFrequencies);
	}

	/**
	 * @param termFrequencies
	 */
	private void initializeTermFrequencyMap(
			List<TermFrequencyPerDocument> termFrequencies) {
		_termFrequencyMap = new HashMap<String, Integer>();
		for (TermFrequencyPerDocument tf : termFrequencies) {
			_termFrequencyMap.put(tf.getText(), tf.getFrequency());
		}
	}

	public int getDocId() {
		return _docId;
	}

	public void setDocId(int _docId) {
		this._docId = _docId;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(_docId);
		buffer.append(SEPARATOR);
		buffer.append(_documentName);
		buffer.append(SEPARATOR);
		buffer.append(_url);
		for (Map.Entry<String, Integer> termFrequency : _termFrequencyMap.entrySet()) {
			buffer.append(SEPARATOR);
			buffer.append(termFrequency.getKey());
			buffer.append(" ");
			buffer.append(termFrequency.getValue().intValue());
		}
		return buffer.toString();
	}

}
