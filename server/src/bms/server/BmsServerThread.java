package bms.server;

import bms.engine.BMSEngine;
import bms.network.Request;
import bms.network.Response;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;

public class BmsServerThread extends Thread {
    private BMSEngine engine;
    private Class engineClass;
    private Socket socket;

    public BmsServerThread(BMSEngine engine, Class engineClass, Socket socket) {
        this.engine = engine;
        this.engineClass = engineClass;
        this.socket = socket;
    }

    @Override
    public void run(){
        System.out.println("start handle request " + Thread.currentThread().getId());
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
             ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {

            Object res;
            Response.Status status = Response.Status.OK;

            // get request
            Request request = (Request)in.readObject();
            System.out.println("method name from request: " + request.getMethodName());
            System.out.println("method args from request: " + request.getArgs());
            System.out.println("invoking: ");
            Method requiredMethod = engineClass.getMethod(request.getMethodName(), request.getTypes());

            try{
                res = requiredMethod.invoke(engine, request.getArgs());
            } catch (Exception e){
                res = e.getCause();
                status = Response.Status.FAILED;
            }

            // write response
            try{
                out.writeObject(new Response(res, status));
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        System.out.println("finish handle request " + Thread.currentThread().getId());
    }
}
