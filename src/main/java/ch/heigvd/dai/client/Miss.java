package ch.heigvd.dai.client;

public class Miss extends Instruction {
  public Miss() {
    super("MISS");
  }

  @Override
  protected String execute(String[] arguments) {
    System.out.println("MISS");
    enemyBoard.getCell(x, y).hit();
    return "";
  }
}
