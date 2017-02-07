import java.util.*;
import java.io.*;
import java.net.*;
import java.math.*;


public class Game extends Thread{
	String word = "Frog";
	Queue<Pair> queue;
	boolean running = false;
	Server server;

	public Game(Server server) {
		this.server = server;
		queue = new ArrayDeque<>();
		running = true;
		start();
	}

	@Override
	public void run() {
		while(true){}
	}


	public void sendDraw(Message msg){
		Set<String> set = server.map.keySet();
		try {
			for(String s: set) {
				DataOutputStream streamOut = server.map.get(s).getOutputStream();
				if(server.map.get(s).isDrawer()) continue;
				streamOut.writeUTF(Utils.messageToJason(msg));
				streamOut.flush();
			}
		}catch(Exception e) {}
	}

	public void handleMessage(String s, ServerThread st) {}

	public void checkAnswer(Message msg, ServerThread sth) {
		String answer = msg.getMessage();
		if(answer == word) {
			System.out.println("FOUND IT");
		}
	}


}