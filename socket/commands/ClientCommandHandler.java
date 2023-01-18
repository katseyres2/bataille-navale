package socket.commands;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientCommandHandler {
	public static final ArrayList<Command> COMMANDS = new ArrayList<Command>(Arrays.asList(
		new Command("help", null, null,
			"Display all available commands."
		),
		new Command("ping", null, null,
			"Send a ping request the server to check if you're connected."
		),
		new Command("invite", new String[]{"help"}, null,
			"Invite your friends to play a new game."
		),
		new Command("users", null, null,
			"List all connected users."
		)
	));
}
