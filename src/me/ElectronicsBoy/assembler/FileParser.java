package me.ElectronicsBoy.assembler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileParser {
	public FileParser(String file) throws Exception {
		File programFile = new File(file);
		if(!programFile.exists()) { System.err.println("Program File \"" + file + "\" not found!"); System.exit(-1); }
		BufferedReader reader = new BufferedReader(new FileReader(programFile));
		String l;
		while((l = reader.readLine()) != null) {
			String[] tokens = l.split(" ");
			int instruction = 0;
			switch(tokens[0]) {
			case "#":
				continue;
			case "LOAD":
				switch(tokens[1]) {
				case "A":
					instruction = 0x01;
					break;
				default:
					System.out.println("Unknown Load Destination \"" + tokens[1] + "\"!");
					break;
				}
				break;
			case "NOP":
				instruction = 0;
				break;
			}
		}
		reader.close();
	}
}
