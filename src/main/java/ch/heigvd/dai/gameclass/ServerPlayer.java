package ch.heigvd.dai.gameclass;

import ch.heigvd.dai.server.GameManager;
import java.io.*;
import java.net.Socket;

public class ServerPlayer extends BasePlayer {
  private final GameManager manager;

  private ServerPlayer adversary;

  public ServerPlayer(Socket socket, GameManager manager) {
    super(socket);
    this.manager = manager;
  }

  public void setEnemyBoard(Board enemyBoard){this.enemyBoard = enemyBoard;}

  public void requestStop() {stopRequested = true;}

  public void giveTurn(){mustPlay = true;}

  public void startGameWith(ServerPlayer adversary){
    this.adversary = adversary;
    adversary.setEnemyBoard(board);
  }

  public void endGame(){
    gameOver = true;
    System.out.printf("%s sank all your ships and won the game. You suck.", adversary.getName());
    adversary = null;
  }

  public void run() {
    System.out.printf("[Player@%s] : Connected %n", socket.getInetAddress());

    // wait for join
    try{
      waitForJoin();
    }catch (IOException e){
      System.err.println("[Server] : " + e.getMessage());
      return;
    }

    while(!stopRequested){
      // populate board
      try{
        initializeBoard();
      }catch (IOException e){
        System.err.println("[Server] : " + e.getMessage());
      }

      // request a game with another player
      if (!manager.request(this)) {
        System.err.printf(
                "[Player@%s] : Game manager refused game request \n", socket.getInetAddress());
        return;
      }
      System.out.printf("[Player@%s] : Game requested successfully \n", socket.getInetAddress());

      // play
      try{
        play();
      }catch (IOException e){
        System.err.println("[Server] : " + e.getMessage());
      }
    }

    try {
      socket.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    System.out.printf("[Player@%s] : Disconnected %n", socket.getInetAddress());
  }

  private void play() throws IOException{
    gameOver = false;
    while(!gameOver){
      // wait for your turn
      if(!mustPlay){
        try {
          Thread.sleep(100);
        }catch(InterruptedException e){
          System.err.println("[Server] : " + e.getMessage());
        }
        continue;
      }

      // asks client to play
      send("YOUR_TURN");
      
      // read answer
      String[] message = receive();
      if(!message[0].equals("JOIN") || message.length != 3) {
        System.err.println("[Server] : player must play with PLAY:<x>:<y>");
        continue;
      }
      char x = message[1].charAt(0);
      int y = Integer.parseInt(message[2]);
      Cell cell = enemyBoard.getCell(x,y);
      if(cell.isHit()){
        send("ERROR");
        continue;
      }
      cell.hit();
      mustPlay = false;

      if(cell.getShipType() == ShipType.NONE){
        send("MISS");
        continue;
      }

      send("HIT");

      if(enemyBoard.allShipsSank()){
        System.out.printf("You beat %s, well played",adversary.getName());
        adversary.endGame();
        gameOver = true;
        adversary = null;
      }else{
        adversary.giveTurn();
      }
    }
  }

  private void initializeBoard() throws IOException{
    final ShipType[] ships = new ShipType[]{ShipType.CARRIER,ShipType.BATTLESHIP,ShipType.DESTROYER,ShipType.SUBMARINE,ShipType.PATROLER};
    board = new Board();
    int toPlace = 0;
    while(toPlace < ships.length){
      // tell client to place a boat
      send("PLACE:" + ships[toPlace++]);
      

      // read answer
      String[] message = receive();
      if(!message[0].equals("PLACE") || message.length != 5) {
        System.err.println("[Server] : player expected to use PLACE:<ShipType>:<x>:<y>:<orientation>");
        continue;
      }
      try {
        ShipType shipType = ShipType.valueOf(message[1]);
        char x = message[2].charAt(0);
        int y = Integer.parseInt(message[3]);
        ORIENTATION orientation = ORIENTATION.valueOf(message[4]);

        if(!board.place(shipType,x,y,orientation)){
          System.err.println("[Server] : Invalid placement");
        }
      }catch (Exception e){
        System.err.println("[Server] : Invalid arguments");
      }
    }

  }

  private void waitForJoin() throws IOException{
    int attempts = 10;
    while(attempts > 0){
      --attempts;
      String[] message = receive();
      if(!message[0].equals("JOIN") || message.length != 2) {
        System.err.println("[Server] : player must start with JOIN:<name>");
        continue;
      }
      name = message[1];
      return;
    }
    throw new IOException("Too many mistakes");
  }
}