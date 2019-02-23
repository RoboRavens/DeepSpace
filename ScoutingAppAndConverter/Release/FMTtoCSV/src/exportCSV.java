import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Spliterator;
import java.util.stream.*;

public class exportCSV{
	
	BufferedWriter writes;
	BufferedWriter writinger;
	BufferedReader reads;
	importFMT grabber;
	String line;
	String tmp;
	
	ArrayList<String> allData     = new ArrayList<String>();
	ArrayList<String> infoData    = new ArrayList<String>();
	ArrayList<String> eventData   = new ArrayList<String>();
	ArrayList<String> commentData = new ArrayList<String>();
	ArrayList<String> output      = new ArrayList<String>();
	
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
			writinger = new BufferedWriter(new FileWriter(new File((fileName.replace(".txt",".csv")))));
			//For the first 5 non-event values
			while(reads.ready()) {
				//This reads fileName.txt
				allData.add(reads.readLine());
			}
			allData.remove(6);
			allData.remove(5);
			allData.remove(3);
			allData.remove(2);
			splinitialize(allData);
			fillOutputArray();
			writinger.write(listOffOutList());
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
	
	private void splinitialize(ArrayList<String> allData) {
		System.out.println("Data to parse: " + allData);
		int dataSize = allData.size();
		infoData.add(allData.get(0));
		infoData.add(allData.get(1));
		infoData.add(allData.get(2));
		for(int backPlace = 5; backPlace > 0; backPlace--) {
			commentData.add(allData.get(dataSize - backPlace));
		}
		for(int place = 3; place <= (dataSize - 6); place++) {
			eventData.add(allData.get(place));
		}
		System.out.println("Info data: " + infoData + "\nEvent data: " + eventData + "\nCommented data: " + commentData);
	}
	
	private String listOffOutList() {
		String outputString = output.get(0) + ",";
		for (int i = 1; i < output.size(); i++) {
            outputString = outputString + output.get(i)+",";
    	}
		return(outputString);
	}
	
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
	
	public String boolToString(boolean value) {
		return(value ? "true" : "false");
	}
	
	public boolean stringToBool(String string) {
		boolean out = false;
		if(string == "true") {
			out = true;
		}
		return(out);
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
	
	private String encapsulateEvent(String prefix, int data) {
		return(prefix + ": " + data + ";");
	}
	
	public void fillOutputArray() {
		//Add user's name
		output.add(commentData.get(0) + " " + commentData.get(1));
		//Add user's team #
		output.add(infoData.get(0));
		//Add match #
		output.add(infoData.get(2));
		//Add robot's #
		output.add(infoData.get(1));
		//Add robot's color
		output.add(commentData.get(2));
		//Run timesUsed on all events
		for(String string : eventData) {	timesUsed(string);	}
		//Add hatch grab attempts
		output.add((hatchesHeld + hatchesDropped) + "");
		//Add hatch grab successes
		output.add(hatchesHeld + "");
		//Add cargo grab attempts
		output.add((cargoesHeld + cargoesDropped) + "");
		//Add cargo grab successes
		output.add(cargoesHeld + "");
		//Add cargoship attempts
		output.add((shipsScored + shipsFailed) + "");
		//Add cargoship successes
		output.add(shipsScored + "");
		//Add rocket1 attempts
		output.add((rocketOneScored + rocketOneFailed) + "");
		//Add rocket1 successes
		output.add(rocketOneScored + "");
		//Add rocket2 attempts
		output.add((rocketTwoScored + rocketTwoFailed) + "");
		//Add rocket2 successes
		output.add(rocketTwoScored + "");
		//Add rocket3 attempts
		output.add((rocketThreeScored + rocketThreeFailed) + "");
		//Add rocket3 successes
		output.add(rocketThreeScored + "");
		//Add hab climb level
		output.add(habClimbTier + "");
		//Add boolean for if robot can collect cargo
		if (cargoesHeld > 0) {
			output.add("true");
		} else {
			output.add("false");
		}
		//Add boolean for if robot can collect hatches from the floor
		output.add(commentData.get(3));
		//Add boolean for if robot can collect hatches from the station
		output.add(commentData.get(4));
		//Add attempts to score on cargo ship AUTONOMOUSLY
		output.add(getInAutonomous(true, 0));
		//Add successful AUTONOMOUS cargo ship score events
		output.add(getInAutonomous(false, 0));
		//Add attempts to score on rocket lvl1 AUTONOMOUSLY
		output.add(getInAutonomous(true, 1));
		//Add successful AUTONOMOUS rocket lvl1 score events
		output.add(getInAutonomous(false, 1));
		//Add attempts to score on rocket lvl2 AUTONOMOUSLY
		output.add(getInAutonomous(true, 2));
		//Add successful AUTONOMOUS rocket lvl2 score events
		output.add(getInAutonomous(false, 2));
		//Add attempts to score on rocket lvl3 AUTONOMOUSLY
		output.add(getInAutonomous(true, 3));
		//Add successful AUTONOMOUS rocket lvl3 score events
		output.add(getInAutonomous(false, 3));
	}
	
	private String getInAutonomous(boolean outputAttempts, int floor) {
		int goals = 0;
		int fails = 0;
		String output = "Preminger strikes again!";
		if(floor == 0) {
			for(String event : eventData) {
				if(Double.parseDouble(event.substring((event.indexOf(':') + 1 ))) < 15) {
					if(event.startsWith("g")) {
						goals++;
					} else if(event.startsWith("h")) {
						fails++;
					}
				}
			}
		} else if(floor == 1) {
			for(String event : eventData) {
				if(Double.parseDouble(event.substring((event.indexOf(':') + 1 ))) < 15) {
					if(event.startsWith("i")) {
						goals++;
					} else if(event.startsWith("j")) {
						fails++;
					}
				}
			}
		} else if(floor == 2) {
			for(String event : eventData) {
				if(Double.parseDouble(event.substring((event.indexOf(':') + 1 ))) < 15) {
					if(event.startsWith("k")) {
						goals++;
					} else if(event.startsWith("l")) {
						fails++;
					}
				}
			}
		} else if(floor == 3) {
			for(String event : eventData) {
				if(Double.parseDouble(event.substring((event.indexOf(':') + 1 ))) < 15) {
					if(event.startsWith("m")) {
						goals++;
					} else if(event.startsWith("n")) {
						fails++;
					}
				}
			}
		}
		if(outputAttempts) {
			//Output attempts
			output = (goals + fails) + "";
		} else {
			//Output successes
			output = goals + "";
		}
		return(output);
	}
}