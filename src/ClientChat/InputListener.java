package ClientChat;

import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class InputListener implements Runnable {
	private int number;
	private Socket socket;
	
	private ObjectInputStream ois = null;
	private boolean connect = true;
	
	private List<PropertyChangeListener> listeners = new ArrayList<>();
	
	public InputListener(Socket socket, int number) {
		this.socket = socket;
		this.number = number;
	}
	
	public void addListener(PropertyChangeListener listener) {
		this.listeners.add(listener);
	}
	
	public void disconnect() {
		try {
			ois.close();
			socket.close();
			connect = false;
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setConnect(boolean status) {
		this.connect = status;
	}
	
	@Override
	public void run() {
		while(connect) {
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				String msg = (String) ois.readObject();
				notifyListener("Client"+this.number, msg);
				if ( msg.equals("DISCONNECTED!!!") ) {
					disconnect();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void notifyListener(String property, String ois) {
		for( PropertyChangeListener listener : listeners ) {
			listener.propertyChange(new PropertyChangeEvent(this, property, null, ois));
		}
	}
}
