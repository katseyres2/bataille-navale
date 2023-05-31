package socket;
import interfaces.ICommand;

public abstract class Command implements ICommand {
	public enum Role {
		UNDEFINED,
		AUTHENTICATED,
		ADMIN
	}

	String name;
	String[] optionalParameters;
	String[] mandatoryParameters;
	String documentation;
	Role role;

	public Command(String name, String[] optionalParameters, String[] mandatoryParameters,  Role role, String documentation) {
		this.name = name;
		this.optionalParameters = optionalParameters != null ? optionalParameters : new String[]{};
		this.mandatoryParameters = mandatoryParameters != null ? mandatoryParameters : new String[]{};
		this.documentation = documentation;
		this.role = role;
	}

	public String getHelp() 	{ return documentation; }
	public String getName() 	{ return name; 			}
	public Role getRole() 		{ return role;			}

	public boolean hasPermission(Role value) {
		if (value == Role.ADMIN) return true;
		return value == Role.AUTHENTICATED && role == Role.AUTHENTICATED || role == Role.UNDEFINED;
	}

	public String getParameters() {
		StringBuilder output = new StringBuilder();

		for (String parameter : optionalParameters) {
			output.append("[").append(parameter).append("] ");
		}
		for (String parameter : mandatoryParameters) {
			output.append("<").append(parameter).append("> ");
		}
		
		return output.toString();
	}
}
