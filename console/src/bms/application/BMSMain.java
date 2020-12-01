package bms.application;

import bms.engine.BMSEngine;
import bms.engine.Engine;
import bms.engine.list.manager.Exceptions;
import bms.module.Activity;
import bms.module.Boat;
import bms.module.Member;

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

        engine.addMember(123, "Maya", 23, "PRO", Member.Level.MODERATE,
                LocalDate.of(2020, 11, 20), LocalDate.of(2021, 11, 20),
                false, 0, "054-5454545", "maya@gmail.com",
                "1234", true);



        //System.out.println(test.values()[1]);;

        // load user

        Menu mainMenu = getMainMenuForUser(true, engine);
        mainMenu.startLoop();

//        System.out.println(Boat.getCounter());
//
//        engine.addBoat( "Gal", "2X", true, false, true, false);
//        engine.addBoat( "Gal", "2X", true, false, true, false);
//        //System.out.println(engine.getBoat(0).print());
//
//        System.out.println(Boat.getCounter());
//
//        Boat.setCounter(10);
//

//
//
//        System.out.println(Boat.getCounter());
////
////        engine.updateBoat(0,"iris",null,null,null,null,null,null);
////        System.out.println(engine.getBoat(0).print());
////
////       engine.addMember("meshi", 22,null, Member.Level.PROFESIONAL, LocalDateTime.now(),LocalDateTime.now(),true,1,"054-9999999",
////                null,"nee",true);
////
////       System.out.println(engine.getMember(0));
////
//////        for (Member member : engine.getMembers())
//////            System.out.println(member);
//////       engine.deleteMember(0);
//////       for (Member member : engine.getMembers())
//////           System.out.println(member);
////
////        engine.updateMember(0,"maya",null,null,null,null,null,null,null,null,null,null);
////        System.out.println(engine.getMember(0));
////
////
////        engine.addActivity("diving",LocalDateTime.now(),LocalDateTime.now(),"wide");
////        System.out.println(engine.getActivity(0));
////
////        engine.updateActivity(0,"runing",null,null,null);
////        System.out.println(engine.getActivity(0));

    }

}
