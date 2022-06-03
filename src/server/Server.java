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

    public static void main(String(] angs) throws Exception {


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
    new ClientHandler(Listener.accept(), clientNumber++).start();
}
} finally {
        listener.close();
        }
}

}