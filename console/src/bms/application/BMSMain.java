package bms.application;

import bms.engine.BMSEngine;
import bms.engine.Engine;
import bms.module.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


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

        engine.addMember(3243, "Gal", 25, "PRO", Member.Level.BEGINNER,
                LocalDate.of(2020, 12, 20), LocalDate.of(2021, 12, 20),
                false, 0, "054-5454545", "gal3@gmail.com",
                "1234", true);


        engine.addMember(1276, "Maya", 23, "PRO", Member.Level.MODERATE,
                LocalDate.of(2020, 11, 20), LocalDate.of(2021, 11, 20),
                false, 0, "054-5454545", "maya@gmail.com",
                "1234", false);


        engine.addBoat(1,"dsfsf", Boat.Rowers.ONE, Boat.Paddles.DOUBLE, true, true,
                false, true, true );

        engine.addBoat(2,"hjyjuyj", Boat.Rowers.EIGHT, Boat.Paddles.DOUBLE, false, true,
                true, true, true );

        engine.addActivity("a1", LocalTime.of(5, 50), LocalTime.of(6, 50));
        engine.addActivity("a2", LocalTime.of(7, 40), LocalTime.of(9, 30));
        engine.addActivity("a2", LocalTime.of(10, 40), LocalTime.of(11, 30));

//        ActivityView a1 = engine.getActivity(0);
//        ActivityView a2 = engine.getActivity(1);
//
//        System.out.println(a1.isOverlapping(a2));


        List<Integer> memlist1 = new ArrayList<Integer>();
        memlist1.add(123);

        List<Integer> memlist2 = new ArrayList<Integer>();
        memlist2.add(123);

        List<Integer> memlist3 = new ArrayList<Integer>();
        memlist3.add(123);

        List<Integer> memlist4 = new ArrayList<Integer>();
        memlist4.add(1276);

        Reservation r1 = new Reservation(new Activity(LocalTime.of(5, 50), LocalTime.of(6, 50)),
                LocalDate.of(2020, 12, 9), LocalDateTime.now(), 123);
        r1.addBoatType(BoatView.Rowers.FOUR);
        r1.addParticipant(123);
        engine.addReservation(r1);

        Reservation r2 = new Reservation(new Activity(LocalTime.of(7, 00), LocalTime.of(9, 50)),
                LocalDate.of(2020, 12, 9), LocalDateTime.now(), 123);
        r2.addBoatType(BoatView.Rowers.FOUR);
        r2.addParticipant(123);
        engine.addReservation(r2);

        Reservation r3 = new Reservation(new Activity(LocalTime.of(5, 50), LocalTime.of(6, 50)),
                LocalDate.of(2020, 12, 10), LocalDateTime.now(), 123);
        r3.addBoatType(BoatView.Rowers.FOUR);
        r3.addParticipant(123);
        engine.addReservation(r3);

        // load user
        // log in...

        MemberView user = engine.getMember(123);
        engine.setCurrentUser(user);

        Menu mainMenu = getMainMenuForUser(user, engine);
        mainMenu.startLoop();

    }

}
