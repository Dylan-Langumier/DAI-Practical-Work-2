package ch.heigvd.dai.gameclass;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public abstract class BasePlayer {
  private static final String DELIMITER = ":";
  protected final Socket socket;
  private BufferedWriter out;
  private BufferedReader in;
  protected boolean stopRequested;
  protected String name = "Player";

  protected boolean mustPlay = false, gameOver = false;
  protected Board board, enemyBoard;

  BasePlayer(Socket socket) {
    this.socket = socket;
    try {
      in =
          new BufferedReader(
              new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
      out =
          new BufferedWriter(
              new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  protected void send(String... message) throws IOException {
    if (message == null) {
      throw new IllegalArgumentException("Message cannot be null");
    }
    out.write(String.join(DELIMITER, message));
    out.newLine();
    out.flush();
  }

  protected String[] receive() throws IOException {
    return in.readLine().split(DELIMITER, 0);
  }

  public String getName() {
    return name;
  }

  public abstract void run();
}
