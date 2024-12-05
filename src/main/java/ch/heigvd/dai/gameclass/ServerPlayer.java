package ch.heigvd.dai.gameclass;

import ch.heigvd.dai.server.GameManager;
import ch.heigvd.dai.client.Error.ErrorMessage;
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
    logger.info("Player %s is now playing", name);
    send(Message.PLAY, x, y);
    mustPlay = true;
  }

  public void start() throws IOException {
    send(Message.PLAY);
    mustPlay = true;
  }

  public void startGameWith(ServerPlayer adversary) throws IOException {
    this.adversary = adversary;
    adversary.setEnemyBoard(board);
    send(Message.GAME_STARTED, adversary.getName());
  }

  public void endGame(boolean victory) {
    gameOver = true;
    adversary = null;
    try {
      send(Message.GAME_OVER, victory ? "WIN" : "LOSE");
    } catch (IOException ignore) {
    }
  }

  public void run() {
    logger.info("[%s@%s] : Connected", name, socket.getInetAddress());

    // wait for join
    try {
      waitForJoin();
    } catch (IOException e) {
      logger.error(e.getMessage());
      return;
    }

    while (!stopRequested) {
      // populate board
      try {
        initializeBoard();
      } catch (IOException e) {
        logger.error(e.getMessage());
      }

      // request a game with another player
      if (!manager.request(this)) {
        logger.error("[%s@%s] : Game manager refused game request", name, socket.getInetAddress());
        return;
      }
      logger.info("[%s@%s] : Game requested successfully", name, socket.getInetAddress());

      // play
      try {
        play();
      } catch (IOException e) {
        logger.error(e.getMessage());
        adversary.endGame(true);
        break;
      }
    }

    try {
      socket.close();
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
    logger.info("[%s@%s] : Disconnected", name, socket.getInetAddress());
  }

  private void play() throws IOException {
    send(Message.WAIT);
    gameOver = false;
    while (!gameOver) {
      // wait for your turn
      if (!mustPlay) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          logger.error(e.getMessage());
        }
        continue;
      }

      // play
      logger.info("Player %s must play", name);
      String[] message = receive();
      Message command = Message.valueOf(message[0]);
      logger.info("Player %s sent cmd : %s", name, String.join(DELIMITER, message));
      if (command != Message.PLAY || message.length != 3) {
        logger.error("Player must play with PLAY:<x>:<y>");
        send(Message.ERROR, ErrorMessage.FORMAT);
        continue;
      }
      char x = message[1].charAt(0);
      int y = Integer.parseInt(message[2]);
      Cell cell;
      try {
        cell = enemyBoard.getCell(x, y);
      } catch (IndexOutOfBoundsException e) {
        logger.error("Coordinates out of bounds");
        send(Message.ERROR, ErrorMessage.OUT_OF_BOUNDS);
        continue;
      }
      if (cell.isHit()) {
        send(Message.ERROR, ErrorMessage.ALREADY_HIT);
        continue;
      }
      cell.hit();
      mustPlay = false;

      send(Message.FEEDBACK, cell.hasShip() ? "HIT" : "MISS");

      if (enemyBoard.allShipsSank()) {
        logger.info("[%s@%s] : Won against %s", name, socket.getInetAddress(), adversary.getName());
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
          ShipType.CARRIER,
          ShipType.BATTLESHIP,
          ShipType.DESTROYER,
          ShipType.SUBMARINE,
          ShipType.PATROLLER
        };
    board = new Board();
    int toPlace = 0;
    while (toPlace < ships.length) {
      // tell client to place a boat
      send(Message.PLACE, ships[toPlace++].toString());

      // read answer
      String[] message = receive();
      Message command = Message.valueOf(message[0]);
      if (command != Message.PLACE || message.length != 5) {
        logger.error("Player expected to use %s:<ShipType>:<x>:<y>:<orientation>%n", Message.PLACE);
        send(Message.ERROR, ErrorMessage.FORMAT);
        continue;
      }
      try {
        ShipType shipType = ShipType.valueOf(message[1]);
        char x = message[2].charAt(0);
        int y = Integer.parseInt(message[3]);
        Orientation orientation = Orientation.valueOf(message[4]);

        if (!board.place(shipType, x, y, orientation)) {
          logger.error("Invalid placement for %s", name);
        }
      } catch (Exception e) {
        logger.error("Invalid arguments for %s", name);
      }
    }
  }

  private void waitForJoin() throws IOException {
    int attempts = 10;
    while (attempts > 0) {
      --attempts;
      String[] message = receive();
      Message command = Message.valueOf(message[0]);
      if (command != Message.JOIN || message.length != 2) {
        logger.error("Player must start with %s:<name>", Message.JOIN);
        send(Message.ERROR, ErrorMessage.STARTING_COMMAND);
        continue;
      }
      name = message[1];
      return;
    }
    throw new IOException("Too many mistakes");
  }
}
