package bms.application;

import bms.engine.BMSEngine;
import bms.engine.Engine;


import bms.menu.Menu;

import static bms.utils.ConsoleUtils.getNumber;

public class BMSMain {



    public static void main(String[] args) {
        new BMSMain().start();
    }

    private void start(){
        BMSEngine engine = new Engine();


        // currentUser
        // menu class

        Menu m = new Menu();

        while(true){ // while not exit
            m.show();

            m.execute(getNumber());
        }
        // exit app

//
//        // while loop (until choose exit)
//        //    menu.show()
//        //    ans <- what do you choose
//        //    menu.doAction(ans)
//
//
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
//
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
