package test;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.Scanner;	//
import java.util.regex;		//

public class Client {

	private static Socket socket;
	
	
	public static void main(String[] args) throws Exception
	{
		Scanner userInput = new Scanner(System.in);

		String input = getInitialInput();

		String serverAddress = input.split[0];
		int port = input.split("!")[1];

		socket = new Socket(serverAddress, port);
		
		System.out.format("Le serveur est sur %s:%d%n", serverAddress, port);
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		String helloMsgDeServeur = in.readUTF();
		System.out.print(helloMsgDeServeur);
		
		socket.close();		
	}

	private static String getInitialInput(Scanner input){
		String userInput1;
		do {
			System.out.printIn("Entrez l'addresse IP du serveur");
			userInput1 = input.nextLine();
		} while (!validateIPAddress(userInput1))

		String userInput2;
		do {
			System.out.printIn("Entrez le port d'écoute du serveur");
			userInput2 = input.nextLine();
		} while(!validatePort(userInput2))

		return userInput1 + "!" + userInput2;
	}

	private static boolean validateIPAddress(String input){
		String[] parts = input.split(".");
		boolean valide = true;
		if (parts.length != 4){ //
			valide= false;
		}

		for( String part : parts){
			if (valide = false){
				break
			}
			if (!isNumeral(part)){
				valide =  false;
			}
			int value = Integer.parseInt(part);
			if (value < 0 || value > 255) {
				valide = false;
			}
		}

		if (!valide){
			System.out.printIn("Format d'addresse IP invalide");
		}

		return valide;
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

	static boolean validateCommand(String string){
		for (String command : commandsRegex){
			if (string.matches(command)){
				return true;
			}
		}
		return false;
	}

	String[] commandsRegex = {
			"^ls$",
			"^exit$",
			"^cd\s.+",
			"^mkdir\s.+",
			"^upload\s.+",
			"^download\s.+"
	}

}
