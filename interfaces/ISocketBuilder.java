package interfaces;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The ISocketBuilder interface represents a builder for creating sender and receiver threads for a socket.
 */
public interface ISocketBuilder {

    /**
     * Builds a sender thread for the socket.
     *
     * @param pw the PrintWriter used for sending data over the socket.
     * @return the sender thread.
     */
    public Thread buildSender(PrintWriter pw);

    /**
     * Builds a receiver thread for the socket.
     *
     * @param s the Socket object representing the connection.
     * @param pw the PrintWriter used for sending data over the socket.
     * @param br the BufferedReader used for receiving data from the socket.
     * @return the receiver thread.
     */
    public Thread buildReceiver(Socket s, PrintWriter pw, BufferedReader br);
}