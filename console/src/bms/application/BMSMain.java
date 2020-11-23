package bms.application;

import bms.engine.BMSEngine;
import bms.engine.Engine;


import bms.menu.Menu;
import bms.module.Activity;
import bms.module.Boat;
import bms.module.Member;

import java.time.LocalDateTime;

import static bms.utils.ConsoleUtils.getNumber;

public class BMSMain {



    public static void main(String[] args) {
        new BMSMain().start();
    }

    private void start(){
        BMSEngine engine = new Engine();


        // currentUser
        // menu class
//
//        Menu m = new Menu();
//
//        while(true) { // while not exit
//            m.show();
//
//            m.execute(getNumber());
//        }
        // exit app

//
//        // while loop (until choose exit)
//        //    menu.show()
//        //    ans <- what do you choose
//        //    menu.doAction(ans)
//
//
        engine.addBoat( "Gal", "2X", true, false, true, false);
//        System.out.println("getBoat method:");
        System.out.println(engine.getBoat(0).print());
//        System.out.println("getAll method:");
//        for (Boat boat : engine.getBoats())
//            System.out.println(boat);
//        engine.deleteBoat(0);
//        System.out.println("after delete method:");
//        for (Boat boat : engine.getBoats())
//            System.out.println(boat);

        engine.updateBoat(0,"iris",null,null,null,null,null,null);
        System.out.println(engine.getBoat(0).print());

//
       engine.addMember("meshi", 22,null, Member.Level.PROFESIONAL, LocalDateTime.now(),LocalDateTime.now(),true,1,"054-9999999",
                null,"nee",true);
//
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
//       for(Activity activity : engine.getActivities())
//           System.out.println(activity);
//       engine.deleteActivity(0);
//        for(Activity activity : engine.getActivities())
//            System.out.println(activity);


        engine.updateActivity(0,"runing",null,null,null);
        System.out.println(engine.getActivity(0));



    }

}
