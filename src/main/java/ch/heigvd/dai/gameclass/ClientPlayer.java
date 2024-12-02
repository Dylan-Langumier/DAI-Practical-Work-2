package ch.heigvd.dai.gameclass;

import java.io.IOException;
import java.net.Socket;

public class ClientPlayer extends BasePlayer {

    public ClientPlayer(Socket socket) {
        super(socket);
    }

    public Board getBoard(){return board;}
    public Board getEnemyBoard(){return enemyBoard;}

    @Override
    public void run() {
        System.out.println("[Client] : starting");

        // joins the server
        try {
            send("JOIN:" + name);
        } catch (IOException e) {
            System.err.println("[Client] : " + e.getMessage());
        }

        // start game loop
        while(!stopRequested){
            try {
                String[] message = receive();

            } catch (IOException e) {
                System.out.println("[Client] : " + e.getMessage());
            }
        }

        System.out.println("[Client] : stopping");
    }
}
