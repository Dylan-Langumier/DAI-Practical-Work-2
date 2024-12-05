package ch.heigvd.dai.client;

public class Wait extends Instruction {
  public Wait() {
    super("WAIT");
  }

  @Override
  protected String[] execute(String[] arguments) {
    System.out.println("Waiting for another player");
    return null;
  }
}
