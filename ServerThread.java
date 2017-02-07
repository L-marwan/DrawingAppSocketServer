import java.util.*;
import java.io.*;
import java.net.*;
import java.math.*;


public class ServerThread extends Thread {

	private Socket socket;
	private String clientName;
	private boolean running = false, game_started = false, isDrawer = false, ready = false;
	private DataOutputStream streamOut;
	private DataInputStream streamIn;
	private Server server;

	public Game game;
	
	public ServerThread(Server server, Socket socket, String name) {
		this.socket = socket;
		this.server = server;
		clientName = name;
		try {
			streamOut = new DataOutputStream(socket.getOutputStream());
			streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		}catch(Exception e) {}
		running = true;
	}

	public void stopServerThread() {
		running = false;
		if(socket != null) {
			try {
				socket.close();
			}catch(Exception e) {}
		}
	}

	@Override
	public void run() {
		try {
			while(running) {
				if(streamIn.available() != 0)  {
					String line = streamIn.readUTF();
					if(line != null) {
						Message msg = Utils.jsonToMessage(line);
						handleMessage(msg);
					}			
				}
			}
		}catch(Exception e) {
			System.out.println("!Error :"+e.getMessage());
		}
	}

	private void handleMessage(Message msg) {
		System.out.println(Utils.messageToJason(msg));
		if(msg.flag == Message.FLAG.READY) {
			ready = true;
			server.setReady(this);
		} else if(msg.flag == Message.FLAG.ACTION) {
			game.sendDraw(msg);
		} else if(msg.flag == Message.FLAG.ANSWER) {
			game.checkAnswer(msg, this);
		}
	}

	public DataOutputStream getOutputStream() {
		return this.streamOut;
	}


	public void startGame(Game game, boolean choosen) {
		this.game = game;
		this.isDrawer = choosen;
		game_started= true;

		Message msg = new Message(Message.FLAG.START, isDrawer? "DRAWER": "NOT DRAWER");
		
		try {
			streamOut.writeUTF(Utils.messageToJason(msg));
			streamOut.flush();
		}catch(Exception e) {}
	}


	public String getClientName() {
		return clientName;
	}

	public boolean isReady() {
		return ready;
	}

	public boolean isDrawer() {
		return this.isDrawer;
	}
}