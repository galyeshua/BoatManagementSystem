package bms.application;

import bms.engine.BMSEngine;
import bms.engine.Engine;


import bms.menu.BoatCommands;
import bms.menu.Menu;

import static bms.utils.ConsoleUtils.getStringFromUser;


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
//        for (Boat boat : engine.getBoats())
//            System.out.println(boat);
//
//        engine.deleteBoat(1);
//        engine.deleteBoat(2);
//        engine.deleteBoat(3);
//
//        for (Boat boat : engine.getBoats())
//            System.out.println(boat);

//
//
//        engine.addMember(1);
//        engine.addMember(2);
//        engine.addMember(3);
//
//        System.out.println(engine.getMember(1));
//
//        for (Member member : engine.getMembers())
//            System.out.println(member);
//
//        engine.deleteMember(1);
//        engine.deleteMember(2);
//        engine.deleteMember(3);
//
//        for (Member member : engine.getMembers())
//            System.out.println(member);


    }

}
