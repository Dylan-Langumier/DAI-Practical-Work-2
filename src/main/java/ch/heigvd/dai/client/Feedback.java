package ch.heigvd.dai.client;

import ch.heigvd.dai.gameclass.Cell;
import ch.heigvd.dai.gameclass.ShipType;

public class Feedback extends Instruction {
  private enum Result {
    MISS,
    HIT
  }

  public Feedback() {
    super(Message.FEEDBACK);
  }

  @Override
  protected String[] execute(String[] arguments) {
    //    if (arguments[0].equals("HIT")) {
    //      System.out.println("HIT");
    //      enemyBoard.setCell(x, y, new Cell(ShipType.PATROLLER));
    //    } else if (arguments[0].equals("MISS")) {
    //      System.out.println("MISS");
    //    }
    //    enemyBoard.getCell(x, y).hit();
    Result result = Result.valueOf(arguments[0]);
    if (result == Result.HIT) {
      enemyBoard.setCell(x, y, new Cell(ShipType.PATROLLER));
    }
    logger.info(result.name());
    logger.info("%nEnemy board%n%s", enemyBoard);

    return null;
  }
}
