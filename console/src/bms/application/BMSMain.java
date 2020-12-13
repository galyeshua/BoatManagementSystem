package bms.application;

import bms.engine.BMSEngine;
import bms.engine.Engine;
import bms.engine.list.manager.Exceptions;
import bms.module.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


import static bms.utils.InputUtils.getStringFromUser;
import static bms.utils.menu.MenuUtils.getMainMenuForUser;


public class BMSMain {

    public static void main(String[] args) {
        new BMSMain().start();
    }

    private void start() {
        BMSEngine engine = new Engine();

        try {
            engine.loadState();
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
        }

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
