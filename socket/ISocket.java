package socket;

interface ISocket {
	void start(int port);
	Thread buildSender(SocketUser user);
	Thread buildReceiver(SocketUser user);
}
