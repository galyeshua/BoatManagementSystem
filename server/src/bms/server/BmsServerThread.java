package bms.server;

import bms.engine.Engine;
import bms.engine.LoginHandler;
import bms.network.Request;
import bms.network.Response;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class BmsServerThread extends Thread {
    private Socket socket;
    private BmsServer server;
    private Engine engine;
    private UsersManager usersManager;

    public BmsServerThread(BmsServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.engine = server.getEngine();
        this.usersManager = server.getUsersManager();
    }

    @Override
    public void run(){
        boolean userMustBeLoggedIn = true;
        Object result;
        Method method;
        Object objectForInvoke = engine;

        System.out.println("\nStart handle request (Thread ID " + Thread.currentThread().getId() + ")");

        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
             ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {

            Request request = (Request)in.readObject();

            boolean isLoginHandlerClass = request.getClassName().equals(LoginHandler.class.getSimpleName());
            if (isLoginHandlerClass){
                objectForInvoke = usersManager;
                userMustBeLoggedIn = false;
            }

            method = objectForInvoke.getClass().getMethod(request.getMethodName(), request.getTypes());
            Response.Status status = Response.Status.OK;

            try{
                if(userMustBeLoggedIn){
                    usersManager.updateSessionExpiredTime(request.getUserSerialNumber(), request.getSessionID());
                    engine.setCurrentUser(request.getUserSerialNumber());
                }
                result = method.invoke(objectForInvoke, request.getArgs());

            } catch (InvocationTargetException e){
                result = e.getCause();
                status = Response.Status.FAILED;
            } catch (Exception e){
                result = e;
                status = Response.Status.FAILED;
            }

            System.out.println("Request method: " + request.getMethodName() + ", Status: " + status +
                            " (Session " + request.getSessionID() + ")");

            out.writeObject(new Response(result, status));
            out.flush();

            usersManager.printMembers();

        } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        System.out.println("Finish handle request (Thread ID " + Thread.currentThread().getId() + ")" + "\n");
    }

}
