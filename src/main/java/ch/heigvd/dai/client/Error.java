package ch.heigvd.dai.client;

public class Error extends Instruction {
  public enum ErrorMessage {
    OUT_OF_BOUNDS,
    ALREADY_HIT,
    FORMAT,
    STARTING_COMMAND
  }

  public Error() {
    super(Message.ERROR);
  }

  @Override
  protected String[] execute(String[] arguments) {
    try {
      ErrorMessage errorMessage = ErrorMessage.valueOf(arguments[0]);
      switch (errorMessage) {
        case OUT_OF_BOUNDS -> logger.error("Out of bounds");
        case FORMAT -> logger.error("Wrong format");
        case STARTING_COMMAND -> logger.error("Wrong starting command");
        case ALREADY_HIT -> logger.error("Space was already hit");
      }
    } catch (IllegalArgumentException e) {
      logger.error("SERVER ERROR");
      if (previous_instruction == null)
        throw new RuntimeException("No one expects the Spanish Inquisition");
    }
    return previous_instruction.execute(arguments);
  }
}
