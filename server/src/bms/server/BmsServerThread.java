package bms.server;

import bms.engine.Engine;
import bms.engine.LoginHandler;
import bms.network.Request;
import bms.network.Response;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;

public class BmsServerThread extends Thread {
    private Socket socket;
    private BmsServer server;

    public BmsServerThread(BmsServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }


    @Override
    public void run(){
        System.out.println("start handle request " + Thread.currentThread().getId());
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
             ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {

            Object resultOfMethod;
            Response.Status status = Response.Status.OK;
            Request request = (Request)in.readObject();
            Engine engine = server.getEngine();
            UsersManager usersManager = server.getUsersManager();
            Method requiredMethod;
            Object requiredObject;

            System.out.println("method name from request: " + request.getMethodName());

            System.out.println("users:");
            for(Session s : usersManager.getSessions())
                System.out.println(s.user.getName());

            boolean isLoginHandlerClass = request.getClassName().equals(LoginHandler.class.getSimpleName());

            if (isLoginHandlerClass){
                requiredObject = usersManager;
                requiredMethod = usersManager.getClass().getMethod(request.getMethodName(), request.getTypes());
            } else {
                if (request.getSessionID() != null)
                    usersManager.getSession(request.getSessionID()).updateExpired();
                requiredObject = engine;
                requiredMethod = engine.getClass().getMethod(request.getMethodName(), request.getTypes());
            }


            try{
                resultOfMethod = requiredMethod.invoke(requiredObject, request.getArgs());

//                if(request.getSessionID() == null)
//                    if(request.getUser() != null)
//                        System.out.println("need to set session id");

            } catch (Exception e){
                resultOfMethod = e.getCause();
                status = Response.Status.FAILED;
            }

            try{
                out.writeObject(new Response(resultOfMethod, status));
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        System.out.println("finish handle request " + Thread.currentThread().getId());
    }
}
