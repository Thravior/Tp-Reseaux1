package test;

import java.io.DataInputStream;
import java.net.Socket;


public class Client {

	private static Socket socket;
	
	
	public static void main(String[] args) throws Exception
	{
		String serverAddress = "127.0.0.1";
		int port = 5001;
		Scanner userInput = new Scanner(System.in);

		String input = getInitialInput();

		while(!validateInitialInput()){

		}
		
		socket = new Socket(serverAddress, port);
		
		System.out.format("Le serveur est sur %s:%d%n", serverAddress, port);
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		String helloMsgDeServeur = in.readUTF();
		System.out.print(helloMsgDeServeur);
		
		socket.close();		
	}
	private static boolean validateIPAddress(String input){
		String[] parts = input.split(".");
		if (parts.length != 4){ //
			return false;
		}
		for( String part : parts){
			if (!isNumeral(part)){
				return false;
			}
			int value = Integer.parseInt(part);
			if (value < 0 || value > 255) {
				return false;
			}
		}

		return true;
	}

	private static boolean isNumeral(String string){
		int intValue;

		if (string == null || string .equals("")){
			return false;
		}

		try {
			int intValue = Integer.parseInt(string);
			return true;
		} catch(NumberFormatException e) { }
		return false;
	}
}

