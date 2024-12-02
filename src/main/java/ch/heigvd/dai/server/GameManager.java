package ch.heigvd.dai.server;

import ch.heigvd.dai.gameclass.ServerPlayer;

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

  private void tryToStartGame(){
    synchronized (mutex) {
      if (waiting.size() >= 2) {
        ServerPlayer p1 = waiting.pop();
        ServerPlayer p2 = waiting.pop();
        p1.startGameWith(p2);
        p2.startGameWith(p1);
        p1.giveTurn();
      }
    }
  }

  public void cancel(ServerPlayer serverPlayer) {
    synchronized (mutex) {
      waiting.remove(serverPlayer);
    }
  }
}
