package ClientChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	public static void main(String[] args) {
		
		ServerSocket serverSocket = null;
		Socket socket = null;
		ArrayList<Socket> socketList = new ArrayList<Socket>();
		ClientHandler clientHandler = null;
		
		try {
			serverSocket = new ServerSocket(3333);
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		while ( true ) {
			try {
				socket = serverSocket.accept();
				socketList.add(socket);
				//TEST
				System.out.println("A new Socket");
				
				if ( socketList.size() == 2 ) {
					clientHandler = new ClientHandler(socketList.get(0), socketList.get(1));
					socketList.clear();
					
					Thread server = new Thread(clientHandler);
					server.start();
					
					while(server.isAlive());
					System.out.println("A Server STOP"); 
				} else if ( socketList.size() == 1 && clientHandler != null ) {
					// Should have a list of ClientHandler
					// Check if in any ClientHandler only 1 thread run -> add new socket to ClientHandler
					if ( clientHandler.isWaiting() ) {
						//TEST
						System.out.println("ADDING a Socket");
						clientHandler.addSocket(socket);
						socketList.clear();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
