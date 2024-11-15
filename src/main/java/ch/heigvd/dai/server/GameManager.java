package ch.heigvd.dai.server;

import ch.heigvd.dai.gameclass.Game;
import ch.heigvd.dai.gameclass.Player;
import java.util.ArrayDeque;
import java.util.concurrent.ExecutorService;

public class GameManager extends Thread {
  private boolean stopRequested = false;
  ArrayDeque<Player> waiting = new ArrayDeque<>();
  private final Object mutex = new Object();
  ExecutorService executor;

  public GameManager(ExecutorService executor) {
    this.executor = executor;
  }

  public void requestStop() {
    stopRequested = true;
  }

  public boolean request(Player player) {
    synchronized (mutex) {
      if (waiting.contains(player)) return false;
      waiting.push(player);
    }
    return true;
  }

  public void cancel(Player player) {
    synchronized (mutex) {
      waiting.remove(player);
    }
  }

  public void run() {
    System.out.println("Starting Game manager");
    while (!stopRequested) {
      if (waiting.size() < 2) {
        try {
          sleep(500);
        } catch (InterruptedException e) {
          System.err.println("[GameManager]¨" + e.getMessage());
        }
        continue;
      }
      synchronized (mutex) {
        if (waiting.size() < 2) continue;

        executor.submit(new Game(waiting.pop(), waiting.pop()));
      }
      System.out.println("[GameManager] : new game started");
    }
    System.out.println("Game Manager stops");
  }
}
