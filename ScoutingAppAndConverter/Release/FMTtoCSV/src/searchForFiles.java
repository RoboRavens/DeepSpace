import java.io.File;

public class searchForFiles {
	
	public static File findDataInParent() {
		File tmp1 = new File("");
		File tmp2 = new File(tmp1.getAbsolutePath().replace("FMTtoCSV", "data"));
		return(tmp2);
	}
	
	public static File findQueue() {
		File tmp = new File("queue");
		return(tmp);
	}
	
	public static String listPeers() {
		
		File FMTtoCSV = findDataInParent();
		File[] listOfFiles = FMTtoCSV.listFiles();
		File[] listOfFilesInFile;
		String output = "ERROR!";
		//Run and see the problem
		System.out.println("Searching through [ " + FMTtoCSV.getAbsolutePath() + " ] for convertable files!");
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				
				listOfFilesInFile = listOfFiles[i].listFiles();

				for (int ii = 0; ii < listOfFilesInFile.length; ii++) {
					if (listOfFilesInFile[ii].isFile() && (listOfFilesInFile[ii].getName()).contains(".fmt") && !(listOfFilesInFile[ii].getName()).contains("_converted.fmt")) {
						System.out.println("Found a file (Inside folder " + listOfFiles[i] + " in queue): " + listOfFilesInFile[ii].getName());
						output = listOfFilesInFile[ii].getPath();
					}
				}
			}
		}
		
		return(output);
	}
}