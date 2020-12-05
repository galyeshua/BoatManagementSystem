package bms.utils.menu;

import bms.engine.BMSEngine;
import bms.engine.list.manager.Exceptions;
import bms.module.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static bms.utils.InputUtils.*;
//import static bms.utils.InputUtils.getStringFromUser;

public class Commands {
    static BMSEngine engine;

    public static void setEngine(BMSEngine engine) {
        Commands.engine = engine;
    }


    public static Command test(){
        return new Command() {
            @Override
            public void execute() {
                System.out.println("Test");
            }
        };
    }


    public static Command printBoats(){
        return new Command() {
            @Override
            public void execute() {
                for (BoatView boat : engine.getBoats()){
                    //System.out.println(boat);
                    System.out.println("Name: " + boat.getName() + ". Type: " + boat.getFormattedCode() +
                            ". Serial Number: " + boat.getSerialNumber() + ". Private: " + boat.getPrivate() +
                            ". Disabled: " + boat.getDisabled());
                }

            }
        };
    }


    public static Command addBoat(){
        return new Command() {
            boolean askForCoxswain;
            boolean askForPaddles;

            int serialNumber;
            String boatName;
            Boat.Rowers numOfRowers;
            Boat.Paddles numOfPaddles;
            Boolean isPrivate;
            Boolean isWide;
            Boolean hasCoxswain;
            Boolean isMarine;
            Boolean isDisabled;

            private void askForValues(){
                askForCoxswain = true;
                askForPaddles = true;

                System.out.println("Enter Serial Number:");
                serialNumber = getNumberFromUser(1);
                System.out.println("Enter name:");
                boatName = getStringFromUser();
                System.out.println("How many rowers:");
                numOfRowers = (Boat.Rowers) chooseFromOptions(Boat.Rowers.values());
                if (numOfRowers.equals(Boat.Rowers.ONE)){
                    hasCoxswain = false;
                    askForCoxswain = false;

                    numOfPaddles = Boat.Paddles.DOUBLE;
                    askForPaddles = false;
                }
                if (numOfRowers.equals(Boat.Rowers.EIGHT)){
                    hasCoxswain = true;
                    askForCoxswain = false;
                }
                if (askForPaddles){
                    System.out.println("choose type of paddles:");
                    numOfPaddles = (Boat.Paddles) chooseFromOptions(Boat.Paddles.values());
                }
                System.out.println("is private?");
                isPrivate = getBoolFromUser();
                System.out.println("is Wide?");
                isWide = getBoolFromUser();
                if (askForCoxswain){
                    System.out.println("has Coxswain?");
                    hasCoxswain = getBoolFromUser();
                }
                System.out.println("is Marine?");
                isMarine = getBoolFromUser();
                System.out.println("is Disabled?");
                isDisabled = getBoolFromUser();
            }

            @Override
            public void execute() {
                try{
                    askForValues();
                    engine.addBoat(serialNumber, boatName, numOfRowers, numOfPaddles, isPrivate, isWide, hasCoxswain, isMarine, isDisabled);
                } catch (Exceptions.BoatAlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command deleteBoat() {
        return new Command() {
            int serialNumber;

            @Override
            public void execute() {
                printBoats().execute();
                System.out.println("choose boat to delete");
                serialNumber = getNumberFromUser();

                try{
                    engine.deleteBoat(serialNumber);
                } catch (Exceptions.BoatNotFoundException e){
                    System.out.println("Boat not found");
                }
            }
        };
    }


    public static Command chooseAndEditBoat() {
        return new Command() {
            int serialNumber;
            BoatView boat;

            private void chooseBoatToUpdate() throws Exceptions.BoatNotFoundException {
                System.out.println("All the boats:");
                printBoats().execute();
                System.out.println("choose boat to edit");
                serialNumber = getNumberFromUser();
                boat = engine.getBoat(serialNumber);
                if (boat == null)
                    throw new Exceptions.BoatNotFoundException();
            }

            @Override
            public void execute() {
                try{
                    chooseBoatToUpdate();
                    new MenuUtils.openEditBoatMenu(serialNumber).execute();
                } catch (Exceptions.BoatNotFoundException e){
                    System.out.println("Boat not found");
                }
            }
        };
    }

    public static Command editBoatName(int serialNumber) {
        return new Command() {
            String boatName;
            Boat newBoat;

            @Override
            public void execute() {
                try{
                    newBoat = new Boat(engine.getBoat(serialNumber));
                    boatName = getStringFromUser();
                    newBoat.setName(boatName);
                    engine.updateBoat(newBoat);
                } catch (Exceptions.BoatNotFoundException e){
                    System.out.println("Boat not found");
                } catch (Exceptions.IllegalBoatValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }



    public static Command editBoatNumOfPaddles(int serialNumber) {
        return new Command() {
            Boat.Paddles numOfPaddles;
            Boat newBoat;

            @Override
            public void execute() {
                try{
                    newBoat = new Boat(engine.getBoat(serialNumber));
                    numOfPaddles = (BoatView.Paddles) chooseFromOptions(BoatView.Paddles.values());
                    newBoat.setNumOfPaddles(numOfPaddles);
                    engine.updateBoat(newBoat);
                } catch (Exceptions.BoatNotFoundException e){
                    System.out.println("Boat not found");
                } catch (Exceptions.IllegalBoatValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editBoatPrivate(int serialNumber) {
        return new Command() {
            boolean isPrivate;
            Boat newBoat;

            @Override
            public void execute() {
                try{
                    newBoat = new Boat(engine.getBoat(serialNumber));
                    isPrivate = getBoolFromUser();
                    newBoat.setPrivate(isPrivate);
                    engine.updateBoat(newBoat);
                } catch (Exceptions.BoatNotFoundException e){
                    System.out.println("Boat not found");
                } catch (Exceptions.IllegalBoatValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editBoatCoxswain(int serialNumber) {
        return new Command() {
            boolean hasCoxswain;
            Boat newBoat;

            @Override
            public void execute() {
                try{
                    newBoat = new Boat(engine.getBoat(serialNumber));
                    hasCoxswain = getBoolFromUser();
                    newBoat.setHasCoxswain(hasCoxswain);
                    engine.updateBoat(newBoat);
                } catch (Exceptions.BoatNotFoundException e){
                    System.out.println("Boat not found");
                } catch (Exceptions.IllegalBoatValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editBoatMarine(int serialNumber) {
        return new Command() {
            boolean isMarine;
            Boat newBoat;

            @Override
            public void execute() {
                try{
                    newBoat = new Boat(engine.getBoat(serialNumber));
                    isMarine = getBoolFromUser();
                    newBoat.setMarine(isMarine);
                    engine.updateBoat(newBoat);
                } catch (Exceptions.BoatNotFoundException e){
                    System.out.println("Boat not found");
                } catch (Exceptions.IllegalBoatValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editBoatDisabled(int serialNumber) {
        return new Command() {
            boolean isDisabled;
            Boat newBoat;

            @Override
            public void execute() {
                try{
                    newBoat = new Boat(engine.getBoat(serialNumber));
                    isDisabled = getBoolFromUser();
                    newBoat.setDisabled(isDisabled);
                    engine.updateBoat(newBoat);
                } catch (Exceptions.BoatNotFoundException e){
                    System.out.println("Boat not found");
                } catch (Exceptions.IllegalBoatValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }


    public static Command chooseAndEditMember() {
        return new Command() {
            int serialNumber;
            MemberView member;

            private void chooseMemberToUpdate() throws Exceptions.MemberNotFoundException {
                System.out.println("All the members:");
                printMembers().execute();
                System.out.println("choose member to edit");
                serialNumber = getNumberFromUser();
                member = engine.getMember(serialNumber);
                if (member == null)
                    throw new Exceptions.MemberNotFoundException();
            }

            @Override
            public void execute() {
                try{
                    chooseMemberToUpdate();
                    new MenuUtils.openEditMemberMenu(serialNumber).execute();
                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                }
            }
        };
    }

    public static Command addMember() {
        return new Command() {
            private int serialNumber;
            private String name;
            private int age;
            private String notes;
            private Member.Level level;
            private LocalDate joinDate;
            private LocalDate expireDate;
            private boolean hasPrivateBoat;
            private int boatSerialNumber;
            private String phoneNumber;
            private String email;
            private String password;
            private boolean isManager;

            private void askForValues(){
                System.out.println("Enter Serial Number:");
                serialNumber = getNumberFromUser(1, 99);
                System.out.println("Enter name:");
                name = getStringFromUser();
                System.out.println("Enter Age:");
                age = getNumberFromUser(16, 99);
                System.out.println("Enter Notes:");
                notes = getStringFromUser();
                System.out.println("Enter Level:");
                level = (Member.Level) chooseFromOptions(Member.Level.values());
                joinDate = LocalDate.now();
                expireDate = LocalDate.now().plusYears(1);
                System.out.println("Does member have private boat?");
                hasPrivateBoat = getBoolFromUser();
                if (hasPrivateBoat){
                    System.out.println("Enter Boat Serial Number:");
                    boatSerialNumber = getNumberFromUser(1, 99);
                }
                System.out.println("Enter phone number:");
                phoneNumber = getStringFromUser();
                System.out.println("Enter email:");
                email = getStringFromUser();
                System.out.println("Enter password:");
                password = getStringFromUser();
                System.out.println("is manager?");
                isManager = getBoolFromUser();
            }

            @Override
            public void execute() {
                try{
                    askForValues();
                    engine.addMember(serialNumber, name, age, notes, level, joinDate, expireDate, hasPrivateBoat,
                            boatSerialNumber, phoneNumber, email, password, isManager);
                } catch (Exceptions.MemberAlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command printMembers() {
        return new Command() {
            @Override
            public void execute() {
                for (MemberView member : engine.getMembers())
                    System.out.println(member);
            }
        };
    }

    public static Command deleteMember() {
        return new Command() {
            int serialNumber;

            @Override
            public void execute() {
                printBoats().execute();
                System.out.println("choose member to delete");
                serialNumber = getNumberFromUser();

                try{
                    engine.deleteMember(serialNumber);
                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                }
            }
        };
    }

    public static Command editMemberName(int serialNumber) {
        return new Command() {
            String name;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(engine.getMember(serialNumber));
                    name = getStringFromUser();
                    newMember.setName(name);
                    engine.updateMember(newMember);
                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                } catch (Exceptions.IllegalMemberValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editMemberAge(int serialNumber) {
        return new Command() {
            int age;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(engine.getMember(serialNumber));
                    age = getNumberFromUser(16, 99);
                    newMember.setAge(age);
                    engine.updateMember(newMember);

                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                } catch (Exceptions.IllegalMemberValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editMemberNotes(int serialNumber) {
        return new Command() {
            String notes;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(engine.getMember(serialNumber));
                    notes = getStringFromUser();
                    newMember.setNotes(notes);
                    engine.updateMember(newMember);
                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                } catch (Exceptions.IllegalMemberValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }


    public static Command editMemberLevel(int serialNumber) {
        return new Command() {
            Member.Level level;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(engine.getMember(serialNumber));
                    level = (Member.Level) chooseFromOptions(Member.Level.values());
                    newMember.setLevel(level);
                    engine.updateMember(newMember);
                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                } catch (Exceptions.IllegalMemberValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editMemberPrivateBoat(int serialNumber) {
        return new Command() {
            boolean hasPrivateBoat;
            int boatSerialNumber;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(engine.getMember(serialNumber));
                    hasPrivateBoat = getBoolFromUser();
                    newMember.setHasPrivateBoat(hasPrivateBoat);
                    if (hasPrivateBoat){
                        System.out.println("Enter Boat Serial Number:");
                        boatSerialNumber = getNumberFromUser(1);
                        newMember.setBoatSerialNumber(boatSerialNumber);
                    }
                    engine.updateMember(newMember);
                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                } catch (Exceptions.IllegalMemberValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editMemberPhone(int serialNumber) {
        return new Command() {
            String phone;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(engine.getMember(serialNumber));
                    phone = getStringFromUser();
                    newMember.setPhoneNumber(phone);
                    engine.updateMember(newMember);
                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                } catch (Exceptions.IllegalMemberValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editMemberEmail(int serialNumber) {
        return new Command() {
            String email;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(engine.getMember(serialNumber));
                    email = getStringFromUser();
                    newMember.setEmail(email);
                    engine.updateMember(newMember);
                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                } catch (Exceptions.IllegalMemberValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editMemberPassword(int serialNumber) {
        return new Command() {
            String password;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(engine.getMember(serialNumber));
                    password = getStringFromUser();
                    newMember.setPassword(password);
                    engine.updateMember(newMember);
                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                } catch (Exceptions.IllegalMemberValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editMemberRole(int serialNumber) {
        return new Command() {
            boolean role;
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(engine.getMember(serialNumber));
                    role = getBoolFromUser();
                    newMember.setManager(role);
                    engine.updateMember(newMember);
                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                } catch (Exceptions.IllegalMemberValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Command chooseAndEditActivity() {
        return new Command() {
            ActivityView activity;
            int id;

            private void chooseActivityToUpdate() throws Exceptions.ActivityNotFoundException {
                printActivities().execute();
                System.out.println("choose activity to edit");

                ArrayList<ActivityView> activities = (ArrayList<ActivityView>)engine.getActivities();
                int activityIndex = getNumberFromUser(0, activities.size() - 1);
                id = activities.get(activityIndex).getId();
                activity = engine.getActivity(id);

                if (activity == null)
                    throw new Exceptions.ActivityNotFoundException();
            }

            @Override
            public void execute() {
                try{
                    chooseActivityToUpdate();
                    new MenuUtils.openEditActivityMenu(id).execute();
                } catch (Exceptions.ActivityNotFoundException e){
                    System.out.println("Activity not found");
                }
            }
        };
    }

    public static Command addActivity() {
        return new Command() {
            private String name;
            private LocalTime startTime;
            private LocalTime finishTime;
            private String boatType;


            private void askForValues(){
                System.out.println("Enter Name:");
                name = getStringFromUser();
                System.out.println("Enter startTime:");
                startTime = getLocalTimeFromUser();
                System.out.println("Enter finishTime:");
                finishTime = getLocalTimeFromUser();
                System.out.println("Enter Boat Type:");
                boatType = getStringFromUser();
            }

            @Override
            public void execute() {
                try{
                    askForValues();
                    engine.addActivity(name,startTime,finishTime,boatType);
                } catch (Exceptions.ActivityAlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command printActivities() {
        return new Command() {
            @Override
            public void execute() {
                int i =0;
                for (ActivityView activity : engine.getActivities()){
                    System.out.println("[" + i + "] " + activity.getStartTime() + " - " + activity.getFinishTime()
                            + " : " + activity.getName());
                    i++;
                }
            }
        };
    }

    public static Command deleteActivity() {
        return new Command() {
            @Override
            public void execute() {
                printActivities().execute();
                System.out.println("choose activity to delete");
                List<ActivityView> activities = new ArrayList<ActivityView>(engine.getActivities());
                int activityIndex = getNumberFromUser(0, activities.size() - 1);
                int id = activities.get(activityIndex).getId();
                try{
                    engine.deleteActivity(id);
                } catch (Exceptions.ActivityNotFoundException e){
                    System.out.println("Activity not found");
                }
            }
        };
    }

    public static Command editActivityName(int id) {
        return new Command() {
            String name;
            Activity newActivity;

            @Override
            public void execute() {
                try{
                    newActivity = new Activity(engine.getActivity(id));
                    name = getStringFromUser();
                    newActivity.setName(name);
                    engine.updateActivity(newActivity);
                } catch (Exceptions.ActivityNotFoundException e){
                    System.out.println("Boat not found");
                } catch (Exceptions.IllegalActivityValueException e){
                    System.out.println("Error: " + e.getMessage());
                }

            }
        };
    }

    public static Command editActivityStartTime(int id) {
        return new Command() {
            LocalTime startTime;
            Activity newActivity;

            @Override
            public void execute() {
                try{
                    newActivity = new Activity(engine.getActivity(id));
                    startTime = getLocalTimeFromUser();
                    newActivity.setStartTime(startTime);
                    engine.updateActivity(newActivity);
                } catch (Exceptions.ActivityNotFoundException e){
                    System.out.println("Boat not found");
                } catch (Exceptions.IllegalActivityValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editActivityFinishTime(int id) {
        return new Command() {
            LocalTime finishTime;
            Activity newActivity;

            @Override
            public void execute() {
                try{
                    newActivity = new Activity(engine.getActivity(id));
                    finishTime = getLocalTimeFromUser();
                    newActivity.setStartTime(finishTime);
                    engine.updateActivity(newActivity);
                } catch (Exceptions.ActivityNotFoundException e){
                    System.out.println("Boat not found");
                } catch (Exceptions.IllegalActivityValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editActivityBoatType(int id) {
        return new Command() {
            String boatType;
            Activity newActivity;

            @Override
            public void execute() {
                try{
                    newActivity = new Activity(engine.getActivity(id));
                    boatType = getStringFromUser();
                    newActivity.setBoatType(boatType);
                    engine.updateActivity(newActivity);
                } catch (Exceptions.ActivityNotFoundException e){
                    System.out.println("Boat not found");
                } catch (Exceptions.IllegalActivityValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }


    //////////////////////////////////////////////////////////////////////



    public static Command chooseAndEditReservation() {
        return new Command() {
            int serialNumber;
            ReservationView reservation;

            private void chooseReservationToUpdate() throws Exceptions.ReservationNotFoundException {
                System.out.println("All the reservation:");
                printReservation().execute();
                System.out.println("choose reservation to edit");
                serialNumber = getNumberFromUser();
                reservation = engine.getReservation(serialNumber);
                if (reservation == null)
                    throw new Exceptions.ReservationNotFoundException();
            }
           // openEditReservationMenu
            @Override
            public void execute() {
                try{
                    chooseReservationToUpdate();
                    new MenuUtils.openEditReservationMenu(serialNumber).execute();
                } catch (Exceptions.ReservationNotFoundException e){
                    System.out.println("Reservation not found");
                }
            }
        };
    }

    public static Command addReservation() {
        return new Command() {
            private int memberID;
            private Activity activity; //
            private LocalDate activityDate;
            private List<Boat.Rowers> boatType;
            private List<Member> participants; //
            private LocalDateTime orderDate;
            private int orderedMemberID;

            private void askForValues(){
                System.out.println("Enter member ID:");
                memberID = getNumberFromUser();
                System.out.println("Enter activity:");
                //activity = getStringFromUser();
                System.out.println("Enter activity Date:");
                activityDate = getLocalDateFromUser();
                System.out.println("Enter boat Type:");
                //boatType = getStringFromUser();
                System.out.println("Enter participants:");
               // participants = (Member.Level) chooseFromOptions(Member.Level.values());
                System.out.println("Enter order Date:");
                orderDate = getLocalDateTimeFromUser();
                System.out.println("Enter Your ID:");
                orderedMemberID = getNumberFromUser();
            }

            @Override
            public void execute() {
               // try{ impossible ?????
                    askForValues();
                    engine.addReservation(memberID,activity,activityDate,boatType,participants,orderDate,orderedMemberID);
             //   } catch (Exceptions.ReservationAlreadyExistsException e){
               //     System.out.println("Error: " + e.getMessage());
                //}
            }
        };
    }

    public static Command printReservation() {
        return new Command() {
            @Override
            public void execute() {
                for (ReservationView reservation : engine.getReservations())
                    System.out.println(reservation);
            }
        };
    }

    public static Command deleteReservation() {
        return new Command() {
            int serialNumber;

            @Override
            public void execute() {
                printReservation().execute();
                System.out.println("choose Reservation to delete");
                serialNumber = getNumberFromUser();

                try{
                    engine.deleteReservation(serialNumber);
                } catch (Exceptions.ReservationNotFoundException e){
                    System.out.println("Reservation not found");
                }
            }
        };
    }


    public static Command editReservationMemberID(int id) {
        return new Command() {
            int memberID;
            Reservation newReservation;

            @Override
            public void execute() {
                try {
                    newReservation = new Reservation(engine.getReservation(id));
                    memberID = getNumberFromUser();
                    newReservation.setMemberID(memberID);
                    engine.updateReservation(newReservation);
                }catch (Exceptions.ReservationNotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Exceptions.ReservationAlreadyApprovedException e){
                    System.out.println("Reservation Already Approved");
                } catch (Exceptions.IllegalActivityValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editReservationActivity(int id) {
        return new Command() {
            Activity activity;
            Reservation newReservation;

            @Override
            public void execute() {
                try {
                    newReservation = new Reservation(engine.getReservation(id));
                   // activity = getNumberFromUser();
                    newReservation.setActivity(activity);
                    engine.updateReservation(newReservation);
                }catch (Exceptions.ReservationNotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Exceptions.ReservationAlreadyApprovedException e){
                    System.out.println("Reservation Already Approved");
                } catch (Exceptions.IllegalActivityValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editReservationActivityDate(int id) {
        return new Command() {
            LocalDate activityDate;
            Reservation newReservation;

            @Override
            public void execute() {
                try {
                    newReservation = new Reservation(engine.getReservation(id));
                    activityDate = getLocalDateFromUser();
                    newReservation.setActivityDate(activityDate);
                    engine.updateReservation(newReservation);
                }catch (Exceptions.ReservationNotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Exceptions.ReservationAlreadyApprovedException e){
                    System.out.println("Reservation Already Approved");
                } catch (Exceptions.IllegalActivityValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }


    public static Command editReservationBoatType(int id) {
        return new Command() {
            List<Boat.Rowers> boatType;
            Reservation newReservation;

            @Override
            public void execute() {
                try {
                    newReservation = new Reservation(engine.getReservation(id));
                    //boatType = getLocalDateFromUser();
                    newReservation.setBoatType(boatType);
                    engine.updateReservation(newReservation);
                }catch (Exceptions.ReservationNotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Exceptions.ReservationAlreadyApprovedException e){
                    System.out.println("Reservation Already Approved");
                } catch (Exceptions.IllegalActivityValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editReservationParticipants(int id) {
        return new Command() {
            List<Member> participants;
            Reservation newReservation;

            @Override
            public void execute() {
                try {
                    newReservation = new Reservation(engine.getReservation(id));
                  //  activityDate = getLocalDateFromUser();
                    newReservation.setParticipants(participants);
                    engine.updateReservation(newReservation);
                }catch (Exceptions.ReservationNotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Exceptions.ReservationAlreadyApprovedException e){
                    System.out.println("Reservation Already Approved");
                } catch (Exceptions.IllegalActivityValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }


    public static Command editReservationOrderDate(int id) {
        return new Command() {
            LocalDateTime orderDate;
            Reservation newReservation;

            @Override
            public void execute() {
                try {
                    newReservation = new Reservation(engine.getReservation(id));
                    orderDate = getLocalDateTimeFromUser();
                    newReservation.setOrderDate(orderDate);
                    engine.updateReservation(newReservation);
                }catch (Exceptions.ReservationNotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Exceptions.ReservationAlreadyApprovedException e){
                    System.out.println("Reservation Already Approved");
                } catch (Exceptions.IllegalActivityValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editReservationOrderedMemberID(int id) {
        return new Command() {
            int orderedMemberID;
            Reservation newReservation;

            @Override
            public void execute() {
                try {
                    newReservation = new Reservation(engine.getReservation(id));
                    orderedMemberID = getNumberFromUser();
                    newReservation.setOrderedMemberID(orderedMemberID);
                    engine.updateReservation(newReservation);
                }catch (Exceptions.ReservationNotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Exceptions.ReservationAlreadyApprovedException e){
                    System.out.println("Reservation Already Approved");
                } catch (Exceptions.IllegalActivityValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

}


