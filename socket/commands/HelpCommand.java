package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import game.Player;
import socket.Command;
import socket.server.ServerCommandHandler;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("/help",null,null,Command.Role.UNDEFINED,"Display all available commands.");
    }

	@Override
    public String execute(String[] args, Player player, ArrayList<Player> players, Socket socket, PrintWriter pw, BufferedReader br) {
		String message = "List Of Commands;─┬───────────────; │;";
		Role role = player != null ? player.getRole() : Role.UNDEFINED;


		for (Command command : ServerCommandHandler.COMMANDS) {
			if (command.hasPermission(role)) {
				int totalLength = 30;
				int fillWithSpace = totalLength - command.getName().length() - command.getParameters().length();
				if (fillWithSpace < 0) fillWithSpace = 0;
				message += " ├─ " + command.getName() + " " + command.getParameters() + " ".repeat(fillWithSpace) + " : " + command.getHelp() + ";";
			}
		}

		message += " │;";
		message += " └───────────────";

		return message;
	}
}
