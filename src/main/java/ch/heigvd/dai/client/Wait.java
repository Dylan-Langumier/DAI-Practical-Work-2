package ch.heigvd.dai.client;

public class Wait extends Instruction {
  public Wait() {
    super(Message.WAIT);
  }

  @Override
  protected String[] execute(String[] arguments) {
    logger.info("Waiting for another player");
    return null;
  }
}
