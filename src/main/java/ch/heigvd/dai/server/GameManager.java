package ch.heigvd.dai.server;

import ch.heigvd.dai.gameclass.ServerPlayer;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Random;

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
        ServerPlayer[] players = new ServerPlayer[]{waiting.pop(), waiting.pop()};
        try {
          players[0].startGameWith(players[1]);
          players[1].startGameWith(players[0]);
          Random random = new Random();
          players[random.nextInt(players.length)].start();
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
