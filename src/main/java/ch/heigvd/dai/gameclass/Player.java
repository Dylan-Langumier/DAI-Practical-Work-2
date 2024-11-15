package ch.heigvd.dai.gameclass;

import ch.heigvd.dai.server.GameManager;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Player extends Thread {
  private final Socket socket;
  private BufferedWriter out;
  private BufferedReader in;
  private final GameManager manager;
  private boolean stopRequested;

  private boolean hasAnswered;
  private String answer;

  private final Object outMutex = new Object();

  public Player(Socket socket, GameManager manager) throws IOException {
    this.socket = socket;
    this.manager = manager;
    InputStreamReader isr = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
    OutputStreamWriter osw =
        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
    in = new BufferedReader(isr);
    out = new BufferedWriter(osw);
    start();
  }

  public void requestStop() {
    stopRequested = true;
  }

  public boolean isDone() {
    return !socket.isConnected() || socket.isClosed();
  }

  void send(String message) throws IOException {
    synchronized (outMutex) {
      out.write(message);
    }
  }

  String poll() {
    if (!hasAnswered) return null;
    return answer;
  }

  public void run() {
    System.out.printf("[Player@%s] : Connected %n", socket.getInetAddress());

    if (!manager.request(this)) {
      System.err.printf(
          "[Player@%s] : Game manager refused game request %n", socket.getInetAddress());
      return;
    }

    while (!stopRequested) {
      hasAnswered = false;
      try {
        answer = in.readLine();
      } catch (IOException e) {
        System.err.println("[Player] : " + e.getMessage());
      }
      hasAnswered = true;
    }

    try {
      socket.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    System.out.printf("[Player@%s] : Disconnected %n", socket.getInetAddress());
  }
}
