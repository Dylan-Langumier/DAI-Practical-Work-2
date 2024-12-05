package ch.heigvd.dai.client;

public class GameOver extends Instruction {
  private enum Status {
    WIN,
    LOSE
  }

  public GameOver() {
    super(Message.GAME_OVER);
  }

  @Override
  protected String[] execute(String[] arguments) {
    Status status = Status.valueOf(arguments[0]);
    switch (status) {
      case WIN -> logger.info("Congrats! You won the game");
      case LOSE -> logger.info("You lost the game");
    }
    //    if (arguments[0].equals("WIN")) {
    //      System.out.println("Congrats! You won the game");
    //    } else if (arguments[0].equals("LOSE")) {
    //      System.out.println("You lost.");
    //    }
    return null;
  }
}
