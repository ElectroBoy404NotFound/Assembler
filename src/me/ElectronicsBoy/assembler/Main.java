package me.ElectronicsBoy.assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.ElectronicsBoy.argumentparser.ArgumentParser;

public class Main {
	public static final String VERSION = "V1.0.0";
	public static final int PROG_EEPROM_SIZE = 256;
	
	public static HashMap<String, Integer> instructions = new HashMap<>();
	public static List<String> registers = new ArrayList<>();
	
	public Main(String[] args) throws Exception {
		System.out.println("ElectronicsBoy's PipeLine CPU Assember");
		System.out.println("Assembler Version: " + Main.VERSION);
		System.out.println("Argument Parser Version: " + ArgumentParser.VERSION);
		ArgumentParser parser = new ArgumentParser(args);
		
		registers.add("A");
		registers.add("B");
		
		instructions.put("NOP", 0x00);
		instructions.put("LOAD_A", 0x01);
		instructions.put("LOAD_B", 0x02);
		instructions.put("JUMP", 0x03);
		instructions.put("JUMP_A", 0x04);
		instructions.put("JUMP_B", 0x05);
		
		new FileParser(parser.getArgument("file").getValue(), parser.getArgument("outfile").getValue());
	}
	public static void main(String[] args) throws Exception {
		new Main(args);
	}
}
