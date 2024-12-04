package ch.heigvd.dai.gameclass;

import ch.heigvd.dai.client.Instruction;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientPlayer extends BasePlayer {

  public ClientPlayer(Socket socket) {
    super(socket);
  }

  @Override
  public void run() {
    System.out.println("[Client] : starting");

    System.out.println("Choose a name");
    Scanner in = new Scanner(System.in);
    name = in.nextLine();

    // joins the server
    try {
      send("JOIN", name);
    } catch (IOException e) {
      System.err.println("[Client] : " + e.getMessage());
    }

    board = new Board();
    enemyBoard = new Board();
    Instruction.init(board, enemyBoard);

    // start game loop
    while (!stopRequested) {
      try {
        String[] answer = Instruction.handle(receive());
        if (answer != null) {
          send(answer);
        }

      } catch (IOException e) {
        System.out.println("[Client] : " + e.getMessage());
        break;
      }
    }

    System.out.println("[Client] : stopping");
  }
}
