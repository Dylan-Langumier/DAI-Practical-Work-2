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
    Result result = Result.valueOf(arguments[0]);
    if (result == Result.HIT) {
      enemyBoard.setCell(x, y, new Cell(ShipType.PATROLLER));
    }
    enemyBoard.getCell(x, y).hit();
    logger.info(result.name());
    logger.info("%nEnemy board%n%s", enemyBoard);

    return null;
  }
}
