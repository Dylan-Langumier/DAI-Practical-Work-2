package ch.heigvd.dai.gameclass;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public abstract class BasePlayer{
    protected final Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    protected boolean stopRequested;
    protected String name;

    protected boolean mustPlay = false,
            gameOver = false;
    protected Board board,
            enemyBoard;

    BasePlayer(Socket socket){
        this.socket = socket;
        try(InputStreamReader isr = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
            OutputStreamWriter osw =
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)) {
            in = new BufferedReader(isr);
            out = new BufferedWriter(osw);
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    protected void send(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }

    protected String[] receive() throws IOException{
        return in.readLine().splitWithDelimiters(":",0);
    }

    public String getName(){return name;}

    public abstract void run();
}
