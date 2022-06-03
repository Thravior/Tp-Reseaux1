package server;

import Client.UserInputGetter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.util.Scanner;

public class Server {
    private static ServerSocket listener;
    private static Path serverRoot;


    public static void main(String[] args) throws Exception{
        Scanner userInput = new Scanner(System.in);

        Path rootDirectory = Paths.get("", "/root");
        if (Files.notExists(rootDirectory)) {
            Files.createDirectory(rootDirectory);
        }
        serverRoot = Paths.get("root");


        // Compteur incrémenté à chaque connexien d'un client au serveur
        int clientNumber = 0;

        String input = UserInputGetter.getInitialInput(userInput);

        {
            String serverAddress = input.split("!")[0];
            int serverPort = Integer.parseInt(input.split("!")[1]);

            listener = new ServerSocket();
            listener.setReuseAddress(true);
            InetAddress serverIP = InetAddress.getByName(serverAddress);
            // Association de l'adresse et. du port à la connexien
            listener.bind(new InetSocketAddress(serverIP, serverPort));
            System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);
        }


        try {
            while (true) {
                // Important : la fonction accept() est bloquante : attend qu'un prochain client se connecte
                // Une nouvelle connection : on incémente le compteur clientNumber
                new ClientHandler(listener.accept(), clientNumber++, serverRoot).start();
            }
        }
        finally
        {
        listener.close();
        }
    }
    private static class ClientHandler extends Thread {
        private Socket socket;
        private int clientNumber;
        private Path currentDirectory;

        public ClientHandler(Socket socket, int clientNumber, Path rootDirectory) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            currentDirectory = rootDirectory;
            System.out.println("New connection with client#" + clientNumber + " at " + socket);
        }


    //Une thread se charge d'envoyer au client un message de bienvenue

        private void mkdir(String name) {
            String answer = "";
            try {
                name = "/" + name;
                Path creationPath = Paths.get(currentDirectory.toString(), name);
                Files.createDirectory(creationPath);
            } catch (FileAlreadyExistsException e) {
                answer = answer + "Error: directory already exists";
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                sendAnswer(answer);
            }
        }

        private void cd(String name) {
            String answer = "";
            if (name == "..") {
                if(!currentDirectory.endsWith("root")){
                    currentDirectory = currentDirectory.getParent();
                    answer = answer + "it worked";
                }
                else {
                    answer = answer + "Error: already at root directory.";
                }
            }
            else {
                try {
                    currentDirectory = currentDirectory.resolve(name);
                    answer = answer + "Currently on directory: " + name;
                } catch (InvalidPathException e) {
                    answer = answer + "Error: Could not resolve path";
                }
                finally {

                }
            }
            sendAnswer(answer);
        }

        private void sendAnswer(String answer){
            try {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                out.writeUTF(answer);
            } catch (IOException e) {
                System.out.println("Error handling client#" + clientNumber + " : " + e);
            } finally {
                try {
                    socket.close();
                }
                catch(IOException e){
                    System.out.println("Couldn't close a socket, what's going on?");
                }
                System.out.println("Connection with client# " + clientNumber + "closed");
            }
        }



        public void run()
        {
            try {
                // création d'un canal sortant pour envoyer des messages au client
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                // Envoie d'un message au client
                out.writeUTF("Hello from server you are client#" + clientNumber);
            } catch (IOException e) {
                System.out.println("Error handling client#" + clientNumber + " : " + e);
            } finally {
                try {
                // fermetuce de la sonnexien aves le client
                    socket.close();
                }
                catch(IOException e){
                    System.out.println("Couldn't close a socket, what's going on?");
                }
                System.out.println("Connection with client# " + clientNumber + "closed");
            }
        }
    }

}

