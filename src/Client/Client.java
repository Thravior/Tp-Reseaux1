package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;	//


public class Client {

	private static Socket socket;

	private static void upload(File file) throws IOException {
//		TODO revoir car valeur statique
//		file = new File("./go.txt");
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		FileInputStream fis = new FileInputStream(file.toString());
		byte[] buffer = new byte[4096];
		int read;
		dos.writeLong(file.length());
		while ((read=fis.read(buffer)) > 0) {
			dos.write(buffer, 0, read);
		}
		fis.close();
	}

	private static void request(String instruction) {
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			System.out.println(instruction);
			out.writeUTF(instruction);
		} catch (IOException e) {
			System.out.println("Error envoi serveur: " + e);
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
			if (!(input.split(" ")[0].equals("upload")) ) {
				request(input);
			}
			else {

				upload(new File(input));
			}
		} while (!input.equals("exit"));

		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		String helloMsgDeServeur = in.readUTF();
		System.out.print(helloMsgDeServeur);
		
		socket.close();		
	}



}

