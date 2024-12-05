package ch.heigvd.dai.client;

import java.io.IOException;
import java.net.Socket;

public class ClientRunnable {
  public static void run(final int PORT, final String HOST) {
    try (Socket socket = new Socket(HOST, PORT)) {
      System.out.println("Trying to conect to host " + HOST + " at port " + PORT);
      while (true)
        ;
    } catch (IOException e) {
      System.err.println("[Client] : " + e.getMessage());
    }
  }
}
