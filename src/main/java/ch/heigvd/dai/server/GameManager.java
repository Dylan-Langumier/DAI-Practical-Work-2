package ch.heigvd.dai.server;

import ch.heigvd.dai.gameclass.Player;
import ch.heigvd.dai.gameclass.Game;

import java.util.ArrayDeque;
import java.util.Queue;

public class GameManager extends Thread{
    private int maxGames;
    private boolean stopRequested = false;
    ArrayDeque<Player> waiting = new ArrayDeque<>();
    final private Object mutex = new Object();

    public GameManager(final int MAX_GAMES){
        maxGames = MAX_GAMES;
        start();
    }

    public void requestStop(){stopRequested = true;}

    public boolean request(Player player){
        synchronized (mutex){
            if(waiting.contains(player))
                return false;
            waiting.push(player);
        }
        return true;
    }

    public void cancel(Player player){
        synchronized (mutex) {
            waiting.remove(player);
        }
    }

    public void run(){
        System.out.println("Starting Game manager");
        while(! stopRequested){
            if(waiting.size() < 2){
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    System.err.println("[GameManager]¨" + e.getMessage());
                }
                continue;
            }
            synchronized (mutex){
                if(waiting.size() < 2)
                    continue;

                Game newGame = new Game(waiting.pop(),waiting.pop());
            }
            System.out.println("[GameManager] : new game started");
        }
        System.out.println("Game Manager stops");
    }

}
