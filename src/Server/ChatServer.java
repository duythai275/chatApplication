package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	
	public static ArrayList<ClientThread> clients;
	
	public static void main(String[] args)
	{
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		
		try {
			serverSocket = new ServerSocket(3333);
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		clients =  new ArrayList<ClientThread>();
		while (true) {
			try {
				socket = serverSocket.accept();
				
				ClientThread client = new ClientThread(socket);
				Thread thread = new Thread(client);
				thread.start();
				clients.add(client);
				
//				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
