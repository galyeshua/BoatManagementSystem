package bms.utils.menu;

import bms.application.Menu;
import bms.engine.BMSEngine;

public class MenuUtils {

    public static Menu getMainMenuForUser(boolean isManager, BMSEngine engine) {
        Commands.setEngine(engine);
        return createMainMenu(isManager);
    }

    private static Menu createMainMenu(boolean isManager){
        Menu mainMenu = new Menu("Boat House Main Menu");
        //mainMenu.addOption("New reservation", Commands.test());
        //mainMenu.addOption("Show reservations", Commands.test());
        //mainMenu.addOption("Reservation history", Commands.test());
        //mainMenu.addOption("Edit my profile", Commands.test());
        if (isManager)
            mainMenu.addOption("Manage", new openManageMenu());
        mainMenu.addOption("Exit", mainMenu.ExitApp());

        return mainMenu;
    }

    public static class openManageMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Manage");

            subMenu.addOption("Manage Members", new openManageMembersMenu());
            subMenu.addOption("Manage Boats", new openManageBoatsMenu());
            //subMenu.addOption("Manage Times", new openManageTimesMenu());
            //subMenu.addOption("Shibutzim", new BoatCommands.PrintBoats());
            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openManageMembersMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Manage Members");

            subMenu.addOption("Add Member", Commands.addMember());
            subMenu.addOption("Show Members", Commands.printMembers());
            subMenu.addOption("Edit Member", Commands.chooseAndEditMember());
            subMenu.addOption("Delete Member", Commands.deleteMember());
            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openEditMemberMenu implements Command
    {
        int memberSerialNumber;
        public openEditMemberMenu(int memberSerialNumber) {
            this.memberSerialNumber = memberSerialNumber;
        }

        @Override
        public void execute() {
            Menu subMenu = new Menu("Edit Member " + memberSerialNumber);

            subMenu.addOption("Edit Member Name", Commands.editMemberName(memberSerialNumber));
            subMenu.addOption("Edit Member Age", Commands.editMemberAge(memberSerialNumber));
            subMenu.addOption("Edit Member Notes", Commands.editMemberNotes(memberSerialNumber));
            subMenu.addOption("Edit Member Level", Commands.editMemberLevel(memberSerialNumber));
            //subMenu.addOption("Edit Member expire Date", Commands.editBoatMarine(memberSerialNumber));
            subMenu.addOption("Edit Member Private Boat", Commands.editMemberPrivateBoat(memberSerialNumber));
            subMenu.addOption("Edit Member Phone number", Commands.editMemberPhone(memberSerialNumber));
            subMenu.addOption("Edit Member Email", Commands.editMemberEmail(memberSerialNumber));
            subMenu.addOption("Edit Member Password", Commands.editMemberPassword(memberSerialNumber));
            subMenu.addOption("Edit Member Role", Commands.editMemberRole(memberSerialNumber));

            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openManageBoatsMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Manage Boats");

            subMenu.addOption("Add Boat", Commands.addBoat());
            subMenu.addOption("Show Boats", Commands.printBoats());
            subMenu.addOption("Edit Boat", Commands.chooseAndEditBoat());
            subMenu.addOption("Delete Boat", Commands.deleteBoat());
            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openEditBoatMenu implements Command
    {
        int boatSerialNumber;
        public openEditBoatMenu(int boatSerialNumber) {
            this.boatSerialNumber = boatSerialNumber;
        }

        @Override
        public void execute() {
            Menu subMenu = new Menu("Edit Boat " + boatSerialNumber);

            subMenu.addOption("Edit Boat Name", Commands.editBoatName(boatSerialNumber));
            subMenu.addOption("Edit Boat Number Of Paddles", Commands.editBoatNumOfPaddles(boatSerialNumber));
            subMenu.addOption("Edit Boat Private", Commands.editBoatPrivate(boatSerialNumber));
            subMenu.addOption("Edit Boat has Coxswain", Commands.editBoatCoxswain(boatSerialNumber));
            subMenu.addOption("Edit Boat Marine", Commands.editBoatMarine(boatSerialNumber));
            subMenu.addOption("Edit Boat Disabled", Commands.editBoatDisabled(boatSerialNumber));

            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openManageActivitiesMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Manage Boats");

            subMenu.addOption("Add Activity", Commands.addBoat());
            subMenu.addOption("Show Activities", Commands.printBoats());
            subMenu.addOption("Edit Activity", Commands.chooseAndEditBoat());
            subMenu.addOption("Delete Activity", Commands.deleteBoat());
            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openEditActivityMenu implements Command
    {
        int boatSerialNumber;
        public openEditActivityMenu(int boatSerialNumber) {
            this.boatSerialNumber = boatSerialNumber;
        }

        @Override
        public void execute() {
            Menu subMenu = new Menu("Edit Boat " + boatSerialNumber);

            subMenu.addOption("Edit Activity Name", Commands.editActivityName(boatSerialNumber));
            subMenu.addOption("Edit Activity Start Time", Commands.editActivityStartTime(boatSerialNumber));
            subMenu.addOption("Edit Activity Finish Time", Commands.editActivityFinishTime(boatSerialNumber));
            subMenu.addOption("Edit Activity Boat Type", Commands.editActivityBoatType(boatSerialNumber));


            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }



//    public static class openManageTimesMenu implements Command
//    {
//        @Override
//        public void execute() {
//            Menu subMenu = new Menu("Manage Boats");
//
//            subMenu.addOption("Back", new exitMenu(subMenu));
//            subMenu.addOption("Add Boat", Commands.test());
//            subMenu.addOption("Show Boats", Commands.test());
//            subMenu.addOption("Edit Boat", Commands.test());
//            subMenu.addOption("Delete Boat", Commands.test());
//
//            subMenu.startLoop();
//        }
//    }


}
