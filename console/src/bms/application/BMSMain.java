package bms.application;

import bms.engine.BMSEngine;
import bms.engine.Engine;
import bms.module.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


import static bms.utils.menu.MenuUtils.getMainMenuForUser;


public class BMSMain {

    public static void main(String[] args) {
        new BMSMain().start();
    }

    private void start() {
        BMSEngine engine = new Engine();


        engine.addBoat(new Boat(1,"dsfsf", BoatView.BoatType.SINGLE) );

        Member member1 = new Member(123, "Gal", "gal@gmail.com", "1234");
        member1.setManager(true);
        member1.setHasPrivateBoat(true);
        member1.setBoatSerialNumber(1);
        engine.addMember(member1);

        Member member2 = new Member(1276, "Maya", "Maya@gmail.com", "1235");
        engine.addMember(member2);


        engine.addBoat(new Boat(2,"hjyjuyj", Boat.Rowers.TWO, Boat.Paddles.DOUBLE, false, true,
                false, true, false ));

        Activity ac3 = new Activity("a2", LocalTime.of(10, 40), LocalTime.of(11, 30));
        ac3.setBoatType(BoatView.BoatType.COXED_FOUR);

        engine.addActivity(new Activity("Rowing at 6", LocalTime.of(6, 00), LocalTime.of(7, 30)));
        engine.addActivity(new Activity("a2", LocalTime.of(7, 40), LocalTime.of(9, 30)));
        engine.addActivity(ac3);

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
                LocalDate.of(2020, 12, 11), LocalDateTime.now(), 123);
        r1.addBoatType(BoatView.Rowers.ONE);
        r1.addParticipant(123);
        engine.addReservation(r1);

        Reservation r2 = new Reservation(new Activity(LocalTime.of(7, 00), LocalTime.of(9, 50)),
                LocalDate.of(2020, 12, 12), LocalDateTime.now(), 123);
        r2.addBoatType(BoatView.Rowers.FOUR);
        r2.addParticipant(123);
        //r2.setAllocatedBoatID(1);
        engine.addReservation(r2);

        Reservation r3 = new Reservation(new Activity(LocalTime.of(6, 50), LocalTime.of(7, 50)),
                LocalDate.of(2020, 12, 12), LocalDateTime.now(), 123);
        r3.addBoatType(BoatView.Rowers.FOUR);
        r3.addParticipant(1276);
        engine.addReservation(r3);

//        for(BoatView boat : engine.getAvailableBoats()){
//            System.out.println(boat);
//        }

//        for(BoatView boat : engine.getAvailableBoats(LocalDate.of(2020, 12, 13), new Activity(LocalTime.of(6, 50), LocalTime.of(7, 50)))){
//            System.out.println(boat);
//        }

        // load user
        // log in...


        //engine.loadActivitiesFromFile("C:\\javatest\\activities.xml");
        //System.out.println(engine.getXmlImportErrors().size());

        //engine.saveActivitiesToFile("C:\\javatest\\activities2.xml");


        MemberView user = engine.getMember(123);
        engine.setCurrentUser(user);

        Menu mainMenu = getMainMenuForUser(user, engine);
        mainMenu.startLoop();

    }

}
