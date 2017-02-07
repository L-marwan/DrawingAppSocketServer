import java.util.*;
import java.io.*;
import java.net.*;
import com.google.gson.*;

public class Server extends Thread{

	private final int _PORT = 2017;
	private ServerSocket serverSocket;
	private boolean running = false;
	private int num_rdy;
	private int count = 1;

	public HashMap<String, ServerThread> map;
	public Game game;



	public Server (){
		DBG("Server Launched");
		try {
			serverSocket = new ServerSocket(_PORT);
			map = new HashMap<String, ServerThread>();
			num_rdy = count=  0;
			running = true;
			start();

		}catch(Exception e) {
			DBG("Server stopped due to an Exception ("+e.getMessage()+")");
		}
	}

	@Override
 	public void run() {
		DBG("Server Started a PORT: "+_PORT);

		try {
			while(running)  {

				DBG("Waiting For Connection ...");
				Socket socket = serverSocket.accept();	// wait for a client to connect to the server
				DataInputStream streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

				String line = streamIn.readUTF();
				Message msg = Utils.jsonToMessage(line);
				msg.setMessage(msg.getMessage()+(count++));

				DBG("User "+msg.getMessage()+" Connected");
				ServerThread serverThread = new ServerThread(this, socket, msg.getMessage());
				serverThread.start();

				Set<String> users = map.keySet();
				for(String s: users) {
					DataOutputStream output = map.get(s).getOutputStream();
					output.writeUTF(line);
					output.flush();

					output = new DataOutputStream(socket.getOutputStream());
					output.writeUTF(Utils.messageToJason(new Message(Message.FLAG.FULLNAME, s)));
					output.flush();
				}

				map.put(msg.getMessage(), serverThread);
			}
		}catch(Exception e) {
			DBG("Server stopped duo to an Exception ("+e.getMessage()+")");
		}

	}


	public void setReady(ServerThread sth) {
		if(map.get(sth.getClientName()) == null) return;
		if(sth.isReady()) num_rdy++;

		if(num_rdy == map.keySet().size()) startGame();
	}

	private void startGame() {
		game = new Game(this);
		Set<String> set = map.keySet();
		boolean choose = true;
		for(String s: set) {
			map.get(s).startGame(game, choose);
			choose = false;
		}

		DBG("GAME LAUNCHED....");
	}


	/**
	 * Main Method
	 * @param args [description]
	 */
	public static void main(String[] args) {
		Server server = new Server();
	}

	public static void DBG(String s) {
		System.out.println(s);
	}
}

