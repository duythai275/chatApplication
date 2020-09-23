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
	
	private List<PropertyChangeListener> listeners = new ArrayList<>();;
	
	public InputListener(Socket socket, int number) {
		this.socket = socket;
		this.number = number;
	}
	
	public void addListener(PropertyChangeListener listener) {
		this.listeners.add(listener);
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				notifyListener("Client"+this.number, ois);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void notifyListener(String property, ObjectInputStream ois) {
		for( PropertyChangeListener listener : listeners ) {
			listener.propertyChange(new PropertyChangeEvent(this, property, null, ois));
		}
	}
}
