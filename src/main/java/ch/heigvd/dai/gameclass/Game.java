package ch.heigvd.dai.gameclass;

import java.io.IOException;

public class Game extends Thread {

  final private Player[] players;
  private int turn;
  private final int ATTEMPTS_AMOUNT = 30;
  private final int ATTEMPT_TIMEOUT = 1000;

  private boolean gameOver = false;

  public Game(Player p1, Player p2) {
    players = new Player[] {p1, p2};
    turn = 0;
  }

  public void run() {
    System.out.println("[Game] : A new game has started");

    while(!gameOver){

      players[turn].giveTurn();

      for(int attempts = 0; attempts < ATTEMPTS_AMOUNT; ++attempts){
        try {
          Thread.sleep(ATTEMPT_TIMEOUT);
        } catch(InterruptedException e){
          System.out.println("[Server] : " + e.getMessage());
        }

        //players[turn]

        ++attempts;
      }

      turn = next(turn);
    }

    System.out.println("[Server] : game ended");
  }

  private int next(int turn) {
    return (turn + 1) % 2;
  }
}
