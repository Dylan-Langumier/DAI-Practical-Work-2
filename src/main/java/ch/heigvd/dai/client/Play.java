package ch.heigvd.dai.client;

import ch.heigvd.dai.gameclass.Orientation;

import java.util.Scanner;

public class Play extends Instruction{
    public Play(){
        super("PLAY");
    }

    @Override
    protected String execute(String[] arguments) {
        char hitX = arguments[0].charAt(0);
        int hitY = Integer.parseInt(arguments[1]);
        Scanner scanner = new Scanner(System.in);
        while(true){
            try{
                String[] tokens = scanner.nextLine().splitWithDelimiters("-",2);
                char x = tokens[0].charAt(0);
                int y = Integer.parseInt(tokens[1]);
                return "PLAY:" + x + ":" + y;
            }catch(Exception ignore){
                System.out.println("Follow format : A-5");
            }
        }
    }
}
