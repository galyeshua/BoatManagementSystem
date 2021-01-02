package bms.application;

import bms.network.Request;
import bms.network.Response;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

public class RequestCreator implements InvocationHandler {
    private String host;
    private int port;

    public RequestCreator(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //Object obj = null;
        Response response;

        try {
            //creates a socket to the server
            Socket socket = new Socket(host, port);

            // start with sending request to server
            try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
                Request request = new Request(method.getName(), args, method.getParameterTypes());
                out.writeObject(request);
                out.flush();

                System.out.println("sent and waiting to response (" + method.getName() + ")");

                try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()))){
                    response = (Response)in.readObject();
                    System.out.println("STATUS: " + response.getStatus());
                    //System.out.println(response.getObject());

                    if(response.getStatus().equals(Response.Status.FAILED)) {
                        //throw Exception
                        socket.close();
                        throw (Throwable)response.getObject();
                    }

                }
            }

            System.out.println("finish");

            socket.close();
            System.out.println("after socket");
        } finally {
        }

        return response.getObject();
    }
}
