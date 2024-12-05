package ch.heigvd.dai.server;

import ch.heigvd.dai.Logger;
import ch.heigvd.dai.gameclass.ServerPlayer;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class ListenServer {
  private static final Logger logger = new Logger(ListenServer.class.getSimpleName());

  public static void run(final int PORT, final int MAX_GAMES) {
    logger.info("Starting");

    try (ServerSocket serverSocket = new ServerSocket(PORT);
        ExecutorService executor = Executors.newFixedThreadPool(MAX_GAMES * 2)) {
      final GameManager manager = new GameManager();
      logger.info("Listening on port %d, Allowing up to %d simultaneous games", PORT, MAX_GAMES);
      while (serverSocket.isBound()) {
        try {
          executor.submit(new ServerPlayer(serverSocket.accept(), manager));
        } catch (RejectedExecutionException e) {
          logger.error("Already hosting maximum amount of games");
        }
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }
}
