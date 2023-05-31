package interfaces;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public interface ISocketBuilder {
    public Thread buildSender(PrintWriter pw);
    public Thread buildReceiver(Socket s, PrintWriter pw, BufferedReader br);
}
