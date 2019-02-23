import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class importFMT {
	
	File file;
	BufferedReader reads;
	String line;

	
	public importFMT(File file) {
			this.file = file;
	}
	public void Grab() {
		try {
			reads = new BufferedReader(new FileReader(file));
			line = reads.readLine();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void halt() {
		try {
			reads.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
