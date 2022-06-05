package server;

import Client.UserInputGetter;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Server {
    private static ServerSocket listener;
    private static Path serverRoot;
    private static String serverAddress = "127.0.0.1";
    private static int serverPort = 5000;

    public static void main(String[] args) throws Exception{
        Scanner userInput = new Scanner(System.in);

        String userDir = System.getProperty("user.dir");
        Path rootDirectory = Paths.get(userDir, "root");

        if (Files.notExists(rootDirectory)) {
            Files.createDirectory(rootDirectory);
        }
        serverRoot = rootDirectory;
        System.out.println(serverRoot);

        // Compteur incrémenté à chaque connexien d'un client au serveur
        int clientNumber = 0;

//        String input = UserInputGetter.getInitialInput(userInput);
//
//        serverAddress = input.split("!")[0];
//        serverPort = Integer.parseInt(input.split("!")[1]);

        listener = new ServerSocket();
        listener.setReuseAddress(true);
        InetAddress serverIP = InetAddress.getByName(serverAddress);
        // Association de l'adresse et. du port à la connexien
        listener.bind(new InetSocketAddress(serverIP, serverPort));
        System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);


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
        private BufferedReader in;
        private PrintStream out;

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
                Path creationPath = currentDirectory.resolve(name);
                Files.createDirectory(creationPath);
                answer = answer + "Fichier " + name + " créé avec succès \n";
            } catch (FileAlreadyExistsException e) {
                answer = answer + "Error: directory already exists\n";
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                sendAnswer(answer);
            }
        }
        private void upload(String fileName) throws IOException {
            System.out.println("in upload");
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            FileOutputStream fos = new FileOutputStream(fileName);
            byte[] buffer = new byte[4096];
            long fileSize = dis.readLong();
            int read = 0;
            while(fileSize > 0 && (read = dis.read(buffer)) > 0) {
                fos.write(buffer, 0, read);
                fileSize -= read;
            }
            fos.close();
            out.println("Le fichier " + fileName + " a bien ete televerse");
        }

        private void ls(){
            String answer = "";
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDirectory)) {
                for (Path entry: stream) {
                    if (Files.isDirectory(entry)){
                        answer = answer + "[" + entry.getFileName() + "]\n";
                    }
                    else {
                        answer = answer + entry.getFileName() + "\n";
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            sendAnswer(answer);
        }

        private void cd(String name) {
            String answer = "";
            System.out.println(currentDirectory);
            if ("..".equals(name)) {
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
                e.printStackTrace();
            } finally { }
        }


        private void log(String[] command){
            DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime localTime = LocalTime.now();
            String now = dtfDay.format(localDate) +  " @ " + dtf.format(localTime) ;
            // TODO demander a Matias c'est quoi le probleme avec le cd car on a un caractere non reconnue
            String info = "[" + serverAddress + ":" + serverPort +"-" + now + "]:" + String.join(",", command) ;
            System.out.println(info);
        }

        public void run()
        {
            try {
                // création d'un canal sortant pour envoyer des messages au client
                String[] cmd;
//                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//                in.readUTF();

                String inputL;
                System.out.println("avant while");
                while( (inputL = in.readUTF()) != null) {
                    System.out.println("Dans while");

                    cmd = inputL.split(" ");
                    log(cmd);
                    System.out.println(cmd[0]);
                    if (cmd[0].equals("cd")){
                        System.out.println("Dans cd");
                        cd(cmd[1]);
                    }

                    if (cmd[0].equals("exit") ){
                        break;
                    }
                    if (cmd[0].equals("upload") ){
                        System.out.println("Dans upload");
                        upload(cmd[1]);
                    }
                }

                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                // Envoie d'un message au client
                out.writeUTF("Hello from server you are client#" + clientNumber);
            } catch (IOException e) {
                System.out.println("Error handling client#" + clientNumber + " : " + e);
                e.printStackTrace();
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

