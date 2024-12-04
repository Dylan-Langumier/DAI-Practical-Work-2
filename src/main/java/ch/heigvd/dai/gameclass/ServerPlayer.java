package ch.heigvd.dai.gameclass;

import ch.heigvd.dai.server.GameManager;
import java.io.*;
import java.net.Socket;

public class ServerPlayer extends BasePlayer implements Runnable {
  private final GameManager manager;

  private ServerPlayer adversary;

  public ServerPlayer(Socket socket, GameManager manager) {
    super(socket);
    this.manager = manager;
  }

  public void setEnemyBoard(Board enemyBoard) {
    this.enemyBoard = enemyBoard;
  }

  public void giveTurn(char x, int y) throws IOException {
    System.out.printf("Player %s is now playing\n", name);
    send("PLAY", String.valueOf(x), String.valueOf(y));
    mustPlay = true;
  }

  public void start() throws IOException {
    send("PLAY");
    mustPlay = true;
  }

  public void startGameWith(ServerPlayer adversary) throws IOException {
    this.adversary = adversary;
    adversary.setEnemyBoard(board);
    send("GAME_STARTED:" +  adversary.getName());
  }

  public void endGame(boolean victory) {
    gameOver = true;
    adversary = null;
    String argument;
    if(victory)
      argument = "WIN";
    else
      argument = "LOSE";
    try {
      send("GAME_OVER", argument);
    } catch (IOException ignore) {
    }
  }

  public void run() {
    System.out.printf("[Player@%s] : Connected %n", socket.getInetAddress());

    // wait for join
    try {
      waitForJoin();
    } catch (IOException e) {
      System.err.println("[Server] : " + e.getMessage());
      return;
    }

    while (!stopRequested) {
      // populate board
      try {
        initializeBoard();
      } catch (IOException e) {
        System.err.println("[Server] : " + e.getMessage());
      }

      // request a game with another player
      if (!manager.request(this)) {
        System.err.printf(
            "[%s@%s] : Game manager refused game request%n", name, socket.getInetAddress());
        return;
      }
      System.out.printf("[%s@%s] : Game requested successfully%n", name, socket.getInetAddress());

      // play
      try {
        play();
      } catch (IOException e) {
        System.err.printf("[Server] : %s%n", e.getMessage());
        adversary.endGame(true);
        break;
      }
    }

    try {
      socket.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    System.out.printf("[%s@%s] : Disconnected %n", name, socket.getInetAddress());
  }

  private void play() throws IOException {
    gameOver = false;
    while (!gameOver) {
      // wait for your turn
      if (!mustPlay) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          System.err.printf("[Server] : %s%n", e.getMessage());
        }
        continue;
      }

      // play
      System.out.printf("Player %s must play%n", name);
      String[] message = receive();
      System.out.printf(
          "Player %s sent cmd : %s x : %s y : %s%n", name, message[0], message[1], message[2]);
      if (!message[0].equals("PLAY") || message.length != 3) {
        System.err.println("[Server] : player must play with PLAY:<x>:<y>");
        send("ERROR");
        continue;
      }
      char x = message[1].charAt(0);
      int y = Integer.parseInt(message[2]);
      Cell cell;
      try {
        cell = enemyBoard.getCell(x, y);
      } catch (IndexOutOfBoundsException e) {
        System.err.println("Coordinates out of bound");
        send("ERROR", "OUT_OF_BOUND");
        continue;
      }
      if (cell.isHit()) {
        send("ERROR", "ALREADY_HIT");
        continue;
      }
      cell.hit();
      mustPlay = false;

      if (cell.getShipType() == ShipType.NONE) {
        send("FEEDBACK", "MISS");
      } else {
        send("FEEDBACK", "HIT");
      }

      if (enemyBoard.allShipsSank()) {
        System.out.printf("[%s@%s] : Won against %s", name, socket.getInetAddress(),adversary.getName());
        adversary.endGame(false);
        endGame(true);
      } else {
        adversary.giveTurn(x, y);
      }
    }
  }

  private void initializeBoard() throws IOException {
    final ShipType[] ships =
        new ShipType[] {
          // ShipType.CARRIER,
          // ShipType.BATTLESHIP,
          // ShipType.DESTROYER,
          // ShipType.SUBMARINE,
          ShipType.PATROLLER
        };
    board = new Board();
    int toPlace = 0;
    while (toPlace < ships.length) {
      // tell client to place a boat
      send("PLACE", ships[toPlace++].toString());

      // read answer
      String[] message = receive();
      if (!message[0].equals("PLACE") || message.length != 5) {
        System.err.println(
            "[Server] : player expected to use PLACE:<ShipType>:<x>:<y>:<orientation>");
        send("ERROR");
        continue;
      }
      try {
        ShipType shipType = ShipType.valueOf(message[1]);
        char x = message[2].charAt(0);
        int y = Integer.parseInt(message[3]);
        Orientation orientation = Orientation.valueOf(message[4]);

        if (!board.place(shipType, x, y, orientation)) {
          System.err.println("[Server] : Invalid placement");
        }
      } catch (Exception e) {
        System.err.println("[Server] : Invalid arguments");
      }
    }
  }

  private void waitForJoin() throws IOException {
    int attempts = 10;
    while (attempts > 0) {
      --attempts;
      String[] message = receive();
      if (!message[0].equals("JOIN") || message.length != 2) {
        System.err.println("[Server] : player must start with JOIN:<name>");
        send("ERROR");
        continue;
      }
      name = message[1];
      return;
    }
    throw new IOException("Too many mistakes");
  }
}
