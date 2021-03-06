import java.io.File;
import java.util.Scanner;
public class main extends searchForFiles{
	
	static Scanner scans = new Scanner(System.in);
	static String name;
	
	final static int SLEEP_TIME = 3000;
	
	public static void main(String[] args) {
		name = listPeers();
		
		if (name == "ERROR!") {
		
			System.out.println("Whoops! No unconverted files found.\nRemove the _converted in files to make them convert again.\n\nEXAMPLE:\nT5_124_converted.fmt --> T5_124.fmt");
			wait(SLEEP_TIME * 3);
			
		} else {
			
			File filed = new File(name);
			importFMT importer = new importFMT(filed);
			exportCSV exporter = new exportCSV(importer);
		
			System.out.println("Converting: " + name.replace(".\\", "\\"));
			
			importing(importer);
			//wait(SLEEP_TIME / 6);
			
			System.out.println("Exporting: " + (name.replace(".\\", "\\")).replace(".fmt", ".csv"));
			
			exporting(importer, exporter);
			//wait(SLEEP_TIME / 6);
			
			System.out.println("Cleaning up...\n");
			
			disableOld(filed, name);
			//move(filed, ".txt", "byproduct");
			//move(filed, ".csv", "output");
			
			System.out.println("\nClean complete! Thank you for using FMTtoCSV!\nMay all your radios connect!");
			wait(SLEEP_TIME / 2);
		}
	}
	
	public static void importing(importFMT importerer) {
		importerer.Grab();
	}
	
	public static void exporting(importFMT importerer, exportCSV exporterer) {
		importerer.halt();
		exporterer.steal();
		exporterer.write(name.replace(".fmt", ".txt"));
		exporterer.halt();
	}
	
	public static void move(File assistant, String extension, String folder) {
		File myFile = new File("");
		String myFilePath = myFile.getAbsolutePath();
		String tmp = assistant.getAbsolutePath();
		String toMovePath = tmp.replace("_converted.fmt", extension).replace(".fmt", extension);
		(new File(toMovePath)).delete();
		File toMove = new File(toMovePath);
		(new File(myFilePath + "\\" + folder + "\\" + toMove.getName())).delete();
		File destination = new File(myFilePath + "\\" + folder + "\\" + toMove.getName());
		if (toMove.renameTo(destination)) {
			System.out.println("Moved " + folder + " to: " + destination.getAbsolutePath());
		} else {
			System.out.println("Clogged! Couldn't move " + toMove.getName() + " to " + destination.getAbsolutePath() + " so it has been left in " + assistant.getParent());
			System.out.println("(Destination exists: " + destination.exists() + ". Target exists: " + toMove.exists() + ")");
		}
		//wait(SLEEP_TIME / 5);
	}
	
	public static void wait(int time) {
		try {
			Thread.sleep(time);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public static void disableOld(File inputFile, String inputName) {
		if(inputFile.renameTo(new File(inputName.replace(".fmt", "_converted.fmt")))) {
			System.out.println("Disabled old file!");
		} else {
			System.out.println("Couldn't disable old file. If this is a big problem, move, delete or rename " + inputFile.getName() + ".");
		}
		wait(SLEEP_TIME / 6);
	}
}