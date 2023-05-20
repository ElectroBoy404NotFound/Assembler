package me.ElectronicsBoy.assembler;

import me.ElectronicsBoy.argumentparser.ArgumentParser;

public class Main {
	
	public Main(String[] args) throws Exception {
		new FileParser(new ArgumentParser(args).getArgument("file").getValue());
	}
	public static void main(String[] args) throws Exception {
		new Main(args);
	}
}
