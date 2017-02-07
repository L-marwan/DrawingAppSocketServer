import java.util.*;
import java.net.*;
import java.io.*;

public class Client implements Runnable{

	private String name;
	private Socket socket;
	private Scanner console   = new Scanner(System.in);
   	private DataOutputStream streamOut;
   	private DataInputStream streamIn;
   	private boolean running = false;

   	public Client() {
   		Scanner in = new Scanner(System.in);
   		System.out.print("Name : ");
   		name = in.nextLine();

   		System.out.println("Hello "+name);
   		System.out.println("Establishing Connection to the Server ...");
   		try {
   			socket = new Socket("127.0.01", 2017);
   			System.out.println("Connected!!");

   			
   			streamOut = new DataOutputStream(socket.getOutputStream());
   			streamOut.writeUTF(name);
	        streamOut.flush();

	        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

   			running = true;
   			start();
   		}catch(UnknownHostException uhe) {
   			System.out.println("ERROR: Host Unknown ("+uhe.getMessage()+")!");
   		} catch(IOException ioe) {  
   			System.out.println("Unexpected exception: " + ioe.getMessage());
   		}
   	}

   	@Override
   	public void run() {
   		try {
   			Scanner in = new Scanner(System.in);
   			while(true) {

   				if(streamIn.available() != 0) {
   					String line = streamIn.readUTF();
   					if(line != null) System.out.println("Message recieved form Server : "+line);
   				}

   				String msg = in.nextLine();
   				streamOut.writeUTF(msg);
   				streamOut.flush();

   				if(msg.equals("BYE")) break;
   			}
   		} catch(Exception e) {}
   		
   		try {streamOut.close();}catch(Exception e) {
   			System.out.println("Error :"+e.getMessage());
   		}
   	}

   	public void closeClient() {
   		System.out.println("Disconnecting ....");

   		try { 
   			if (streamOut != null)  streamOut.close();
   			if (socket    != null)  socket.close();
   		}catch(IOException ioe) {
   			System.out.println("Error closing ...");
   		}
   		System.out.println("Good Bye!");
   	}
   		
   		
   	public void start() throws IOException {  
      	
   }

	public static void main(String[] args) {
		Client cl = new Client();
		Thread th = new Thread(cl);
		th.start();
	}

}