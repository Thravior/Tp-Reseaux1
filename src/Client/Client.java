package Client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Client {

	private static Socket socket;

	private static void upload(String instruction) throws IOException {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;

		Path fileSource = Paths.get(System.getProperty("user.dir"));
		String[] sections = instruction.split(" ");

		fileSource = fileSource.resolve(sections[1]);

		if (Files.exists(fileSource)) {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

			long filesSize = Files.size(fileSource);
			System.out.println(filesSize);

			dos.writeUTF(instruction + " " + (filesSize + 1000) );
			DataInputStream in = new DataInputStream(socket.getInputStream());

			try {

				File myFile = new File (fileSource.toString());
				byte [] mybytearray  = new byte [(int)myFile.length()];
				fis = new FileInputStream(myFile);
				bis = new BufferedInputStream(fis);

				bis.read(mybytearray,0,mybytearray.length);
				os = socket.getOutputStream();

				System.out.println("Sending " + fileSource + "(" + mybytearray.length + " bytes)");
				os.write(mybytearray,0,mybytearray.length);

				os.flush();

			}            finally {
				if (bis != null) bis.close();
				if (os != null) os.close();
			}
		}
		else {
			System.out.println("No file with this name in directory");
		}
	}

	private static void request(String instruction) {
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			System.out.println(instruction);
			out.writeUTF(instruction);
		} catch (IOException e) {
			System.out.println("Error received from server: " + e);
		}
	}

	private static void download(String instruction) {
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			System.out.println(instruction);
			out.writeUTF(instruction);

		} catch (IOException e) {
			System.out.println("Error while sending command to server: " + e);
		}
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());

			String response = in.readUTF();

			Path fileDestination = Paths.get(System.getProperty("user.dir"));
			fileDestination = fileDestination.resolve(instruction.split(" ")[1]);


			long fileSize = Long.decode(response);

			FileOutputStream fos = null;
			BufferedOutputStream bos = null;

			try {
				int bytesRead;
				int current = 0;

				System.out.println(fileDestination);

				InputStream is = socket.getInputStream();
				byte [] mybytearray  = new byte [(int)fileSize];
				fos = new FileOutputStream(fileDestination.toString());
				bos = new BufferedOutputStream(fos);
				bytesRead = is.read(mybytearray,0,mybytearray.length);
				current = bytesRead;

				do {
					bytesRead =
							is.read(mybytearray, current, (mybytearray.length-current));
					if(bytesRead >= 0) current += bytesRead;
				} while(bytesRead > -1);

				bos.write(mybytearray, 0 , current);
				bos.flush();
			}
			finally {
				if (fos != null) fos.close();
				if (bos != null) bos.close();
			}

		} catch (IOException e) {
			System.out.println("Error received from server: " + e);
		}

	}

	public static void main(String[] args) throws Exception
	{
		Scanner userInput = new Scanner(System.in);

		String input = UserInputGetter.getInitialInput(userInput);

		String serverAddress = input.split("!")[0];
		int port = Integer.parseInt(input.split("!")[1]);

		socket = new Socket(serverAddress, port);

		System.out.format("The server is running on %s:%d%n", serverAddress, port);

		DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

		do {
			input = UserInputGetter.getWorkingInput(userInput);
			String command = input.split("0")[0];
			if (command.equals("upload") ) {
				upload(input);
			}
			else if (command.equals("download")) {
				download(input);
			}
			else {
				request(input);
			}

			String serverAnswer = in.readUTF();
			System.out.print(serverAnswer);
		} while (!input.equals("exit"));

		socket.close();	}
}

