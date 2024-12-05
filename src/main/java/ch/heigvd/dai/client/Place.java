package ch.heigvd.dai.client;

import ch.heigvd.dai.gameclass.Orientation;
import ch.heigvd.dai.gameclass.ShipType;
import java.util.Scanner;

public class Place extends Instruction {

  public Place() {
    super(Message.PLACE);
  }

  @Override
  protected String[] execute(String[] arguments) {
    previous_instruction = this;
    ShipType shipType = ShipType.valueOf(arguments[0]);
    logger.request("You must place " + arguments[0]);
    Scanner scanner = new Scanner(System.in);

    logger.info("Your board\n" + board);

    while (true) {
      try {
        // read
        String[] tokens = scanner.nextLine().split("-", 0);
        char x = tokens[0].toUpperCase().charAt(0);
        int y = Integer.parseInt(tokens[1]);
        Orientation orientation = Orientation.valueOf(tokens[2].toUpperCase());

        // validate
        if (!board.place(shipType, x, y, orientation)) {
          logger.warn("Invalid placement");
          continue;
        }
        // send
        return new String[] {
              Message.PLACE.name(),
              shipType.name(),
              String.valueOf(x),
              String.valueOf(y),
              orientation.toString(),
            }
            .clone();
      } catch (Exception ignore) {
        logger.warn("Follow format : A-5-TOP/BOTTOM/RIGHT/LEFT");
      }
    }
  }
}
