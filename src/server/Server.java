package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private static ServerSocket listener;

    public static void main(String[] args) throws Exception {


        // Someteur incrémenté à shaque connexien d'un client au serveun
        int clientNumber = 0;
        // Adresse et port du serveun
        String serverAddress = "127.0.0.1";
        int serverPort = 5000;
// créatian de la sonnexian pour communiquer aves les clients
        listener = new ServerSocket();
        listener.setReuseAddress(true);
        InetAddress serverIP = InetAddress.getByName(serverAddress);
// Association de l'adresse et. du port à la connexien
        listener.bind(new InetSocketAddress(serverIP, serverPort));
        System.out.format("The server is running on %:%d%n", serverAddress, serverPort);
        try {
            while (true) {
// Important : la fonstion accept() est blaquante : attend qu'un proshain client se connecte
// Une nouvetle connection : on incémente le compteur clientNumber
    new ClientHandler(listener.accept(), clientNumber++).start();
}
} finally {
        listener.close();
        }
}
    private static class ClientHandler extends Thread {
        private Socket socket;
        private int clientNumber;

        public ClientHandler(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            System.out.println("New connection with client#" + clientNumber
                    + " at " + socket);
        }

        /*
        s:
        Une thread se charge d'envoyer au client un message de bienvenue
        */
        public void run() {
            try {
// création d'un canal sontant pour envover des messages au client
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
// Envoie d'un message au client
                out.writeUTF("Hello from server you are client#" + clientNumber);
            } catch (IOException e) {
                System.out.println("Error handling client#" + clientNumber + ": " + e);
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