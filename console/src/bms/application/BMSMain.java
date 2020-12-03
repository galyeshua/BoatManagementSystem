package bms.application;

import bms.engine.BMSEngine;
import bms.engine.Engine;
import bms.module.Boat;
import bms.module.BoatView;
import bms.module.Member;

import java.time.LocalDate;
import java.time.LocalTime;


import static bms.utils.menu.MenuUtils.getMainMenuForUser;


public class BMSMain {

    public static void main(String[] args) {
        new BMSMain().start();
    }

    private void start() {
        BMSEngine engine = new Engine();


        engine.addMember(123, "Gal", 23, "PRO", Member.Level.BEGINNER,
                LocalDate.of(2020, 12, 20), LocalDate.of(2021, 12, 20),
                false, 0, "054-5454545", "gal@gmail.com",
                "1234", true);


        engine.addMember(1276, "Maya", 23, "PRO", Member.Level.MODERATE,
                LocalDate.of(2020, 11, 20), LocalDate.of(2021, 11, 20),
                false, 0, "054-5454545", "maya@gmail.com",
                "1234", true);


        engine.addBoat(1,"dsfsf", Boat.Rowers.ONE, Boat.Paddles.DOUBLE, true, true,
                false, true, true );

        engine.addBoat(2,"hjyjuyj", Boat.Rowers.EIGHT, Boat.Paddles.DOUBLE, false, true,
                true, true, true );

        engine.addActivity("sdffs", LocalTime.of(6, 50), LocalTime.of(7, 50));

        // load user

        Menu mainMenu = getMainMenuForUser(true, engine);
        mainMenu.startLoop();

    }

}
