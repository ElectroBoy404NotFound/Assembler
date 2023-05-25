package me.ElectronicsBoy.argumentparser;

import java.util.HashMap;

public class ArgumentParser {
	public static final String VERSION = "V1.0.0";
	private HashMap<String, Argument> parsedArgs = new HashMap<String, Argument>();
	
	public ArgumentParser(String[] args) {
		StringBuilder argumentBuilder = new StringBuilder();
		String currentArg = "";
		boolean arg = true;
		for(String token : args) {
			if(token.startsWith("-")) {
				arg = true;
				if(!currentArg.equals(""))
					parsedArgs.get(currentArg).setValue(argumentBuilder.toString());
				argumentBuilder.delete(0, argumentBuilder.capacity());
			} else
				argumentBuilder.append(token + " ");
			if(arg) {
				currentArg = token.replaceAll("-", "");
				parsedArgs.put(currentArg, new Argument(token, ""));
				arg = false;
			}
		}
		parsedArgs.get(currentArg).setValue(argumentBuilder.toString());
	}
	
	public Argument getArgument(String arg) {
		Argument retval = parsedArgs.get(arg);
		if(retval == null) retval = new Argument(arg, "");
		return retval;
	}
}
