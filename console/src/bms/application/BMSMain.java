package bms.application;

import bms.engine.BMSEngine;
import bms.engine.Engine;
import bms.module.*;
import static bms.utils.InputUtils.getStringFromUser;
import static bms.utils.MenuUtils.getMainMenuForUser;


public class BMSMain {

    public static void main(String[] args) {
        new BMSMain().start();
    }

    private void start() {
        BMSEngine engine = new Engine();

        try {
            engine.loadState();
        } catch (Member.IllegalValueException e) {
            e.printStackTrace();
        } catch (Member.AlreadyExistsException e) {
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
