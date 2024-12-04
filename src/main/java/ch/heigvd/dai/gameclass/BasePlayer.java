package ch.heigvd.dai.gameclass;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public abstract class BasePlayer {
  protected final Socket socket;
  private BufferedWriter out;
  private BufferedReader in;
  protected boolean stopRequested;
  protected String name;

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

  protected void send(String message) throws IOException {
    out.write(message);
    out.newLine();
    out.flush();
  }

  protected String[] receive() throws IOException {
    String line = in.readLine();
    if (line == null) {
      throw new IOException("Lost connection");
    }
    return line.split(":", 0);
  }

  public String getName() {
    return name;
  }

  public abstract void run();
}
