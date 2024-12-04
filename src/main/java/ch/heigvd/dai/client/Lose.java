package ch.heigvd.dai.client;

public class Lose extends Instruction {

  public Lose() {
    super("LOSE");
  }

  @Override
  protected String execute(String[] arguments) {
    System.out.println("You lost.");
    return null;
  }
}
