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
		ClientHandler clientHandler;
		
		try {
			serverSocket = new ServerSocket(3333);
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		while ( true ) {
			try {
				socket = serverSocket.accept();
				socketList.add(socket);
				
				if ( socketList.size() == 2 ) {
					clientHandler = new ClientHandler(socketList.get(0), socketList.get(1));
					Thread server = new Thread(clientHandler);
					server.start();
					
					socketList.clear();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
