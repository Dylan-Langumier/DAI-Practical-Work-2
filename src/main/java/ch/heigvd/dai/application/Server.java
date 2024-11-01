package ch.heigvd.dai.application;

import java.io.*;
import java.net.ServerSocket;

public class Server {

    public void run(final int PORT){
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(PORT);
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

}
