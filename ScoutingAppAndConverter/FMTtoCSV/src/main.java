import java.io.File;
import java.util.Scanner;

public class main {
	
	static Scanner scans = new Scanner(System.in);
	static String name;
	
	public static void main(String[] args) {
		System.out.println("Enter the file you want to convert here: ");
		name = scans.nextLine();
		File filed = new File(name);
		importFMT importer = new importFMT(filed);
		exportCSV exporter = new exportCSV(importer);
		
		System.out.println("Converting: " + importer.file);
		importing(importer);
		System.out.println("Exporting: " + importer.file.getName().replace(".fmt", ".csv"));
		exporting(importer, exporter);
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
}
