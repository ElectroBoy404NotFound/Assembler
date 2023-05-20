package me.ElectronicsBoy.argumentparser;

public class Argument {
	private String arg;
	private String val;
	
	public Argument(String arg, String val) {
		this.arg = arg;
		this.val = val;
	}
	
	public String getArgument() {
		return arg;
	}
	public String getValue() {
		return val;
	}
	
	public void setValue(String value) {
		this.val = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Argument) {
			if(((Argument)(obj)).getArgument().equals(this.getArgument())) {
				if(((Argument)(obj)).getValue().equals(this.getValue())) {
					return true;
				}
				return false;
			}
			return false;
		}
		return false;
	}
}
