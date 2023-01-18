package socket.commands;

public class Command {
	private String name;
	private String[] optionalParameters;
	private String[] mandatoryParameters;
	private String documentation;

	public Command(String name, String[] optionalParameters, String[] mandatoryParameters, String documentation) {
		this.name = name;
		this.optionalParameters = optionalParameters != null ? optionalParameters : new String[]{};
		this.mandatoryParameters = mandatoryParameters != null ? optionalParameters : new String[]{};
		this.documentation = documentation;
	}

	public String help() { return documentation; }
	public String getName() { return name; }

	public String getParameters() {
		String output = "";

		for (String parameter : optionalParameters) {
			output += "["+ parameter +"] ";
		}
		for (String parameter : mandatoryParameters) {
			output += ""+ parameter +" ";
		}
		
		return output;
	}
}
