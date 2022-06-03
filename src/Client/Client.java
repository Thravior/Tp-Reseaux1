package Client;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.Scanner;	//

import java.util.function.*;
import java.util.HashMap;

public class Client {

	private static Socket socket;
	private HashMap<String, BiConsumer<String, Socket>> interpreteur = new HashMap<>();

	private void mkdir(String name, Socket socket){

	}

	private void download(String name, Socket socket){

	}

	private void upload(String name, Socket socket){

	}

	private void cd(String name, Socket socket){

	}

	private void ls(String name, Socket socket){

	}


	public static void main(String[] args) throws Exception
	{
		Scanner userInput = new Scanner(System.in);

		String input = UserInputGetter.getInitialInput(userInput);

		String serverAddress = input.split("!")[0];
		int port = Integer.parseInt(input.split("!")[1]);

		socket = new Socket(serverAddress, port);
		
		System.out.format("Le serveur est sur %s:%d%n", serverAddress, port);



		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		String helloMsgDeServeur = in.readUTF();
		System.out.print(helloMsgDeServeur);
		
		socket.close();		
	}



}

