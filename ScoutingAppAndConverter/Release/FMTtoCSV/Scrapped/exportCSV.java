import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

public class exportCSV {
	
	BufferedWriter writes;
	BufferedWriter writinger;
	BufferedReader reads;
	importFMT grabber;
	String line;
	String tmp;
	
	//These are specially for timesUsed()
	boolean habLvlOne = false;
	boolean habLvlTwo = false;
	
	int cargoesHeld = 0;
	int cargoesDropped = 0;
	
	int hatchesHeld = 0;
	int hatchesDropped = 0;
	
	int shipsScored = 0;
	int shipsFailed = 0;
	
	int rocketOneScored = 0;
	int rocketOneFailed = 0;
	
	int rocketTwoScored = 0;
	int rocketTwoFailed = 0;
	
	int rocketThreeScored = 0;
	int rocketThreeFailed = 0;
	
	int habClimbTier;
	int defenses = 0;
	int crosses = 0;
	
	public exportCSV(importFMT grabber) {
		this.grabber = grabber;
	}
	
	public void steal() {
		line = grabber.line;
	}
	public void write(String fileName) {
		//The input is read as fileName.txt
		try {
			writes = new BufferedWriter(new FileWriter(new File(fileName)));
			tmp = line.replace(":", "").replace(",", ": ").replace(";", "\n");
			writes.write(tmp);
			writes.close();
			reads = new BufferedReader(new FileReader(new File(fileName)));
			writinger = new BufferedWriter(new FileWriter(new File(fileName.replace(".txt", ".csv"))));
			for(int i = 0;i > 5; i++) {
				writinger.write(reads.readLine());
			}
			while(reads.ready()) {
				timesUsed(reads.readLine());
			}
			writinger.write(eventTimesToString());
			writinger.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void halt() {
		try {
			writes.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
/*	
*	Old code that doesn't work right. Saved for glossary-like use
*
*	public String fix(String line) {
*		return line.replace("cargoCount", "11881188").replace("hatchCount", "1818")
*		.replace("a", "Scored on Hab side 1 at").replace("b", "Scored on Hab side 2 at").replace("c", "Cargo has gone up at")
*		.replace("d", "Cargo dropped at").replace("e", "Hatch went up at").replace("f", "Hatch was dropped at")
*		.replace("g", "Ship scored sucessfully at").replace("h", "Ship score attempt made, they failed, at")
*		.replace("i", "Rocket one was scored on sucessfully at").replace("j", "Rocket one was attempted on at")
*		.replace("k", "Rocket 2 was scored on").replace("l", "Rocket 2 was attempted on at").replace("m", "Rocket 3 was scored on at")
*		.replace("n", "Rocket 3 was attempted at").replace("o", "Hab one was climbed at").replace("p", "Hab 2 was climbed at")
*		.replace("q", "Hab 3 was climbed at").replace("r", "A hab was attempted, though failed, at").replace("s", "A robot defended at")
*		.replace("t", "A robot crossed the feild at").replace("11881188", "Cargo Count").replace("1818", "Hatch Count");
*	}
*
*	
*	public String betterFix(String line) {
*		if (line.contains("cargoCount")) {
*			
*			return(line.replace("cargoCount", "Cargo Count"));
*			
*		} else if (line.contains("hatchCount")) {
*			
*			return(line.replace("hatchCount", "Hatch Count"));
*			
*		} else if (line.contains("a")) {
*			
*			return(line.replace("a", "Scored on hab side 1"));
*			
*			
*		} else if (line.contains("b")) {
*			
*			return(line.replace("b", "Scored on hab side 2"));
*			
*		} else if (line.contains("c")) {
*			
*			return(line.replace("c", "Cargo has gone up at"));
*			
*		} else if (line.contains("d")) {
*			
*			return(line.replace("d", "Cargo dropped at"));
*			
*		} else if (line.contains("e")) {
*			
*			return(line.replace("e", "Hatch went up at"));
*			
*		} else if (line.contains("f")) {
*			
*			return(line.replace("f", "Hatch was dropped at"));
*			
*		} else if (line.contains("g")) {
*			
*			return(line.replace("g", "Ship scored succesfully at"));
*			
*		} else if (line.contains("h")) {
*			
*			return(line.replace("h", "Ship scoring attempt made and failed at"));
*			
*		} else if (line.contains("i")) {
*			
*			return(line.replace("i", "Rocket one was scored successfully at"));
*			
*		} else if (line.contains("j")) {
*			
*			return(line.replace("j", "Rocket one was attempted on at"));
*			
*		} else if (line.contains("k")) {
*			
*			return(line.replace("k", "Rocket two was scored successfully at"));
*		
*		} else if (line.contains("l")) {
*			
*			return(line.replace("l", "Rocket two was attempted on at"));
*			
*		} else if (line.contains("m")) {
*			
*			return(line.replace("m", "Rocket three was scored successfully at"));
*			
*		} else if (line.contains("n")) {
*			
*			return(line.replace("n", "Rocket three was attempted on at"));
*			
*		} else if (line.contains("o")) {
*			
*			return(line.replace("o", "Hab one was climbed at"));
*			
*		} else if (line.contains("p")) {
*			
*			return(line.replace("p", "Hab two was climbed at"));
*			
*		} else if (line.contains("q")) {
*			
*			return(line.replace("q", "Hab three was climbed at"));
*			
*		} else if (line.contains("r")) {
*			
*			return(line.replace("r", "A hab climb was failed at"));
*			
*		} else if (line.contains("s")) {
*			
*			return(line.replace("s", "Robot defended at"));
*			
*		} else if (line.contains("t")) {
*			
*			return(line.replace("t", "Robot crossed the field at"));
*			
*		} else {
*			
*			return(line);
*			
*		}
*/		
		
	public void timesUsed(String line) {
		if (line.contains("a")) {
		
			habLvlOne = true;
				
		} else if (line.contains("b")) {
				
			habLvlTwo = true;
				
		} else if (line.contains("c")) {
				
			cargoesHeld++;
				
		} else if (line.contains("d")) {
				
			cargoesDropped++;
				
		} else if (line.contains("e")) {
				
			hatchesHeld++;
			
		} else if (line.contains("f")) {
				
			hatchesDropped++;
				
		} else if (line.contains("g")) {
				
			shipsScored++;
				
		} else if (line.contains("h")) {
				
			shipsFailed++;
				
		} else if (line.contains("i")) {
				
			rocketOneScored++;
				
		} else if (line.contains("j")) {
				
			rocketOneFailed++;
				
		} else if (line.contains("k")) {
				
			rocketTwoScored++;
				
		} else if (line.contains("l")) {
				
			rocketTwoFailed++;
				
		} else if (line.contains("m")) {
				
			rocketThreeScored++;
				
		} else if (line.contains("n")) {
				
			rocketThreeFailed++;
				
		} else if (line.contains("o")) {
				
			habClimbTier = 1;
				
		} else if (line.contains("p")) {
				
			habClimbTier = 2;
				
		} else if (line.contains("q")) {
				
			habClimbTier = 3;
				
		} else if (line.contains("r")) {
				
			habClimbTier = 0;
				
		} else if (line.contains("s")) {
				
			defenses++;
				
		} else if (line.contains("t")) {
				
			crosses++;
				
		} else {
				
			System.out.println("Ew! What is this garbage? Check input file for typos.");
				
		}
	}
	
	private String boolToString(boolean value) {
		return(value ? "true" : "false");
	}
	
	private String eventTimesToString() {
		String outputString;
		
		outputString =                encapsulateEvent("Robot crossed Hab Line One", boolToString(habLvlOne));
		outputString = outputString + encapsulateEvent("Robot crossed Hab Line Two", boolToString(habLvlTwo));
		outputString = outputString + encapsulateEvent("Amount of cargo lifted", cargoesHeld);
		outputString = outputString + encapsulateEvent("Amount of cargo dropped", cargoesDropped);
		outputString = outputString + encapsulateEvent("Amount of hatches lifted", hatchesHeld);
		outputString = outputString + encapsulateEvent("Amount of hatches dropped", hatchesDropped);
		outputString = outputString + encapsulateEvent("Times ship scoring has succeeded", shipsScored);
		outputString = outputString + encapsulateEvent("Times ship scoring has failed", shipsFailed);
		outputString = outputString + encapsulateEvent("Times rocket (LVL 1) scoring has succeeded", rocketOneScored);
		outputString = outputString + encapsulateEvent("Times rocket (LVL 1) scoring has failed", rocketOneFailed);
		outputString = outputString + encapsulateEvent("Times rocket (LVL 2) scoring has succeeded", rocketTwoScored);
		outputString = outputString + encapsulateEvent("Times rocket (LVL 2) scoring has failed", rocketTwoFailed);
		outputString = outputString + encapsulateEvent("Times rocket (LVL 3) scoring has succeeded", rocketThreeScored);
		outputString = outputString + encapsulateEvent("Times rocket (LVL 3) scoring has failed", rocketThreeFailed);
		outputString = outputString + encapsulateEvent("Max level that has been climbed", habClimbTier);
		outputString = outputString + encapsulateEvent("Amount of defense actions", defenses);
		outputString = outputString + encapsulateEvent("Amount of field crosses", crosses);
		
		return(outputString);
	}
	
	private String encapsulateEvent(String prefix, String data) {
		return(prefix + ": " + data + ";");
	}
}