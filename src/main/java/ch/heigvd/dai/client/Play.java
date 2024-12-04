package ch.heigvd.dai.client;

import java.util.Scanner;

public class Play extends Instruction {
  public Play() {
    super("PLAY");
  }

  @Override
  protected String execute(String[] arguments) {
    previous_instruction = this;
    if (arguments.length == 3) {
      char hitX = arguments[0].charAt(0);
      int hitY = Integer.parseInt(arguments[1]);
      board.getCell(hitX, hitY).hit();
    }

    System.out.println("Your board\n" + board);
    System.out.println("Enemy board\n" + enemyBoard);

    Scanner scanner = new Scanner(System.in);
    while (true) {
      try {
        System.out.println("Choose coordinates to attack (X-n)");
        String[] tokens = scanner.nextLine().split("-", 2);
        x = tokens[0].charAt(0);
        y = Integer.parseInt(tokens[1]);
        return "PLAY:" + x + ":" + y;
      } catch (Exception ignore) {
        System.out.println("Follow format : A-5");
      }
    }
  }
}
