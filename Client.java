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

	private static boolean validatePort(String string){
		if (!isNumeral(string)){
			System.out.printIn("Format de port invalide: pas un nombre");
			return false;
		}

		int port = Integer.parseInt();
		boolean valide = (port >=5000 || port <= 5050);

		if (!valide){
			System.out.printIn("Port invalide: Ports disponibles seulement de 5000 a 5050");
		}
		return valide;
	}

	private static boolean isNumeral(String string){
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

