package ch.heigvd.dai.client;

import ch.heigvd.dai.gameclass.Cell;
import ch.heigvd.dai.gameclass.ShipType;

public class Hit extends Instruction {
  public Hit() {
    super("HIT");
  }

  @Override
  protected String execute(String[] arguments) {
    System.out.println("HIT");
    enemyBoard.setCell(x, y, new Cell(ShipType.PATROLER));
    enemyBoard.getCell(x, y).hit();

    System.out.println("Enemy board\n" + enemyBoard);

    return null;
  }
}
