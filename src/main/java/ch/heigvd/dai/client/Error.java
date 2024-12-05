package ch.heigvd.dai.client;

public class Error extends Instruction {

  public Error() {
    super(Message.ERROR);
  }

  @Override
  protected String[] execute(String[] arguments) {
    logger.error("SERVER ERROR");
    if (previous_instruction == null)
      throw new RuntimeException("No one expects the Spanish Inquisition");
    return previous_instruction.execute(arguments);
  }
}
