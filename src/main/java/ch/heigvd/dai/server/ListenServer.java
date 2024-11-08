package ch.heigvd.dai.server;

import ch.heigvd.dai.gameclass.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

public class ListenServer {
    private static LinkedList<Player> players = new LinkedList<>();
    public static void run(final int PORT, final int MAX_GAMES){
        System.out.println("[SERVEUR] : Starting");

        final GameManager manager = new GameManager(MAX_GAMES);

        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            players.add(new Player(serverSocket.accept(),manager));
        }catch (IOException e){
            System.err.println("[SERVEUR] : " + e.getMessage());
        }

        players.removeIf(Player::isDone);
    }
}
