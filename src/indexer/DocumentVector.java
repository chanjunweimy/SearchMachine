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
	
	
	
	public DocumentVector(String documentName, 
						  String url,
						  List <TermFrequencyPerDocument> termFrequencies) {
		initializeDocumentVector(documentName, url, termFrequencies);
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
