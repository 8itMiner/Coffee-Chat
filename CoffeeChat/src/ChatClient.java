import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame implements Runnable{
	
	Socket socket;
	JTextArea ta;
	JButton send, logout;
	JTextField tf;
	
	Thread thread;
	
	DataInputStream din;
	DataOutputStream dout;
	
	String LoginName;
	
	ChatClient(String login, String ip) throws UnknownHostException, IOException {
		super("Coffee Chat - " + login + "'s Chat Box");
		LoginName = login;
		
		addWindowListener (new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					dout.writeUTF(LoginName + " " + "LOGOUT");
					System.exit(1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		ta = new JTextArea(18, 50);
		tf = new JTextField(50);
		
		send = new JButton("Send");
		logout = new JButton("Logout");
		
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (tf.getText().length() > 0) {
						dout.writeUTF(LoginName + " " + "DATA " + tf.getText().toString());
					}
					tf.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		tf.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						if (tf.getText().length() > 0) {
							dout.writeUTF(LoginName + " " + "DATA " + tf.getText().toString());
						}
						tf.setText("");
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
		});
		
		logout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dout.writeUTF(LoginName + " " + "LOGOUT");
					System.exit(1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		socket = new Socket(ip, 5217);
		
		din = new DataInputStream(socket.getInputStream());
		dout = new DataOutputStream(socket.getOutputStream());
		
		dout.writeUTF(LoginName);
		dout.writeUTF(LoginName + " " + "LOGIN");
		
		thread = new Thread(this);
		thread.start();
		setup();
	}
	
	private void setup() {
		setSize(600, 420);
		
		JPanel panel = new JPanel();
		
		panel.add(new JScrollPane(ta));
		panel.add(tf);
		panel.add(send);
		panel.add(logout);
		
		add(panel);
		
		setVisible(true);
	}

	@Override
	public void run() {
		while(true) {
			try {
				ta.append("\n" + din.readUTF());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
/*
	public static void main(String[] args) throws UnknownHostException, IOException {
		ChatClient client = new ChatClient("User2");
	}
*/

}
