package me.ElectronicsBoy.assembler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class FileParser {
	public FileParser(String file, String outputFile) throws Exception {
		File programFile = new File(file);
		if(!programFile.exists()) { System.err.println("Program File \"" + file + "\" not found!"); System.exit(-1); }
		file = file.replaceAll(" ", "");
		outputFile = outputFile.replaceAll(" ", "");
		System.out.println("Assembling file '" + file + "'");
		BufferedReader reader = new BufferedReader(new FileReader(programFile));
		String l;
		int[] finalOutput = new int[258];
		int nextByte = 0;
		for(int i = 0; i < Main.PROG_EEPROM_SIZE; i++) finalOutput[i] = 0;
		int lineNo = 0;
		boolean err = false;
		while((l = reader.readLine()) != null) {
			lineNo++;
			String[] tokens = l.split(" ");
			if(tokens.length > 0) {
				if(!tokens[0].equals("#") && nextByte >= 256) { System.err.println("Out of program space! Line: " + lineNo); System.exit(-1);}
				switch(tokens[0]) {
				case "#":
					continue;
				case "LOAD":
					if(tokens.length > 3) { System.err.println(file + ':' + lineNo + ": Too many arguments for LOAD!"); err = true;}
					else if(tokens.length < 3) { System.err.println(file + ':' + lineNo + ": Too few arguments for LOAD!"); err = true;}
						else {
							if(!listContains(tokens[1], Main.registers)) {
								System.err.println(file + ':' + lineNo + ": Unknown destination register '" + tokens[1] + "'!"); 
								err = true;
								break;
							} else
								finalOutput[nextByte] = Main.instructions.get("LOAD_" + tokens[1]);
							
							finalOutput[nextByte + 1] = stringParser(tokens[2]);
							nextByte+=2;
						}
					break;
				case "NOP":
					if(tokens.length > 1) { System.err.println(file + ':' + lineNo + ": Too many arguments for NOP!"); err = true; }
					else {
						finalOutput[nextByte] = Main.instructions.get(tokens[0]);
						nextByte++;
					}
					break;
				case "JUMP":
					if(tokens.length > 2) { System.err.println(file + ':' + lineNo + ": Too many arguments for JUMP!"); err = true; }
					else
						if(tokens.length < 2) { System.err.println(file + ':' + lineNo + ": Too few arguments for JUMP!"); err = true; }
						else {
							finalOutput[nextByte] = Main.instructions.get(tokens[0]);
							finalOutput[nextByte + 1] = stringParser(tokens[1]);
							nextByte+=2;
						}
					break;
				case "JMPR":
					if(tokens.length > 2) { System.err.println(file + ':' + lineNo + ": Too many arguments for JMPA!"); err = true; }
					else 
						if(tokens.length < 2) { System.err.println(file + ':' + lineNo + ": Too few arguments for JMR!"); err = true; }
						else
							if(!listContains(tokens[1], Main.registers)) {
								System.err.println(file + ':' + lineNo + ": Unknown destination register '" + tokens[1] + "'!"); 
								err = true;
								break;
							} else {
								finalOutput[nextByte] = Main.instructions.get("JUMP_" + tokens[1]);
								nextByte++;
							}
					break;
				case "[PLACE_AT]":
					if(tokens.length > 2) { System.err.println(file + ':' + lineNo + ": Too many arguments for [PLACE_AT]!"); err = true; break; }
					else if(tokens.length < 2) { System.err.println(file + ':' + lineNo + ": Too few arguments for [PLACE_AT]!"); err = true; break; }
					nextByte = stringParser(tokens[1]);
					break;
				case "":
					break;
				default:
					System.err.println(file + ':' + lineNo + ": Unknown Instruction '" + tokens[0] + "'!");
					err = true;
				}
			}
		}
		if(!err) {
			System.out.println("Assembled!");
			System.out.println("Program size: " + nextByte + " bytes / " + "256 bytes " + "(" + (100*((double)nextByte/256)) + "%)");
			File f = new File(outputFile);
			f.delete();	f.createNewFile();
			FileOutputStream fw = new FileOutputStream(f);
			byte[] fileOutput = new byte[256];
			for(int i = 0; i < 256; i++)
				fileOutput[i] = (byte) finalOutput[i];
			fw.write(fileOutput);
			fw.flush();
			fw.close();
			
			File f2 = new File(outputFile + ".dep");
			f2.delete();	f2.createNewFile();
			FileWriter fw2 = new FileWriter(outputFile + ".dep");
			fw2.write("e 00\n");
			for(int i = 0; i <nextByte; i++)
				fw2.write("W " + (i < 0x10 ? "0" + Integer.toHexString(i) : Integer.toHexString(i)) + " " + (finalOutput[i] < 0x10 ? "0" + Integer.toHexString(finalOutput[i]) : Integer.toHexString(finalOutput[i])) + "\n");
			fw2.close();
			
			System.out.println("Output save Successful!");
			System.out.println("Binary File Saved to: " + f.getAbsolutePath());
			System.out.println("Deploy Data Saved to: " + f2.getAbsolutePath());
		} else {
			System.err.println("Errors were found in the program!");
		}
		reader.close();
	}
	private boolean listContains(String string, List<String> list) {
		for(String ele : list)
			if(ele.equals(string)) return true;
		return false;
	}
	private int stringParser(String token) {
		if(token.startsWith("$"))
			return Integer.decode("0x" + token.replace("$", ""));
		if(token.startsWith("#"))
			return Integer.parseInt(token.replace("#", ""), 2);
		if(token.startsWith("%"))
			return (int)token.replace("%", "").charAt(0);
		if(token.startsWith("@"))
			return Integer.parseInt(token.replace("@", ""));
		return 0;
	}
}
