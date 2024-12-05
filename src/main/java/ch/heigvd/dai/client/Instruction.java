package ch.heigvd.dai.client;

import ch.heigvd.dai.gameclass.Board;
import java.util.HashMap;

public abstract class Instruction {
  private static final HashMap<String, Instruction> strToInstr = HashMap.newHashMap(6);
  static boolean lock = false;
  protected static Board board, enemyBoard;
  static Instruction previous_instruction = null;
  static char x;
  static int y;

  public static String[] handle(String[] message) {
    String[] arguments = new String[message.length - 1];
    System.arraycopy(message, 1, arguments, 0, arguments.length);
    return strToInstr.get(message[0]).execute(arguments);
  }

  public Instruction(String key) {
    strToInstr.put(key, this);
  }

  protected abstract String[] execute(String[] arguments);

  public static void init(Board board, Board enemyBoard) {
    Instruction.board = board;
    Instruction.enemyBoard = enemyBoard;
    if (lock) return;
    lock = true;
    new Place();
    new GameStarted();
    new Play();
    new Feedback();
    new GameOver();
    new Error();
    new Wait();
  }
}
