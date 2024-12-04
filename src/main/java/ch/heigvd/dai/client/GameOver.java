package ch.heigvd.dai.client;

public class GameOver extends Instruction {

  public GameOver() {
    super("GAME_OVER");
  }

  @Override
  protected String[] execute(String[] arguments) {
    if (arguments[0].equals("WIN")) {
      System.out.println("Congrats! You won the game");
    } else if (arguments[0].equals("LOSE")) {
      System.out.println("You lost.");
    }
    return null;
  }
}
