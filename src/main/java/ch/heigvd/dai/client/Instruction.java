package ch.heigvd.dai.client;

import ch.heigvd.dai.gameclass.Board;
import java.util.HashMap;

public abstract class Instruction {
    final static private HashMap<String,Instruction> strToInstr = HashMap.newHashMap(6);
    static boolean lock = false;
    static protected Board board, enemyBoard;
    static Instruction previous_instruction = null;

    public static String handle(String[] message){
        String[] arguments = new String[message.length - 1];
        System.arraycopy(message, 1, arguments, 0, arguments.length);
        return strToInstr.get(message[0]).execute(arguments);
    }

    public Instruction(String key){
        strToInstr.put(key,this);
    }

    protected abstract String execute(String[] arguments);

    static public void init(Board board, Board enemyBoard){
        Instruction.board = board;
        Instruction.enemyBoard = enemyBoard;
        if(lock)
            return;
        lock = true;
        new Place();
        new Play();
        new Win();
        new Lose();
        new Error();
    }


}
