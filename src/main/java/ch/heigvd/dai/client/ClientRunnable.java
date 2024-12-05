package ch.heigvd.dai.client;

import ch.heigvd.dai.Logger;
import ch.heigvd.dai.gameclass.ClientPlayer;
import java.io.IOException;
import java.net.Socket;

public class ClientRunnable {
  private static final Logger logger = new Logger(ClientRunnable.class.getSimpleName());

  public static void run(final int PORT, final String HOST) {
    try (Socket socket = new Socket(HOST, PORT)) {
      logger.info("Trying to connect to host %s:%s", HOST, PORT);
      ClientPlayer player = new ClientPlayer(socket);
      player.run();
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }
}
