package bms.server;

import bms.engine.Engine;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class BmsServer {
    private Engine engine;
    private UsersManager usersManager;

    public BmsServer() {
        engine = new Engine();
        usersManager = new UsersManager();
    }

    void startServer(int port) {
        engine.loadState();

        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Starting server on port " + port);

            while(true){
                Socket socket = serverSocket.accept();
                // start new thread for handle request
                new BmsServerThread(this, socket).start();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Engine getEngine() {
        return engine;
    }

    public UsersManager getUsersManager() {
        return usersManager;
    }
}
