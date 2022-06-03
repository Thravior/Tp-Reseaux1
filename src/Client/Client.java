package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;	//


public class Client {

	private static Socket socket;

	private static void upload(String instruction){

	}

	private static void request(String instruction) {
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			System.out.println(instruction);
			out.writeUTF(instruction);
		} catch (IOException e) {
			System.out.println("Error envoi serveur: " + e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Couldn't close a socket, what's going on?");
			}
		}
	}


	public static void main(String[] args) throws Exception
	{
		Scanner userInput = new Scanner(System.in);


		String input = UserInputGetter.getInitialInput(userInput);

		{
			String serverAddress = input.split("!")[0];
			int port = Integer.parseInt(input.split("!")[1]);

			socket = new Socket(serverAddress, port);
			System.out.format("Le serveur est sur %s:%d%n", serverAddress, port);
		}

		do {
			input = UserInputGetter.getWorkingInput(userInput);
			if (!(input.split(" ")[0] == "upload") ) {
				request(input);
			}
			else {
				upload(input);
			}
		} while (input != "exit");

		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		String helloMsgDeServeur = in.readUTF();
		System.out.print(helloMsgDeServeur);
		
		socket.close();		
	}



}

