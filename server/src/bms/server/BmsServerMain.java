package bms.server;

import java.util.Arrays;

public class BmsServerMain {
    public static void main(String[] args) {
        int port = 1989;

        try{
            port = parsePortArg(args[0]);
        } catch (Exception e){
            System.out.println("Usage: --port=NUMBER");
            System.out.println("Default port " + port + " will be used.");
        }

        new BmsServer().startServer(port);
    }

    private static int parsePortArg(String portArg){
        return Integer.parseInt(portArg.split("--port=")[1]);
    }

}
