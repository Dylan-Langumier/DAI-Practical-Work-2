package ch.heigvd.dai.server;

import ch.heigvd.dai.gameclass.ServerPlayer;
import java.io.IOException;
import java.util.ArrayDeque;

public class GameManager {
  ArrayDeque<ServerPlayer> waiting = new ArrayDeque<>();
  private final Object mutex = new Object();

  public GameManager() {}

  public boolean request(ServerPlayer serverPlayer) {
    synchronized (mutex) {
      if (waiting.contains(serverPlayer)) return false;
      waiting.push(serverPlayer);
    }
    tryToStartGame();
    return true;
  }

  private void tryToStartGame() {
    synchronized (mutex) {
      if (waiting.size() >= 2) {
        ServerPlayer p1 = waiting.pop(), p2 = waiting.pop();
        try {
          p1.startGameWith(p2);
          p2.startGameWith(p1);
          p1.start();
        } catch (IOException ignore) {
        }
      }
    }
  }

  public void cancel(ServerPlayer serverPlayer) {
    synchronized (mutex) {
      waiting.remove(serverPlayer);
    }
  }
}
