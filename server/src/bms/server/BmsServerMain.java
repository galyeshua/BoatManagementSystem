package bms.server;

public class BmsServerMain {
    public static void main(String[] args) {
        int port = 1989;
        if (args == null || args.length == 0)
            System.out.println("Default port " + port + " will be used.");
        else
            port = Integer.parseInt(args[0]);

        new BmsServer().startServer(port);
    }
}
