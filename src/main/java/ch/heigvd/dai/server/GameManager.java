package ch.heigvd.dai.server;

import ch.heigvd.dai.gameclass.Player;
import java.util.ArrayDeque;
import java.util.concurrent.ExecutorService;

public class GameManager {
  ArrayDeque<Player> waiting = new ArrayDeque<>();
  private final Object mutex = new Object();

  public GameManager() {}

  public boolean request(Player player) {
    synchronized (mutex) {
      if (waiting.contains(player)) return false;
      waiting.push(player);
    }
    tryToStartGame();
    return true;
  }

  private void tryToStartGame(){
    synchronized (mutex) {
      if (waiting.size() >= 2) {
        Player p1 = waiting.pop();
        Player p2 = waiting.pop();
        p1.startGameWith(p2);
        p2.startGameWith(p1);
        p1.giveTurn();
      }
    }
  }

  public void cancel(Player player) {
    synchronized (mutex) {
      waiting.remove(player);
    }
  }
}
