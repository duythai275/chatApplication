package ClientChat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable, PropertyChangeListener {
	private Socket socket1;
	private Socket socket2;
	private ObjectOutputStream oos1 = null;
	private ObjectOutputStream oos2 = null;
	private InputListener listener1;
	private InputListener listener2;
	
	Thread client1;
	Thread client2;
	
	public ClientHandler ( Socket s1, Socket s2 ) {
		this.socket1 = s1;
		this.socket2 = s2;
		
		listener1 = new InputListener(socket1, 1);
		listener2 = new InputListener(socket2, 2);
		
		listener1.addListener(this);
		listener2.addListener(this);
		
		client1 = new Thread(listener1);
		client2 = new Thread(listener2);
		
		client1.start();
		client2.start();
		
		try {
			oos1 = new ObjectOutputStream(socket1.getOutputStream());
			oos1.writeObject((String) "Connected!!!");
			oos2 = new ObjectOutputStream(socket2.getOutputStream());
			oos2.writeObject((String) "Connected!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isWaiting () {
		return (client1.isAlive() && !client2.isAlive()) || (client2.isAlive() && !client1.isAlive());
	}
	
	public void addSocket(Socket socket) {
		if ( !client1.isAlive() ) {
			
			this.socket1 = socket;
			listener1 = new InputListener(socket1, 1);
			
			listener1.addListener(this);
			
			client1 = new Thread(listener1);
			client1.start();
			try {
				oos1 = new ObjectOutputStream(socket1.getOutputStream());
				oos1.writeObject((String) "Connected!!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			
			this.socket2 = socket;
			listener2 = new InputListener(socket2, 2);
			
			listener2.addListener(this);
			
			client2 = new Thread(listener2);
			client2.start();
			try {
				oos2 = new ObjectOutputStream(socket2.getOutputStream());
				oos2.writeObject((String) "Connected!!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
	public void run() {
		while(client1.isAlive() && client2.isAlive());
		//TEST
		System.out.println("ClientHandle Stopped!!!");
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		try {
			String propertyChanged = event.getPropertyName();
			String ois = (String) event.getNewValue();
			String msg = ois;
			
			if ( propertyChanged.equals("Client1") ) {
				if ( msg.equals("DISCONNECTED!!!") ) {
					oos1 = new ObjectOutputStream(socket1.getOutputStream());
					oos1.writeObject(msg);
					oos1.close();
				} else if (!isWaiting()) {
					oos2 = new ObjectOutputStream(socket2.getOutputStream());
					oos2.writeObject(msg);
				}
			} else if ( propertyChanged.equals("Client2") ) {
				if ( msg.equals("DISCONNECTED!!!") ) {
					oos2 = new ObjectOutputStream(socket2.getOutputStream());
					oos2.writeObject(msg);
					oos2.close();
				} else if (!isWaiting()) {
					oos1 = new ObjectOutputStream(socket1.getOutputStream());
					oos1.writeObject(msg);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
