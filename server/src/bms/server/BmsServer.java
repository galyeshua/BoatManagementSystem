package bms.server;

import bms.engine.BMSEngine;
import bms.engine.Engine;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BmsServer {
    public static void main(String[] args) {
        int port = 1989;
        if (args == null || args.length == 0)
            System.out.println("Default port " + port + " will be used.");
        else
            port = Integer.parseInt(args[0]);

        new BmsServer().startServer(port);
    }

    private void startServer(int port) {
        Engine engine = new Engine();
        engine.loadState();
        Class engineClass = engine.getClass();

        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Starting server on port " + port);

            while(true){
                Socket socket = serverSocket.accept();

                // start new thread for handle request
                new BmsServerThread(engine, engineClass, socket).start();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
