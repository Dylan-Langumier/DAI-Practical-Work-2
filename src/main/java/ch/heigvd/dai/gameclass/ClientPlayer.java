package ch.heigvd.dai.gameclass;

import ch.heigvd.dai.client.Instruction;

import java.io.IOException;
import java.net.Socket;

public class ClientPlayer extends BasePlayer {

    public ClientPlayer(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        System.out.println("[Client] : starting");

        // joins the server
        try {
            send("JOIN:" + name);
        } catch (IOException e) {
            System.err.println("[Client] : " + e.getMessage());
        }

        Instruction.init(board,enemyBoard);

        // start game loop
        while(!stopRequested){
            System.out.println("Your board\n" + board);
            System.out.println("Enemy board\n" + enemyBoard);

            try {
                String answer = Instruction.handle(receive());
                if(answer != null){
                    send(answer);
                }

            } catch (IOException e) {
                System.out.println("[Client] : " + e.getMessage());
            }
        }

        System.out.println("[Client] : stopping");
    }
}
