.replace("cargoCount", "11881188").replace("hatchCount", "1818")
.replaceAll("a", "Scored on Hab side 1 at").replaceAll("b", "Scored on Hab side 2 at").replaceAll("c", "Cargo has gone up at")
.replaceAll("d", "Cargo dropped at").replaceAll("e", "Hatch went up at").replaceAll("f", "Hatch was dropped at")
.replaceAll("g", "Ship scored sucessfully at").replaceAll("h", "Ship score attempt made, they failed, at")
.replaceAll("i", "Rocket one was scored on sucessfully at").replaceAll("j", "Rocket one was attempted on at")
.replaceAll("k", "Rocket 2 was scored on").replaceAll("l", "Rocket 2 was attempted on at").replaceAll("m", "Rocket 3 was scored on at")
.replaceAll("n", "Rocket 3 was attempted at").replaceAll("o", "Hab one was climbed at").replaceAll("p", "Hab 2 was climbed at")
.replaceAll("q", "Hab 3 was climbed at").replaceAll("r", "A hab was attempted, though failed, at").replaceAll("s", "A robot defended at")
.replaceAll("t", "A robot crossed the feild at").replace("11881188", "Cargo Count").replace("1818", "Hatch Count")

			writes.write("Hey, Vsauce, Micheal here!");
			writinger = new BufferedWriter(new FileWriter(new File(fileName.replace(".txt", ".csv"))));
			tmp = line.replaceAll(":", "").replaceAll(",", ": ").replaceAll(";", "\n");
			writes.write(tmp);
			reads = new Scanner(new File(fileName));
			while(reads.nextLine() != null) {
				writinger.write(fix(reads.nextLine()));
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
		