package bms.application;

import bms.engine.BMSEngine;
import bms.engine.LoginHandler;
import bms.module.*;

import java.lang.reflect.Proxy;

import static bms.utils.InputUtils.getStringFromUser;
import static bms.utils.MenuUtils.getMainMenuForUser;

public class BMSMain {

    public static void main(String[] args) {
//        Exception e = new Member.NotFoundException();
//        Class c = e.getClass();
//
//        System.out.println(c);
//        System.out.println(c.getName());
//
//        try {
//            throw (Exception)c.newInstance();
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//

//        System.out.println(Convertor.stringFromXmlFilePath("C:\\Users\\tqn863\\IdeaProjects\\BoatManagementSystem\\database.xml"));
//
//        try {
//            Convertor.saveXmlFromString(Convertor.stringFromXmlFilePath("C:\\Users\\tqn863\\IdeaProjects\\BoatManagementSystem\\database.xml"), "C:\\Users\\tqn863\\IdeaProjects\\BoatManagementSystem\\database2.xml");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        new BMSMain().start();
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

//    private static BMSEngine inspectEngine(String host, int port) {
//        return (BMSEngine) Proxy.newProxyInstance(
//                //first parameter is a classLoader that - can come from any class in the code
//                BMSMain.class.getClassLoader(),
//                //second parameter is What the dynamic proxy is going to mimic (must be a list of interfaces)
//                new Class[] {BMSEngine.class, LoginHandler.class},
//                //third parameter is a class with the method that is going to be called for EVERY method
//                //the dynamic proxy mimics
//                new RequestCreator(host, port));
//    }


    private void start() {
        //BMSEngine engine = new Engine();
        Object proxy = inspectEngine("localhost", 1989);

        BMSEngine engine = (BMSEngine)proxy;
        LoginHandler loginHandler = (LoginHandler)proxy;


        String email, password;
//        boolean isValid;
//        do{
//            System.out.println("Enter your email");
//            email = getStringFromUser();
//            System.out.println("Enter your password");
//            password = getStringFromUser();
//
//            isValid = engine.validateUserLogin(email, password);
//            if(!isValid)
//                System.out.println("Wrong email or password");
//
//        } while(!isValid);

        boolean isValid = true;
        MemberView user = null;
        Integer sessionID = null;
        do {

            while(user == null){
                System.out.println("Enter your email");
                email = getStringFromUser();
                System.out.println("Enter your password");
                password = getStringFromUser();

                user = engine.getMember(email, password);

                if(user == null)
                    System.out.println("Wrong email or password");
            }

            try {
                sessionID = loginHandler.createSessionForUser(user);
                isValid = true;
                RequestCreator.updateSession(user, sessionID);
            } catch (Member.AlreadyLoginException e) {
                System.out.println("User already login.");
                isValid = false;
                user = null;
            }

        }while(!isValid);


        System.out.println("Welcome " + user.getName());

        Menu mainMenu = getMainMenuForUser(user, engine);
        mainMenu.startLoop();
    }






}
