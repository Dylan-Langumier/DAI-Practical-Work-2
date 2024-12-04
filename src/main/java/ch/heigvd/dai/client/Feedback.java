package ch.heigvd.dai.client;

import ch.heigvd.dai.gameclass.Cell;
import ch.heigvd.dai.gameclass.ShipType;

public class Feedback extends Instruction {

  public Feedback() {
    super("FEEDBACK");
  }

  @Override
  protected String[] execute(String[] arguments) {
    if (arguments[0].equals("HIT")) {
      System.out.println("HIT");
      enemyBoard.setCell(x, y, new Cell(ShipType.PATROLLER));
    } else if (arguments[0].equals("MISS")) {
      System.out.println("MISS");
    }
    enemyBoard.getCell(x, y).hit();

    System.out.println("Enemy board\n" + enemyBoard);

    return null;
  }
}
