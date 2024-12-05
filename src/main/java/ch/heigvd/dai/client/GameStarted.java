package ch.heigvd.dai.client;

public class GameStarted extends Instruction {

  public GameStarted() {
    super(Message.GAME_STARTED);
  }

  @Override
  protected String[] execute(String[] arguments) {
    logger.info("Game started with %s, good luck!\n", arguments[0]);
    return null;
  }
}
