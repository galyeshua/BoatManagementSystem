package bms.application;

import bms.module.MemberView;
import bms.network.Request;
import bms.network.Response;
import bms.network.SessionView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.Socket;

public class RequestCreator implements InvocationHandler {
    private String host;
    private int port;
    private static Integer userSerialNumber = null;
    private static Integer sessionID = null;

    public RequestCreator(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Response response = null;

        try {
            //creates a socket to the server
            Socket socket = new Socket(host, port);

            // start with sending request to server
            try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
                Request request = new Request(method, args, sessionID, userSerialNumber);
                out.writeObject(request);
                out.flush();

                //System.out.println("sent and waiting to response (" + method.getName() + ")");

                try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()))){
                    response = (Response)in.readObject();
                    //System.out.println("STATUS: " + response.getStatus());

                    if(response.getStatus().equals(Response.Status.FAILED)) {
                        socket.close();
                        throw (Throwable)response.getObject();
                    }
                }
            }

            //System.out.println("finish");

            socket.close();
            //System.out.println("after socket");
        } catch (ConnectException e){
            System.out.println("Connection error: Couldn't connect to " + host + ":" + port);
            System.exit(1);
        } finally {
        }

        return response.getObject();
    }

    public static void updateSession(SessionView session) {
        RequestCreator.userSerialNumber = session.getUserSerialNumber();
        RequestCreator.sessionID = session.getSessionID();
    }

}
