package ch.heigvd.dai.server;

import ch.heigvd.dai.gameclass.ServerPlayer;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class ListenServer {
  public static void run(final int PORT, final int MAX_GAMES) {
    System.out.println("[SERVER] : Starting");

    try (ServerSocket serverSocket = new ServerSocket(PORT);
        ExecutorService executor = Executors.newFixedThreadPool(MAX_GAMES * 2)) {
      final GameManager manager = new GameManager();
      System.out.printf(
          "[SERVER] : Listening on port %d, Allowing up to %d simultaneous games\n",
          PORT, MAX_GAMES);
      while (true) {
        try {
          executor.submit(new ServerPlayer(serverSocket.accept(), manager));
        } catch (RejectedExecutionException e) {
          System.out.println("[SERVER] : Already hosting maximum amount of games");
        }
      }
    } catch (IOException e) {
      System.err.println("[SERVER] : " + e.getMessage());
    }
  }
}
