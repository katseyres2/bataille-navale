package socket.commands;

import socket.Command;
import socket.client.SocketClient;
import socket.server.Player;

import java.util.ArrayList;

public class InviteBotCommand extends Command {

    public InviteBotCommand() {
        super("/invitebot", new String[]{}, new String[]{}, Role.AUTHENTICATED, "Invite a player bot");
    }

    @Override
    public String execute(String[] args, SocketClient client, ArrayList<Player> players) {

        return "";
    }
}
