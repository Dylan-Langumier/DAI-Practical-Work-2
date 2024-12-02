package ch.heigvd.dai.gameclass;

import ch.heigvd.dai.server.GameManager;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Player implements Runnable {
  final private String DELIMITER = ":";
  private final Socket socket;
  private BufferedWriter out;
  private BufferedReader in;
  private final GameManager manager;
  private boolean stopRequested;
  private String name;

  private boolean mustPlay, hasPlayed;

  private Board board, enemyBoard;
  private Player adversary;

  public Player(Socket socket, GameManager manager) {
    this.socket = socket;
    this.manager = manager;
    try(InputStreamReader isr = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
        OutputStreamWriter osw =
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)) {
      in = new BufferedReader(isr);
      out = new BufferedWriter(osw);
    }catch (IOException e){
      System.err.println(e.getMessage());
    }
  }

  public Board getBoard(){return board;}

  public void setEnemyBoard(Board enemyBoard){this.enemyBoard = enemyBoard;}

  public String getName(){return name;}

  public void requestStop() {stopRequested = true;}

  public void giveTurn(){mustPlay = true;}

  public boolean isDone() {
    return !socket.isConnected() || socket.isClosed();
  }

  public void startGameWith(Player adversary){
    this.adversary = adversary;
  }

  public void run() {
    System.out.printf("[Player@%s] : Connected %n", socket.getInetAddress());

    while(!stopRequested){

      // wait for join
      try{
        waitForJoin();
      }catch (IOException e){
        System.err.println("[Server] : " + e.getMessage());
        break;
      }

      // populate board


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
    while(true){
      if(!mustPlay){
        try {
          Thread.sleep(100);
        }catch(InterruptedException e){
          System.err.println("[Server] : " + e.getMessage());
        }
        continue;
      }
      String[] message = in.readLine().splitWithDelimiters(DELIMITER,0);
      if(!message[0].equals("JOIN") || message.length != 3) {
        System.err.println("[Server] : player must play with PLAY:<x>:<y>");
        continue;
      }
      char x = message[1].charAt(0);
      int y = Integer.parseInt(message[2]);
      Cell cell = enemyBoard.getCell(x,y);
      if(cell.isHit()){
        out.write("ERROR");
        continue;
      }
      cell.hit();
      mustPlay = false;
      hasPlayed = true;

      if(cell.getShipType() == ShipType.NONE){
        out.write("MISS");
        continue;
      }

      out.write("HIT");

      if(enemyBoard.allShipsSank()){
        // victory
      }
    }
  }

  private void waitForJoin() throws IOException{
    int attempts = 10;
    while(attempts > 0){
      --attempts;
      String[] message = in.readLine().splitWithDelimiters(DELIMITER,0);
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
