package bms.utils.menu;

import bms.engine.BMSEngine;
import bms.engine.list.manager.Exceptions;
import bms.module.Activity;
import bms.module.Boat;
import bms.module.BoatView;
import bms.module.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static bms.utils.InputUtils.*;

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
                for (BoatView boat : engine.getBoats())
                    System.out.println(boat);
            }
        };
    }


    public static Command addBoat(){
        return new Command() {
            boolean askForCoxswain = true;

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
                System.out.println("Enter Serial Number:");
                serialNumber = getNumberFromUser(1);
                System.out.println("Enter name:");
                boatName = getStringFromUser();
                System.out.println("How many rowers:");
                numOfRowers = (Boat.Rowers) chooseFromOptins(Boat.Rowers.values());
                if (numOfRowers.equals(Boat.Rowers.ONE)){
                    hasCoxswain = false;
                    askForCoxswain = false;
                }

                if (numOfRowers.equals(Boat.Rowers.EIGHT)){
                    hasCoxswain = true;
                    askForCoxswain = false;
                }
                System.out.println("choose type of paddles:");
                numOfPaddles = (Boat.Paddles) chooseFromOptins(Boat.Paddles.values());
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
                    numOfPaddles = (BoatView.Paddles) chooseFromOptins(BoatView.Paddles.values());
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
            Member member;

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
                level = (Member.Level) chooseFromOptins(Member.Level.values());
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
                for (Member member : engine.getMembers())
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
            String MemberName;

            @Override
            public void execute() {
                try{
                    MemberName = getStringFromUser();
                    engine.updateMemberName(serialNumber, MemberName);

                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                }
            }
        };
    }

    public static Command editMemberAge(int serialNumber) {
        return new Command() {
            int MemberAge;

            @Override
            public void execute() {
                try{
                    MemberAge = getNumberFromUser(16, 99);
                    engine.updateMemberAge(serialNumber, MemberAge);

                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                }
            }
        };
    }

    public static Command editMemberNotes(int serialNumber) {
        return new Command() {
            String MemberNotes;

            @Override
            public void execute() {
                try{
                    MemberNotes = getStringFromUser();
                    engine.updateMemberNotes(serialNumber, MemberNotes);

                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                }
            }
        };
    }


    public static Command editMemberLevel(int serialNumber) {
        return new Command() {
            Member.Level MemberLevel;

            @Override
            public void execute() {
                try{
                    MemberLevel = (Member.Level) chooseFromOptins(Member.Level.values());
                    engine.updateMemberLevel(serialNumber, MemberLevel);

                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                }
            }
        };
    }

    public static Command editMemberPrivateBoat(int serialNumber) {
        return new Command() {
            boolean hasPrivateBoat;

            @Override
            public void execute() {
                try{
                    hasPrivateBoat = getBoolFromUser();
                    engine.updateMemberPrivateBoat(serialNumber, hasPrivateBoat);
                    if (hasPrivateBoat){
                        System.out.println("Enter Boat Serial Number:");
                        int boatSerialNumber = getNumberFromUser(1, 99);
                        engine.updateBoatSerialNumber(serialNumber,boatSerialNumber);
                    }
                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                }
            }
        };
    }

    public static Command editMemberPhone(int serialNumber) {
        return new Command() {
            String MemberPhone;

            @Override
            public void execute() {
                try{
                    MemberPhone = getStringFromUser();
                    engine.updateMemberPhone(serialNumber, MemberPhone);

                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                }
            }
        };
    }

    public static Command editMemberEmail(int serialNumber) {
        return new Command() {
            String MemberEmail;

            @Override
            public void execute() {
                try{
                    MemberEmail = getStringFromUser();
                    engine.updateMemberEmail(serialNumber, MemberEmail);

                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                }
            }
        };
    }

    public static Command editMemberPassword(int serialNumber) {
        return new Command() {
            String MemberPassword;

            @Override
            public void execute() {
                try{
                    MemberPassword = getStringFromUser();
                    engine.updateMemberPassword(serialNumber, MemberPassword);

                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                }
            }
        };
    }

    public static Command editMemberRole(int serialNumber) {
        return new Command() {
            boolean memberRole;

            @Override
            public void execute() {
                try{
                    memberRole = getBoolFromUser();
                    engine.updateMemberRole(serialNumber, memberRole);

                } catch (Exceptions.MemberNotFoundException e){
                    System.out.println("Member not found");
                }
            }
        };
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Command chooseAndEditActivity() {
        return new Command() {
            Activity activity;
            int id;

            private void chooseActivityToUpdate() throws Exceptions.ActivityNotFoundException {
                printActivities().execute();
                System.out.println("choose activity to edit");

                ArrayList<Activity> activities = (ArrayList<Activity>)engine.getActivities();
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
                System.out.println("Enter hour:");
                int hour = getNumberFromUser(5,24 );
                System.out.println("Enter minute:");
                int minute = getNumberFromUser(0,59);
                startTime = LocalTime.of(hour,minute);
                System.out.println("Enter finishTime:");
                System.out.println("Enter hour:");
                hour = getNumberFromUser(5,24 );
                System.out.println("Enter minute:");
                minute = getNumberFromUser(0,59);
                finishTime = LocalTime.of(hour,minute);
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
                for (Activity activity : engine.getActivities()){
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
                ArrayList<Activity> activities = (ArrayList<Activity>)engine.getActivities();
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
            String Name;

            @Override
            public void execute() {
                try{
                    Name = getStringFromUser();
                    engine.updateActivityName(id, Name);

                } catch (Exceptions.ActivityNotFoundException e){
                    System.out.println("Activity not found");
                }
            }
        };
    }

    public static Command editActivityStartTime(int id) {
        return new Command() {
            LocalTime StartTime;

            @Override
            public void execute() {
                try{
                    System.out.println("Enter hour:");
                    int hour = getNumberFromUser(5,24 );
                    System.out.println("Enter minute:");
                    int minute = getNumberFromUser(0,59);
                    StartTime = LocalTime.of(hour,minute);
                    engine.updateActivityStartTime(id, StartTime);

                } catch (Exceptions.ActivityNotFoundException e){
                    System.out.println("Activity not found");
                }
            }
        };
    }

    public static Command editActivityFinishTime(int id) {
        return new Command() {
            LocalTime FinishTime;

            @Override
            public void execute() {
                try{
                    System.out.println("Enter hour:");
                    int hour = getNumberFromUser(5,24 );
                    System.out.println("Enter minute:");
                    int minute = getNumberFromUser(0,59);
                    FinishTime = LocalTime.of(hour,minute);
                    engine.updateActivityFinishTime(id, FinishTime);

                } catch (Exceptions.ActivityNotFoundException e){
                    System.out.println("Activity not found");
                }
            }
        };
    }

    public static Command editActivityBoatType(int id) {
        return new Command() {
            String boatType;

            @Override
            public void execute() {
                try{
                    boatType = getStringFromUser();
                    engine.updateActivityBoatType(id, boatType);

                } catch (Exceptions.ActivityNotFoundException e){
                    System.out.println("Activity not found");
                }
            }
        };
    }


}
