package socket;

import interfaces.ICommand;

/**
 * Abstract class representing a command.
 * Commands should inherit from this class and implement the ICommand interface.
 */
public abstract class Command implements ICommand {
	public enum Role {
		UNDEFINED,       // Default role for a command
		AUTHENTICATED,   // Role for authenticated users
		ADMIN            // Role for admin users
	}

	String name;
	String[] optionalParameters;
	String[] mandatoryParameters;
	String documentation;
	Role role;

	/**
	 * Constructor for the Command class.
	 *
	 * @param name                The name of the command.
	 * @param optionalParameters  Array of optional parameters for the command.
	 * @param mandatoryParameters Array of mandatory parameters for the command.
	 * @param role                The role required to execute the command.
	 * @param documentation       The documentation or help message for the command.
	 */
	public Command(String name, String[] optionalParameters, String[] mandatoryParameters, Role role, String documentation) {
		this.name = name;
		this.optionalParameters = optionalParameters != null ? optionalParameters : new String[]{};
		this.mandatoryParameters = mandatoryParameters != null ? mandatoryParameters : new String[]{};
		this.documentation = documentation;
		this.role = role;
	}

	/**
	 * Retrieves the help/documentation message for the command.
	 *
	 * @return The help/documentation message.
	 */
	public String getHelp() {
		return documentation;
	}

	/**
	 * Retrieves the name of the command.
	 *
	 * @return The name of the command.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the role required to execute the command.
	 *
	 * @return The role required to execute the command.
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Checks if a role has permission to execute the command.
	 *
	 * @param value The role to check permission for.
	 * @return True if the role has permission, false otherwise.
	 */
	public boolean hasPermission(Role value) {
		return value == this.role;
	}

	/**
	 * Retrieves the formatted string representation of the command's parameters.
	 *
	 * @return The formatted string representation of the command's parameters.
	 */
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