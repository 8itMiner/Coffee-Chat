import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login {
	public static void main(String[] args) throws java.net.UnknownHostException {
		
		final JFrame login = new JFrame("CoffeeChat - Login");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));
		JLabel nameL = new JLabel("Username : ");
		nameL.setFont(new Font("Helvetica", Font.PLAIN, 30));
		JLabel ipL = new JLabel("IP Address : ");
		ipL.setFont(new Font("Helvetica", Font.PLAIN, 30));
		JTextField loginName = new JTextField(20);
		JPanel enterP = new JPanel();
		JButton enter = new JButton("Join chatroom");
		JButton serverStart = new JButton("Start new server");
		JTextField ipTF = new JTextField(20);
		
		panel.add(nameL);
		panel.add(loginName);
		panel.add(ipL);
		panel.add(ipTF);
		panel.add(enter);
		panel.add(serverStart);
		login.setSize(500, 200);
		login.add(panel);
		login.setVisible(true);
		
		serverStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ChatServer server = new ChatServer();
					JFrame serverF = new JFrame();
					JOptionPane.showMessageDialog(serverF, "Your server is now running at : \n" + InetAddress.getLocalHost() + " : 5217", "CoffeeChat - New server", JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		enter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ChatClient client = new ChatClient(loginName.getText(), ipTF.getText());
					login.setVisible(false);
					login.dispose();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		loginName.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						ChatClient client = new ChatClient(loginName.getText(), ipTF.getText());
						login.setVisible(false);
						login.dispose();
					} catch (java.net.UnknownHostException e1) {
						e1.printStackTrace();
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
		
	}
}
