package ch.heigvd.dai.client;

import java.util.Scanner;

public class Play extends Instruction {
  public Play() {
    super(Message.PLAY);
  }

  @Override
  protected String[] execute(String[] arguments) {
    previous_instruction = this;
    if (arguments.length == 2) {
      char hitX = arguments[0].charAt(0);
      int hitY = Integer.parseInt(arguments[1]);
      board.getCell(hitX, hitY).hit();

      if (!board.getCell(hitX, hitY).hasShip()) {
        logger.info("The enemy missed! (%c,%d)\n", hitX, hitY);
      } else {
        logger.warn("The enemy hit one of your ships at (%c,%d)\n", hitX, hitY);
      }
    }

    logger.info("Your board\n" + board);
    logger.info("Enemy board\n" + enemyBoard);

    Scanner scanner = new Scanner(System.in);
    while (true) {
      try {
        logger.request("Choose coordinates to attack (X-n)");
        String[] tokens = scanner.nextLine().split("-", 2);
        x = tokens[0].charAt(0);
        y = Integer.parseInt(tokens[1]);
        board.getCell(x, y);
        return new String[] {Message.PLAY.name(), String.valueOf(x), String.valueOf(y)};
      } catch (IndexOutOfBoundsException e) {
        logger.warn("Out of bound");
      } catch (Exception e) {
        logger.warn("Follow format : A-5");
      }
    }
  }
}
