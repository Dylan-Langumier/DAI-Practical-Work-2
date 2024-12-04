package ch.heigvd.dai.client;

import ch.heigvd.dai.gameclass.ClientPlayer;
import java.io.IOException;
import java.net.Socket;

public class ClientRunnable {
  public static void run(final int PORT, final String HOST) {
    try (Socket socket = new Socket(HOST, PORT)) {
      System.out.println("Trying to connect to host " + HOST + " at port " + PORT);
      ClientPlayer player = new ClientPlayer(socket);
      player.run();
    } catch (IOException e) {
      System.err.println("[Client] : " + e.getMessage());
    }
  }
}
