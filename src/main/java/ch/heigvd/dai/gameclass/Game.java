package ch.heigvd.dai.gameclass;

import java.io.IOException;

public class Game extends Thread {
  private Board[] boards;
  private Player[] players;
  private Integer turn;
  private final int ATTEMPTS_AMOUNT = 10;
  private final int ATTEMPT_TIMEOUT = 1000;

  public Game(Player p1, Player p2) {
    boards = new Board[] {new Board(), new Board(), new Board(), new Board()};
    players = new Player[] {p1, p2};
    turn = 0;
  }

  public void run() {
    System.out.println("[Game] : A new game has started");

    // tell both players the game has been started

    while (true) {
      // ask player to play
      try {
        players[turn].send("YOUR_TURN");
      } catch (IOException e) {
        System.err.println("[Game] : " + e.getMessage());
      }

      // waits for an answer, with a time limit
      String answer = null;
      for (int i = 0; i < ATTEMPTS_AMOUNT; ++i) {
        try {
          sleep(ATTEMPT_TIMEOUT);
        } catch (InterruptedException e) {
          System.err.println("[Game] " + e.getMessage());
        }

        answer = players[turn].poll();
        if (answer != null) break;
      }

      if (answer == null) {
        next(turn);
        continue;
      }

      if (verifyMove()) next(turn);
    }

    // System.out.println("[Server] : game ended");
  }

  private void next(Integer turn) {
    turn = (turn + 1) % 2;
  }

  private boolean verifyMove() {
    /// TODO: game logic
    return true;
  }
}
