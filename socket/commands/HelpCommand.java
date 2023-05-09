package socket.commands;

import java.util.ArrayList;

import socket.server.Player;
import socket.Command;
import socket.server.ServerCommandHandler;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("/help",null,null,Command.Role.UNDEFINED,"Display all available commands.");
    }

	@Override
    public String execute(String[] args, Player player, ArrayList<Player> players) {
		String message = "List Of Commands;-----------------; |;";
		Role role = player != null ? player.getRole() : Role.UNDEFINED;

		for (Command command : ServerCommandHandler.commands) {
			if (command.hasPermission(role)) {
				int totalLength = 40;
				int fillWithSpace = totalLength - command.getName().length() - command.getParameters().length();
				message += " | " + command.getName() + " " + command.getParameters() + " ".repeat(fillWithSpace) + " : " + command.getHelp() + ";";
			}
		}

		message += " |;";
		message += " |---------------";

		return message;
	}
}
