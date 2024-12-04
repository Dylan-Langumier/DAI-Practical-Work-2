package ch.heigvd.dai.client;

public class Hit extends Instruction {
  public Hit() {
    super("HIT");
  }

  @Override
  protected String execute(String[] arguments) {
    System.out.println("HIT");
    enemyBoard.getCell(x, y).hit();
    return "";
  }
}
