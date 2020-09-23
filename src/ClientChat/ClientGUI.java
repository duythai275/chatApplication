package ClientChat;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientGUI extends JFrame implements PropertyChangeListener {
	
	private JButton connect;
	private JButton disconnect;
	private JButton send;
	private JTextArea board;
	private JTextField message;
	
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private InputListener listener;
	
	public ClientGUI() {
		setTitle("GUI Message Client");
		setLayout(new BorderLayout(10, 10));
		
		JPanel northPanel = createNorthPanel();
		add(northPanel, BorderLayout.NORTH);
		
		JPanel centerPanel = createCenterPanel();
		add(centerPanel, BorderLayout.CENTER);
		
		JPanel southPanel = createSouthPanel();
		add(southPanel, BorderLayout.SOUTH);
	}
	
	private JPanel createNorthPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		panel.add(new JLabel("Message to Send:"), BorderLayout.NORTH);
		
		message = new JTextField();
		panel.add(message, BorderLayout.CENTER);
		
		send = new JButton("Send");
		panel.add(send, BorderLayout.SOUTH);
		send.addActionListener(new SendMessageAction());
		send.setEnabled(false);
		
		return panel;
	}
	
	private JPanel createCenterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		panel.add(new JLabel("Message Board"), BorderLayout.NORTH);
		
		board = new JTextArea(20,50);
		board.setEditable(false);
		panel.add(board, BorderLayout.CENTER);
		
		return panel;
	}
	
	private JPanel createSouthPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		
		connect = new JButton("Connect");
		panel.add(connect);
		connect.addActionListener(new ConnectAction(this));
		
		disconnect = new JButton("Disconnect");
		disconnect.setEnabled(false);
		panel.add(disconnect);
		
		return panel;
	}
	
	public void display() {
		pack();
		setVisible(true);
	}
	
	private class ConnectAction implements ActionListener {
		private ClientGUI clientGUI;
		public ConnectAction(ClientGUI clientGUI) { this.clientGUI = clientGUI; }
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				socket = new Socket("localhost",3333);
				
				listener = new InputListener(socket, 0);
				listener.addListener(this.clientGUI);
				
				Thread client = new Thread(listener);
				client.start();
				
				disconnect.setEnabled(true);
				connect.setEnabled(false);
				send.setEnabled(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private class SendMessageAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				OutputStream os = socket.getOutputStream();
				oos = new ObjectOutputStream(os);
				oos.writeObject(message.getText());
				
				board.append("Me: "+ message.getText() +"\n");
				message.setText("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		try {
			ObjectInputStream ois = (ObjectInputStream) event.getNewValue();
			board.append("Client: "+ (String) ois.readObject() +"\n");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
