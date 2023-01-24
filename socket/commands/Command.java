package socket.commands;

public class Command {
	public static enum Role {
		UNDEFINED,
		AUTHENTICATED,
		ADMIN
	};

	private String name;
	private String[] optionalParameters;
	private String[] mandatoryParameters;
	private String documentation;
	private Role role;

	public Command(String name, String[] optionalParameters, String[] mandatoryParameters,  Role role, String documentation) {
		this.name = name;
		this.optionalParameters = optionalParameters != null ? optionalParameters : new String[]{};
		this.mandatoryParameters = mandatoryParameters != null ? mandatoryParameters : new String[]{};
		this.documentation = documentation;
		this.role = role;
	}

	public String help() 		{ return documentation; }
	public String getName() 	{ return name; 			}
	public Role getRole() 		{ return role;			}

	public boolean hasPermission(Role value) {
		if (value == Role.ADMIN) return true;
		if (value == Role.AUTHENTICATED && role == Role.AUTHENTICATED || role == Role.UNDEFINED) return true;
		if (value == Role.UNDEFINED && role == Role.UNDEFINED) return true;
		return false;
	}

	public String getParameters() {
		String output = "";

		for (String parameter : optionalParameters) {
			output += "["+ parameter +"] ";
		}
		for (String parameter : mandatoryParameters) {
			output += "<"+ parameter +"> ";
		}
		
		return output;
	}
}
