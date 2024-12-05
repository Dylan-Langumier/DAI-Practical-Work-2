package ch.heigvd.dai.gameclass;

import ch.heigvd.dai.Logger;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public abstract class BasePlayer {
  protected enum Message {
    PLAY,
    ERROR,
    GAME_STARTED,
    FEEDBACK,
    PLACE,
    JOIN,
    GAME_OVER,
    WAIT
  }

  protected static final String DELIMITER = ":";
  protected final Socket socket;
  private BufferedWriter out;
  private BufferedReader in;
  protected boolean stopRequested;
  protected String name = "Player";
  protected final Logger logger;

  protected boolean mustPlay = false, gameOver = false;
  protected Board board, enemyBoard;

  BasePlayer(Socket socket) {
    this.logger = new Logger(getClass().getSimpleName());
    this.socket = socket;
    try {
      in =
          new BufferedReader(
              new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
      out =
          new BufferedWriter(
              new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  protected void send(Message command, String... message) throws IOException {
    out.write(command.name());
    if (message != null) out.write(DELIMITER + String.join(DELIMITER, message));
    out.newLine();
    out.flush();
  }

  protected void send(Message command, Object... message) throws IOException {
    send(command, Arrays.toString(message));
  }

  protected void send(String... message) throws IOException {
    String[] arguments = new String[message.length - 1];
    System.arraycopy(message, 1, arguments, 0, arguments.length);
    send(Message.valueOf(message[0]), arguments);
  }

  protected String[] receive() throws IOException {
    String line = in.readLine();
    if (line == null) {
      throw new IOException("Connection lost");
    }
    return line.split(DELIMITER, 0);
  }

  public String getName() {
    return name;
  }

  public abstract void run();
}
