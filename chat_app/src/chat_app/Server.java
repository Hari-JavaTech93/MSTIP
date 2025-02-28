package chat_app;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
	
	private int port;
	private List<PrintStream> clients;
	private ServerSocket serversocket;
	
	public Server(int port) {
		this.port=port;
		this.clients=new ArrayList<PrintStream>();
	}
	
	public static void main(String[] args) throws IOException {
		new Server(12345).run();
	}
	
	public void run() throws IOException {
		serversocket=new ServerSocket(port) {protected void finalize() throws IOException{ this.close(); };};
		System.out.println("Port 12345 is now open.");
		while(true) {
			Socket client=serversocket.accept();
			System.out.println("Connection established with client: " + client.getInetAddress().getHostAddress());
			this.clients.add(new PrintStream(client.getOutputStream()));
			new Thread(new ClientHandler(this,client.getInputStream())).start();;
		}
		
	}
	void broadcastMessages(String message) {
		for(PrintStream client:this.clients) {
			client.println(message);
		}
	}
	
}


class ClientHandler implements Runnable{
	
	private Server server;
	private InputStream client;
	
	public ClientHandler(Server server,InputStream client) {
		// TODO Auto-generated constructor stub
		this.server=server;
		this.client=client;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String message;
		Scanner sc=new Scanner(this.client);
		while(sc.hasNextLine()) {
			message=sc.nextLine();
			server.broadcastMessages(message);
		}
		sc.close();
	}
}
