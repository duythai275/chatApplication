package Client;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.*;

public class Client {
	public static void main(String[] args)
	{
		System.out.println("Please enter username");
		Scanner scan = new Scanner(System.in);
		String name = scan.nextLine();
		scan.close();
		
		try {
			Socket socket = new Socket("localhost",3333);
			
			Thread.sleep(1000);
			Thread server = new Thread(new ServerThread(socket,name));
			server.start();
			
//			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
