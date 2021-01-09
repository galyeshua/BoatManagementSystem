package bms.application;

import bms.engine.BMSEngine;
import bms.engine.LoginHandler;
import bms.module.*;
import bms.network.Session;
import bms.network.SessionView;

import java.lang.reflect.Proxy;

import static bms.utils.InputUtils.getStringFromUser;
import static bms.utils.MenuUtils.getMainMenuForUser;

public class BMSMain {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 1989;

        try{
            host = parseHostArg(args[0]);
            port = parsePortArg(args[1]);
        } catch (Exception e){
            System.out.println("Usage: --host=HOST --port=NUMBER");
            System.out.println("Connecting to default host '" + host + "' and port " + port + ".");
        }

        new BMSMain().startClient(host, port);
    }

    private static String parseHostArg(String hostArg){
        return hostArg.split("--host=")[1];
    }

    private static int parsePortArg(String portArg){
        return Integer.parseInt(portArg.split("--port=")[1]);
    }

    private static Object inspectEngine(String host, int port) {
        return Proxy.newProxyInstance(
                //first parameter is a classLoader that - can come from any class in the code
                BMSMain.class.getClassLoader(),
                //second parameter is What the dynamic proxy is going to mimic (must be a list of interfaces)
                new Class[] {BMSEngine.class, LoginHandler.class},
                //third parameter is a class with the method that is going to be called for EVERY method
                //the dynamic proxy mimics
                new RequestCreator(host, port));
    }


    private void startClient(String host, int port) {
        Object proxy = inspectEngine(host, port);

        BMSEngine engine = (BMSEngine)proxy;
        LoginHandler loginHandler = (LoginHandler)proxy;

        while(true){
            Menu mainMenu = null;
            try{
                String email, password;
                SessionView session = null;
                boolean isValid;
                do {
                    isValid = true;

                    System.out.println("Enter your email");
                    email = getStringFromUser();
                    System.out.println("Enter your password");
                    password = getStringFromUser();

                    try {
                        session = loginHandler.createUserSession(email, password);
                        RequestCreator.updateSession(session);
                    } catch (Member.InvalidUsernameOrPasswordException e) {
                        System.out.println("Wrong email or password");
                        isValid = false;
                    } catch (Member.AlreadyLoginException e) {
                        System.out.println("User already logged in");
                        isValid = false;
                    }
                }while(!isValid);

                MemberView user = engine.getMember(session.getUserSerialNumber());

                System.out.println("Welcome " + user.getName());
                mainMenu = getMainMenuForUser(user, engine, loginHandler);
                mainMenu.startLoop();

            } catch(Session.IsExpiredException e){
                System.out.println("Your session is expired. please login again.");
                if(mainMenu != null)
                    mainMenu.stopLoop();
            } catch (Session.IsNotExistsException e){
                System.out.println("Error: session is not exist. please login again.");
                if(mainMenu != null)
                    mainMenu.stopLoop();
            }
        }
    }

}
