package chat_app;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	private String host;
	private int port;
	private String nickname;
	
	public Client(String host,int port) {
		this.host=host;
		this.port=port;
	}
	
	public static void main(String[] args) throws UnknownHostException,IOException {
		new Client("localhost", 12345).run();
	}
	
	public void run() throws UnknownHostException, IOException{
		// connect client to server
		Socket client=new Socket(host,port);
		System.out.println("Client successfully connected to server!");
		
		// create a new thread for server messages handling
		new Thread(new ReceivedMessagesHandler(client.getInputStream())).start();
		
		// ask for a nickname
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter a nickname: ");
		nickname=sc.nextLine();
		
		// read messages from keyboard and send to server
		System.out.println("Send messages: ");
		PrintStream output=new PrintStream(client.getOutputStream());
		while(sc.hasNextLine()) {
			output.println(nickname + ": " + sc.nextLine());
		}
		output.close();
		sc.close();
		client.close();
	}
}

 class ReceivedMessagesHandler implements Runnable{
	
	private InputStream server;
	public ReceivedMessagesHandler(InputStream server)
	{
		this.server = server;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(server);
		
		while(sc.hasNextLine()) {
			// receive server messages and print out to screen
			System.out.println(sc.nextLine());
		}
		sc.close();
		
	}
	
}
