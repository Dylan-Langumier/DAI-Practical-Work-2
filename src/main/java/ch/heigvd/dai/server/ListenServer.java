package ch.heigvd.dai.server;

import ch.heigvd.dai.gameclass.Player;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListenServer {
  public static void run(final int PORT, final int MAX_GAMES) {
    System.out.println("[SERVEUR] : Starting");

    try (ServerSocket serverSocket = new ServerSocket(PORT);
        ExecutorService executor = Executors.newCachedThreadPool()) {
      final GameManager manager = new GameManager(MAX_GAMES, executor);
      while (true) {
        executor.submit(new Player(serverSocket.accept(), manager));
      }
    } catch (IOException e) {
      System.err.println("[SERVEUR] : " + e.getMessage());
    }
  }
}
