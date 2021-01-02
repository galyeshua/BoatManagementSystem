package bms.application;

import bms.engine.BMSEngine;
import bms.module.*;

import java.io.IOException;
import java.lang.reflect.Proxy;

import static bms.utils.InputUtils.getStringFromUser;
import static bms.utils.MenuUtils.getMainMenuForUser;

import bms.xml.Convertor;

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

    private static BMSEngine inspectEngine(String host, int port) {
        return (BMSEngine) Proxy.newProxyInstance(
                //first parameter is a classLoader that - can come from any class in the code
                BMSMain.class.getClassLoader(),
                //second parameter is What the dynamic proxy is going to mimic (must be a list of interfaces)
                new Class[] {BMSEngine.class},
                //third parameter is a class with the method that is going to be called for EVERY method
                //the dynamic proxy mimics
                new RequestCreator(host, port));
    }


    private void start() {
        //BMSEngine engine = new Engine();
        BMSEngine engine = inspectEngine("localhost", 1989);

        String email, password;
        boolean isValid;
        do{
            System.out.println("Enter your email");
            email = getStringFromUser();
            System.out.println("Enter your password");
            password = getStringFromUser();

            isValid = engine.validateUserLogin(email, password);
            if(!isValid)
                System.out.println("Wrong email or password");

        } while(!isValid);

        MemberView user = engine.getMember(email);
        engine.loginUser(user);

        System.out.println("Welcome " + user.getName());

        Menu mainMenu = getMainMenuForUser(user, engine);
        mainMenu.startLoop();
    }






}
