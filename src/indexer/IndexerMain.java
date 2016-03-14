/**
 * @author
 * Amy Yeung
 * Chan Jun Wei
 * Laureen Ma 
 * Matt Levin
 */

package indexer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;

public class IndexerMain {
	private static final String DIR_DATA = "data/";
	private static final String DIR_TEST_NORMAL = "test_normal/";
	private static final int K = 5;
	
	private VectorSpace _vectorSpace = null;
	
	public IndexerMain() {
		_vectorSpace = VectorSpace.getObject();
	}
	
	public void trainByIndexesFile(String filename) {
		_vectorSpace.trainByIndexesFile(filename);
	}
	
	public void trainIndexerByWtf(String dataDirectory) {
		File dataDirectoryFile = new File(dataDirectory);
		trainIndexerByWtf(dataDirectoryFile);
	}
	
	public void trainIndexerByWtf(File dataDirectoryFile) {
		File[] files = dataDirectoryFile.listFiles();
		_vectorSpace.trainByWtf(files);
	}
	
	public void trainIndexerByNtf(String dataDirectory) {
		File dataDirectoryFile = new File(dataDirectory);
		trainIndexerByNtf(dataDirectoryFile);
	}
	
	public void trainIndexerByNtf(File dataDirectoryFile) {
		File[] files = dataDirectoryFile.listFiles();
		_vectorSpace.trainByNtf(files);
	}
	
	public ArrayList <DocumentWeightPair> retrieveTopKDocuments(String query, int k) {
		query = query.replaceAll("[^A-Za-z0-9 ]", "");
		query = query.toLowerCase();
		String[] tokens = query.split(" ");
		
		ArrayList <DocumentWeightPair> documents = new ArrayList <DocumentWeightPair>();
		HashMap <String, Integer> documentIndex = new HashMap <String, Integer>();
		
		for (String token : tokens) {
			if (token.isEmpty()) {
				continue;
			}
			TreeSet <DocumentWeightPair> termDocumentWeight = _vectorSpace.retrieveTopKDocuments(token, k);
			for (DocumentWeightPair dwp : termDocumentWeight) {
				if (documentIndex.containsKey(dwp.documentVector.getUrl())) {
					int index = documentIndex.get(dwp.documentVector.getUrl()).intValue();
					documents.get(index).weight += dwp.weight;
				} else {
					documentIndex.put(dwp.documentVector.getUrl(), documents.size());
					documents.add(dwp);
				}
			}
			
		}
		
		Comparator <DocumentWeightPair> comparator = new Utilities.SorterOfDocumentsByWeightedTermFrequency();
		Collections.sort(documents, comparator);
		
		ArrayList <DocumentWeightPair> ans = new ArrayList <DocumentWeightPair>();
		for (int i = 0; i < Math.min(k, documents.size()); i++) {
			ans.add(documents.get(i));
		}
		return ans;
	}
	
	public void saveIndexes(String filename) {
		_vectorSpace.saveTermDocumentMap(filename);
	}
	
	public void printIndexes() {
		_vectorSpace.printTermDocumentMap();
	}
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		IndexerMain indexer = new IndexerMain();
		indexer.trainIndexerByWtf(DIR_DATA);
		//indexer.printIndexes();
		
		indexer.saveIndexes("indexes.txt");
		
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;		
		System.out.println("total time taken: " + totalTime);
		
		//startCLInterface(indexer);
		
	}

	/**
	 * @param indexer
	 */
	private static void startCLInterface(IndexerMain indexer) {
		try (Scanner sc = new Scanner(System.in)) {
			while (sc.hasNextLine()) {
				String query = sc.nextLine();
				ArrayList <DocumentWeightPair> ans = indexer.retrieveTopKDocuments(query, K);
				System.out.println(ans.size());
				for (DocumentWeightPair dwp : ans) {
					System.out.println(dwp.documentVector.getUrl());
				}
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
