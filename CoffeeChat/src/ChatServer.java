import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class ChatServer {
	static Vector ClientSockets;
	static Vector LoginNames;
	
	ChatServer() throws IOException {
		ServerSocket server = new ServerSocket(5217);
		ClientSockets = new Vector();
		LoginNames = new Vector();
		
		while(true) {
			Socket client = server.accept();
			AcceptClient acceptClient = new AcceptClient(client);
		}
	}
	
	public static void main(String[] args) throws IOException {
		ChatServer server = new ChatServer();
	}
	
	class AcceptClient extends Thread {
		Socket ClientSocket;
		DataInputStream din;
		DataOutputStream dout;
		AcceptClient(Socket client) throws IOException {
			ClientSocket = client;
			din = new DataInputStream(ClientSocket.getInputStream());
			dout = new DataOutputStream(ClientSocket.getOutputStream());
			
		
			String LoginName = din.readUTF();
			
			LoginNames.add(LoginName);
			ClientSockets.add(ClientSocket);
			
			start();
		}
		
		public void run() {
			
			while(true) {
				
				try {
					String msgFromClient = din.readUTF();
					StringTokenizer st = new StringTokenizer(msgFromClient);
					String LoginName = st.nextToken();
					String MsgType = st.nextToken();
					
					String msg = "";
					
					int lo = -1;
					
					while (st.hasMoreTokens()) {
						msg = msg + " " + st.nextToken();
					}
					
					if (MsgType.equals("LOGIN")) {
						
						for(int i = 0;i < LoginNames.size();i++) {
							Socket pSocket = (Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
							pOut.writeUTF("  [ Server Message ] : " + LoginName + " has entered the chat.");
						}
						
					}
					else if (MsgType.equals("LOGOUT")) {
						
						for(int i = 0;i < LoginNames.size();i++) {
							
							if (LoginName.equals(LoginNames.elementAt(i))) {
								lo = i;
							}
							
							Socket pSocket = (Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
							pOut.writeUTF("  [ Server Message ] : " + LoginName + " has left the chat.");
						}
						
						if (lo >= 0) {
							LoginNames.removeElementAt(lo);
							ClientSockets.removeElementAt(lo);
						}
						
					}
						
					else {
						for(int i = 0;i < LoginNames.size();i++) {
							Socket pSocket = (Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
							pOut.writeUTF("  [ " + LoginName + " ] : " + msg);
						}
					}
					
					if (MsgType.equals("LOGOUT"))
						break;
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
