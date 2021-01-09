package bms.utils;

import bms.application.Menu;
import bms.engine.BMSEngine;
import bms.engine.LoginHandler;
import bms.module.*;
import bms.utils.commands.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MenuUtils {
    public static BMSEngine engine;
    public static MemberView user;
    public static LoginHandler loginHandler;

    private static void setLoginHandler(LoginHandler loginHandler) {
        MenuUtils.loginHandler = loginHandler;
    }
    private static void setEngine(BMSEngine engine) {
        MenuUtils.engine = engine;
    }
    private static void setLoggedInUser(MemberView user) {
        MenuUtils.user = user;
    }

    public static Menu getMainMenuForUser(MemberView user, BMSEngine engine, LoginHandler loginHandler) {
        setLoginHandler(loginHandler);
        setEngine(engine);
        setLoggedInUser(user);
        return createMainMenu(user.getManager());
    }

    private static Menu createMainMenu(boolean isManager){
        Menu mainMenu = new Menu("Boat House Main Menu");
        mainMenu.addOption("Edit my profile", new editProfileForUser());
        mainMenu.addOption("Create new reservation", ReservationCommands.addReservation());
        mainMenu.addOption("Show my future reservations", ReservationCommands.printFutureReservationForCurrentUser());
        mainMenu.addOption("Edit my reservations", ReservationCommands.chooseAndEditReservationForCurrentUser());
        mainMenu.addOption("Remove unapproved reservation", ReservationCommands.chooseAndDeleteReservationForCurrentUser());
        mainMenu.addOption("Reservation history", ReservationCommands.printReservationHistoryForCurrentUser());
        if (isManager)
            mainMenu.addOption("Manage", new openManageMenu());
        mainMenu.addOption("Logout", new logoutFromSystem(mainMenu));

        return mainMenu;
    }

    public static class logoutFromSystem implements Command
    {
        Menu menu;
        public logoutFromSystem(Menu menu){
            this.menu = menu;
        }

        @Override
        public void execute() {
            loginHandler.deleteUserSession(user.getSerialNumber());
            menu.ExitApp().execute();
        }
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
            subMenu.addOption("Display assignments for today", ReservationCommands.printApprovedReservations(LocalDate.now()));
            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openManageMembersMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Manage Members");

            subMenu.addOption("Add Member", MemberCommands.addMember());
            subMenu.addOption("Show Members", MemberCommands.printMembers());
            subMenu.addOption("Edit Member", MemberCommands.chooseAndEditMember());
            subMenu.addOption("Delete Member", MemberCommands.deleteMember());
            subMenu.addOption("Import data from file", new openImportMembersMenu());
            subMenu.addOption("Export data to file", MemberCommands.exportMembersToFile());
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

            subMenu.addOption("Edit Member Name", MemberCommands.editMemberName(memberSerialNumber));
            subMenu.addOption("Edit Member Age", MemberCommands.editMemberAge(memberSerialNumber));
            subMenu.addOption("Edit Member Notes", MemberCommands.editMemberNotes(memberSerialNumber));
            subMenu.addOption("Edit Member Level", MemberCommands.editMemberLevel(memberSerialNumber));
            subMenu.addOption("Edit Member Private Boat", MemberCommands.editMemberPrivateBoat(memberSerialNumber));
            subMenu.addOption("Edit Member Phone number", MemberCommands.editMemberPhone(memberSerialNumber));
            subMenu.addOption("Edit Member Email", MemberCommands.editMemberEmail(memberSerialNumber));
            subMenu.addOption("Edit Member Password", MemberCommands.editMemberPassword(memberSerialNumber));
            subMenu.addOption("Edit Member Role", MemberCommands.editMemberRole(memberSerialNumber));

            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openImportMembersMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Import members from file ");

            subMenu.addOption("Add to current data", MemberCommands.addMembersFromFile());
            subMenu.addOption("Replace current data", MemberCommands.replaceMembersFromFile());

            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openManageBoatsMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Manage Boats");

            subMenu.addOption("Add Boat", BoatCommands.addBoat());
            subMenu.addOption("Show Boats", BoatCommands.printBoats());
            subMenu.addOption("Edit Boat", BoatCommands.chooseAndEditBoat());
            subMenu.addOption("Delete Boat", BoatCommands.deleteBoat());
            subMenu.addOption("Import data from file", new openImportBoatsMenu());
            subMenu.addOption("Export data to file", BoatCommands.exportBoatsToFile());
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

            subMenu.addOption("Edit Boat Name", BoatCommands.editBoatName(boatSerialNumber));
            subMenu.addOption("Edit Boat Number Of Paddles", BoatCommands.editBoatNumOfPaddles(boatSerialNumber));
            subMenu.addOption("Edit Boat Private", BoatCommands.editBoatPrivate(boatSerialNumber));
            subMenu.addOption("Edit Boat has Coxswain", BoatCommands.editBoatCoxswain(boatSerialNumber));
            subMenu.addOption("Edit Boat Marine", BoatCommands.editBoatMarine(boatSerialNumber));
            subMenu.addOption("Edit Boat Disabled", BoatCommands.editBoatDisabled(boatSerialNumber));

            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openImportBoatsMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Import boats from file ");

            subMenu.addOption("Add to current data", BoatCommands.addBoatsFromFile());
            subMenu.addOption("Replace current data", BoatCommands.replaceBoatsFromFile());

            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openManageActivitiesMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Manage Activities");

            subMenu.addOption("Add Activity", ActivityCommands.addActivity());
            subMenu.addOption("Show Activities", ActivityCommands.printActivities());
            subMenu.addOption("Edit Activity", ActivityCommands.chooseAndEditActivity());
            subMenu.addOption("Delete Activity", ActivityCommands.deleteActivity());
            subMenu.addOption("Import data from file", new openImportActivitiesMenu());
            subMenu.addOption("Export data to file", ActivityCommands.exportActivitiesToFile());
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

            subMenu.addOption("Edit Activity Name", ActivityCommands.editActivityName(id));
            subMenu.addOption("Edit Activity Start Time", ActivityCommands.editActivityStartTime(id));
            subMenu.addOption("Edit Activity Finish Time", ActivityCommands.editActivityFinishTime(id));
            subMenu.addOption("Edit Activity Boat Type", ActivityCommands.editActivityBoatType(id));

            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openImportActivitiesMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Import activities from file ");

            subMenu.addOption("Add to current data", ActivityCommands.addActivitiesFromFile());
            subMenu.addOption("Replace current data", ActivityCommands.replaceActivitiesFromFile());

            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openManageReservationsMenu implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Manage Reservations");

            subMenu.addOption("Show All Reservation", new openShowDatesForReservationsMenu());
            subMenu.addOption("Show Unapproved Reservation", new openShowDatesForUnapprovedReservationsMenu());
            subMenu.addOption("Edit Unapproved Reservations", ReservationCommands.chooseAndEditReservationForManager());
            subMenu.addOption("Unapprove reservation", ReservationCommands.chooseAndUnapproveReservationForManager());
            subMenu.addOption("Delete Reservation", ReservationCommands.chooseAndDeleteReservationForManager());
            subMenu.addOption("View available (not disabled) boats", BoatCommands.viewAvailableBoats());
            subMenu.addOption("Allocate Boat and confirm reservation", ReservationCommands.chooseReservationToAllocateBoat());
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
                subMenu.addOption(date.format(formatter), ReservationCommands.printReservations(date));
            }
            subMenu.addOption("All week", ReservationCommands.printReservationsForWeek(startDate));

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
                subMenu.addOption(date.format(formatter), ReservationCommands.printUnapprovedReservations(date));
            }
            subMenu.addOption("All week", ReservationCommands.printUnapprovedReservationsForWeek(startDate));

            subMenu.addOption("Back", subMenu.stopLoop());
            subMenu.startLoop();
        }
    }

    public static class openEditReservationMenu implements Command
    {
        int id;
        ReservationView reservation;
        boolean forManager;

        public openEditReservationMenu(int id, boolean forManager) {
            this.id = id;
            this.forManager = forManager;
            this.reservation = engine.getReservation(id);
        }

        @Override
        public void execute() {

            Menu subMenu = new Menu("Edit Reservation. Ordered by: " +
                    engine.getMember(reservation.getOrderedMemberID()).getName());

            subMenu.addOption("Edit Reservation activity", ReservationCommands.editReservationActivity(id));
            subMenu.addOption("Edit Reservation activity Date", ReservationCommands.editReservationActivityDate(id));
            subMenu.addOption("Edit Reservation Boat Type", new openAddOrRemoveReservationBoatTypeMenu(id));
            subMenu.addOption("Edit Reservation participants", new openAddOrRemoveReservationParticipantsMenu(id));

            if(forManager && user.getManager())
                subMenu.addOption("Split reservation participants", ReservationCommands.splitReservationParticipants(id));

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
            this.reservation = engine.getReservation(id);
        }

        @Override
        public void execute() {

            Menu subMenu = new Menu("Edit Boat Type for Reservation");

            subMenu.addOption("Show boat type", ReservationCommands.showReservationBoatType(id));
            subMenu.addOption("Add type", ReservationCommands.addReservationBoatType(id));
            subMenu.addOption("delete type", ReservationCommands.deleteReservationBoatType(id));
            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class openAddOrRemoveReservationParticipantsMenu implements Command
    {
        int id;
        ReservationView reservation;

        public openAddOrRemoveReservationParticipantsMenu(int id) {
            this.id = id;
            this.reservation = engine.getReservation(id);
        }

        @Override
        public void execute() {

            Menu subMenu = new Menu("Edit Perticipents for Reservation");

            subMenu.addOption("Show Participants", ReservationCommands.showReservationPerticipents(id));
            subMenu.addOption("Add Participants", ReservationCommands.addReservationPerticipent(id));
            subMenu.addOption("delete Participants", ReservationCommands.deleteReservationPerticipent(id));
            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }

    public static class editProfileForUser implements Command
    {
        @Override
        public void execute() {
            Menu subMenu = new Menu("Edit Profile");

            subMenu.addOption("Edit Name", MemberCommands.editMemberName(user.getSerialNumber()));
            subMenu.addOption("Edit Phone number", MemberCommands.editMemberPhone(user.getSerialNumber()));
            subMenu.addOption("Edit Email", MemberCommands.editMemberEmail(user.getSerialNumber()));
            subMenu.addOption("Edit Password", MemberCommands.editMemberPassword(user.getSerialNumber()));

            subMenu.addOption("Back", subMenu.stopLoop());

            subMenu.startLoop();
        }
    }
}
