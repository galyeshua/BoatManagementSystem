package bms.utils.menu;

import bms.application.Menu;
import bms.engine.BMSEngine;
import bms.module.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MenuUtils {

    public static Menu getMainMenuForUser(MemberView user, BMSEngine engine) {
        Commands.setEngine(engine);
        Commands.setLoggedInUser(user);
        return createMainMenu(user.getManager());
    }

    private static Menu createMainMenu(boolean isManager){
        Menu mainMenu = new Menu("Boat House Main Menu");
        //mainMenu.addOption("Edit my profile", Commands.test());
        mainMenu.addOption("New reservation", Commands.addReservation());
        mainMenu.addOption("Show my reservations", Commands.printFutureReservationForCurrentUser());
        mainMenu.addOption("Edit my reservations", Commands.chooseAndEditReservationForCurrentUser());
        mainMenu.addOption("Reservation history", Commands.printReservationHistoryForCurrentUser());
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
            subMenu.addOption("Manage Activities", new openManageActivitiesMenu());
            subMenu.addOption("Manage Reservations", new openManageReservationsMenu());
            subMenu.addOption("Display assignments for today", Commands.printApprovedReservations(LocalDate.now()));
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
            Menu subMenu = new Menu("Manage Activities");

            subMenu.addOption("Add Activity", Commands.addActivity());
            subMenu.addOption("Show Activities", Commands.printActivities());
            subMenu.addOption("Edit Activity", Commands.chooseAndEditActivity());
            subMenu.addOption("Delete Activity", Commands.deleteActivity());
            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openEditActivityMenu implements Command
    {
        int id;
        public openEditActivityMenu(int id) {
            this.id = id;
        }

        @Override
        public void execute() {
            Menu subMenu = new Menu("Edit Activity " + id);

            subMenu.addOption("Edit Activity Name", Commands.editActivityName(id));
            subMenu.addOption("Edit Activity Start Time", Commands.editActivityStartTime(id));
            subMenu.addOption("Edit Activity Finish Time", Commands.editActivityFinishTime(id));
            subMenu.addOption("Edit Activity Boat Type", Commands.editActivityBoatType(id));


            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }


    public static class openManageReservationsMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Manage Reservations");

            //subMenu.addOption("Add Reservation", Commands.addReservation());
            subMenu.addOption("Show All Reservation", new openShowDatesForReservationsMenu());
            subMenu.addOption("Show Unapproved Reservation", new openShowDatesForUnapprovedReservationsMenu());
            subMenu.addOption("Edit Unapproved Reservations", Commands.chooseAndEditReservationForManager());
            subMenu.addOption("Unapprove reservation", Commands.chooseAndUnapproveReservationForManager());

            //subMenu.addOption("Delete Reservation", Commands.deleteReservation());
            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openShowDatesForReservationsMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Choose date to show Reservations");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate startDate = LocalDate.now();

            for (int i = 0; i< 7; i++){
                LocalDate date = startDate.plusDays(i);
                subMenu.addOption(date.format(formatter), Commands.printReservations(date));
            }
            subMenu.addOption("All week", Commands.printReservationsForWeek(startDate));

            subMenu.addOption("Back", subMenu.stopLoop());
            subMenu.startLoop();
        }
    }


    public static class openShowDatesForUnapprovedReservationsMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Choose date to show Unapproved Reservations");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate startDate = LocalDate.now();

            for (int i = 0; i< 7; i++){
                LocalDate date = startDate.plusDays(i);
                subMenu.addOption(date.format(formatter), Commands.printUnapprovedReservations(date));
            }
            subMenu.addOption("All week", Commands.printUnapprovedReservationsForWeek(startDate));

            subMenu.addOption("Back", subMenu.stopLoop());
            subMenu.startLoop();
        }
    }


    public static class openEditReservationMenu implements Command
    {
        int id;
        ReservationView reservation;
        boolean forManager = false;

        public openEditReservationMenu(int id, boolean forManager) {
            this.id = id;
            this.forManager = forManager;
            this.reservation = Commands.engine.getReservation(id);
        }

        @Override
        public void execute() {

            Menu subMenu = new Menu("Edit Reservation for " + id + " (Approved: " + reservation.getIsApproved() + ")");

            subMenu.addOption("Edit Reservation activity", Commands.editReservationActivity(id));
            subMenu.addOption("Edit Reservation activity Date", Commands.editReservationActivityDate(id));
            subMenu.addOption("Edit Reservation Boat Type", new openAddOrRemoveReservationBoatTypeMenu(id));
            subMenu.addOption("Edit Reservation participants", new openAddOrRemoveReservationParticipentsMenu(id));

            if(forManager && Commands.user.getManager()){
                subMenu.addOption("View available boats", Commands.test());
                subMenu.addOption("Split reservation participants", Commands.test());
                subMenu.addOption("Allocate Boat and confirm reservation", Commands.test());
            }

            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }


    public static class openAddOrRemoveReservationBoatTypeMenu implements Command
    {
        int id;
        ReservationView reservation;

        public openAddOrRemoveReservationBoatTypeMenu(int id) {
            this.id = id;
            this.reservation = Commands.engine.getReservation(id);
        }

        @Override
        public void execute() {

            Menu subMenu = new Menu("Edit Boat Type for Reservation");

            subMenu.addOption("Show boat type", Commands.showReservationBoatType(id));
            subMenu.addOption("Add type", Commands.addReservationBoatType(id));
            subMenu.addOption("delete type", Commands.deleteReservationBoatType(id));
            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }


    public static class openAddOrRemoveReservationParticipentsMenu implements Command
    {
        int id;
        ReservationView reservation;

        public openAddOrRemoveReservationParticipentsMenu(int id) {
            this.id = id;
            this.reservation = Commands.engine.getReservation(id);
        }

        @Override
        public void execute() {

            Menu subMenu = new Menu("Edit Perticipents for Reservation");

            subMenu.addOption("Show Perticipents", Commands.showReservationPerticipents(id));
            subMenu.addOption("Add Perticipent", Commands.addReservationPerticipent(id));
            subMenu.addOption("delete Perticipent", Commands.deleteReservationPerticipent(id));
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
