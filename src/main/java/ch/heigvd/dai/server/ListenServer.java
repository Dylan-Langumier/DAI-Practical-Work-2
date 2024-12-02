package ch.heigvd.dai.server;

import ch.heigvd.dai.gameclass.ServerPlayer;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListenServer {
  public static void run(final int PORT, final int MAX_GAMES) {
    System.out.println("[SERVEUR] : Starting");

    try (ServerSocket serverSocket = new ServerSocket(PORT);
        ExecutorService executor = Executors.newFixedThreadPool(MAX_GAMES * 3)) {
      final GameManager manager = new GameManager();
      while (true) {
        executor.submit(new ServerPlayer(serverSocket.accept(), manager));
      }
    } catch (IOException e) {
      System.err.println("[SERVEUR] : " + e.getMessage());
    }
  }
}
