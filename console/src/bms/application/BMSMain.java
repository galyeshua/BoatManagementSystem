package bms.application;

import bms.engine.BMSEngine;
import bms.engine.Engine;


import bms.menu.BoatCommands;
import bms.menu.Menu;
import bms.module.Activity;
import bms.module.Boat;
import bms.module.Member;

import java.time.LocalDateTime;


public class BMSMain {

    public static void main(String[] args) {
        new BMSMain().start();
    }

    private void start(){
        BMSEngine engine = new Engine();

        // currentUser
        // menu class


        Menu mainMenu = new Menu("Boat House Main Menu");

        mainMenu.addOption("New reservation", new BoatCommands.PrintBoats());
        mainMenu.addOption("Show reservations", new BoatCommands.PrintBoats());
        mainMenu.addOption("Reservation history", new BoatCommands.PrintBoats());
        mainMenu.addOption("Edit my profile", new BoatCommands.PrintBoats());
        mainMenu.addOption("Manage", new BoatCommands.openSubMenu());
        mainMenu.addOption("Exit", new BoatCommands.exitApp());

        mainMenu.startLoop();


//        engine.addBoat(1);
//        engine.addBoat(2);
//        engine.addBoat(3);
//
//        System.out.println(engine.getBoat(1).print());
//

//
//        Menu m = new Menu();
//
//        while(true) { // while not exit
//            m.show();
//
//            m.execute(getNumber());
//        }


        engine.addBoat( "Gal", "2X", true, false, true, false);
        System.out.println(engine.getBoat(0).print());


        engine.updateBoat(0,"iris",null,null,null,null,null,null);
        System.out.println(engine.getBoat(0).print());

       engine.addMember("meshi", 22,null, Member.Level.PROFESIONAL, LocalDateTime.now(),LocalDateTime.now(),true,1,"054-9999999",
                null,"nee",true);

       System.out.println(engine.getMember(0));

//        for (Member member : engine.getMembers())
//            System.out.println(member);
//       engine.deleteMember(0);
//       for (Member member : engine.getMembers())
//           System.out.println(member);

        engine.updateMember(0,"maya",null,null,null,null,null,null,null,null,null,null);
        System.out.println(engine.getMember(0));


        engine.addActivity("diving",LocalDateTime.now(),LocalDateTime.now(),"wide");
        System.out.println(engine.getActivity(0));

        engine.updateActivity(0,"runing",null,null,null);
        System.out.println(engine.getActivity(0));

    }

}
