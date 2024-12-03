package ch.heigvd.dai.client;

import ch.heigvd.dai.gameclass.Orientation;
import ch.heigvd.dai.gameclass.ShipType;

import java.util.Scanner;

public class Place extends Instruction{

    public Place(){
        super("PLACE");
    }

    @Override
    protected String execute(String[] arguments) {
        ShipType shipType = ShipType.valueOf(arguments[0]);
        System.out.println("You must place " + arguments[0]);
        Scanner scanner = new Scanner(System.in);
        while(true){
            try{
                String[] tokens = scanner.nextLine().splitWithDelimiters("-",0);
                char x = tokens[0].charAt(0);
                int y = Integer.parseInt(tokens[1]);
                Orientation orientation = Orientation.valueOf(tokens[2]);
                return "PLACE:" + arguments[0] + ":" + x + ":" + y + ":" + orientation;
            }catch(Exception ignore){
                System.out.println("Follow format : A-5-TOP/BOTTOM/RIGHT/LEFT");
            }
        }
    }
}
