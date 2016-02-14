package indexer;

import java.io.File;

public class IndexerMain {
	//private static final String DIR_DATA = "data/";
	private static final String DIR_TEST_NORMAL = "test_normal/";
	
	private VectorSpace _vectorSpace = null;
	
	public IndexerMain() {
		_vectorSpace = VectorSpace.getObject();
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
	
	public void printIndexes() {
		_vectorSpace.printTermDocumentMap();
	}
	
	public static void main(String[] args) {
		IndexerMain indexer = new IndexerMain();
		indexer.trainIndexerByWtf(DIR_TEST_NORMAL);
		indexer.printIndexes();
	}
}
