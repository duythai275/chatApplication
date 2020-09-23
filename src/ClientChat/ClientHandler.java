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
	
	public ClientHandler ( Socket s1, Socket s2 ) {
		this.socket1 = s1;
		this.socket2 = s2;
	}
	
	@Override
	public void run() {
		listener1 = new InputListener(socket1, 1);
		listener2 = new InputListener(socket2, 2);
		
		listener1.addListener(this);
		listener2.addListener(this);
		
		Thread client1 = new Thread(listener1);
		Thread client2 = new Thread(listener2);
		
		client1.start();
		client2.start();
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		try {
			String propertyChanged = event.getPropertyName();
			ObjectInputStream ois = (ObjectInputStream) event.getNewValue();
			
			if ( propertyChanged.equals("Client1") ) {
				oos2 = new ObjectOutputStream(socket2.getOutputStream());
				oos2.writeObject(ois.readObject());
			} else if ( propertyChanged.equals("Client2") ) {
				oos1 = new ObjectOutputStream(socket1.getOutputStream());
				oos1.writeObject(ois.readObject());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
