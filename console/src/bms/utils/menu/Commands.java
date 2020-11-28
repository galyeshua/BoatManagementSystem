package bms.utils.menu;

import bms.engine.BMSEngine;
import bms.engine.list.manager.Exceptions;
import bms.module.Boat;
import bms.module.Member;

import java.time.LocalDateTime;

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
                for (Boat boat : engine.getBoats())
                    System.out.println(boat);
            }
        };
    }


    public static Command addBoat(){
        return new Command() {
            boolean askForCoxswain = false;

            String boatName;
            Boat.Rowers numOfRowers;
            Boat.Paddles numOfPaddles;
            Boolean isPrivate;
            Boolean isWide;
            Boolean hasCoxswain;
            Boolean isMarine;
            Boolean isDisabled;

            private void askForValues(){
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
                    engine.addBoat( boatName, numOfRowers, numOfPaddles, isPrivate, isWide, hasCoxswain, isMarine, isDisabled);
                } catch (Exceptions.BoatAlreadyExistsException e){
                    System.out.println("Boat Already Exists");
                } catch (Exceptions.IllegalBoatValueException e){
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
            Boat boat;

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

            @Override
            public void execute() {
                try{
                    boatName = getStringFromUser();
                    engine.updateBoatName(serialNumber, boatName);

                } catch (Exceptions.BoatNotFoundException e){
                    System.out.println("Boat not found");
                }
            }
        };
    }

    public static Command editBoatNumOfPaddles(int serialNumber) {
        return new Command() {
            Boat.Paddles numOfPaddles;

            @Override
            public void execute() {
                try{
                    numOfPaddles = (Boat.Paddles) chooseFromOptins(Boat.Paddles.values());
                    engine.updateBoatNumOfPaddles(serialNumber, numOfPaddles);

                } catch (Exceptions.BoatNotFoundException e){
                    System.out.println("Boat not found");
                }
            }
        };
    }

    public static Command editBoatPrivate(int serialNumber) {
        return new Command() {
            boolean isPrivate;

            @Override
            public void execute() {
                try{
                    isPrivate = getBoolFromUser();
                    engine.updateBoatPrivate(serialNumber, isPrivate);

                } catch (Exceptions.BoatNotFoundException e){
                    System.out.println("Boat not found");
                }
            }
        };
    }

    public static Command editBoatCoxswain(int serialNumber) {
        return new Command() {
            boolean hasCoxswain;

            @Override
            public void execute() {
                try{
                    hasCoxswain = getBoolFromUser();
                    engine.updateBoatCoxswain(serialNumber, hasCoxswain);
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

            @Override
            public void execute() {
                try{
                    isMarine = getBoolFromUser();
                    engine.updateBoatMarine(serialNumber, isMarine);

                } catch (Exceptions.BoatNotFoundException e){
                    System.out.println("Boat not found");
                }
            }
        };
    }

    public static Command editBoatDisabled(int serialNumber) {
        return new Command() {
            boolean isDisabled;

            @Override
            public void execute() {
                try{
                    isDisabled = getBoolFromUser();
                    engine.updateBoatDisabled(serialNumber, isDisabled);

                } catch (Exceptions.BoatNotFoundException e){
                    System.out.println("Boat not found");
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
            private LocalDateTime joinDate;
            private LocalDateTime expireDate;
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
                joinDate = LocalDateTime.now();
                expireDate = LocalDateTime.now().plusYears(1);
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

//
//    public static Command updateBoat() {
//        return new Command() {
//            int serialNumber;
//            boolean askForCoxswain = false;
//            Boat oldBoat;
//
//            String boatName = null;
//            Boat.Rowers numOfRowers = null;
//            Boat.Paddles numOfPaddles = null;
//            Boolean isPrivate = null;
//            Boolean isWide = null;
//            Boolean hasCoxswain = null;
//            Boolean isMarine = null;
//            Boolean isDisabled = null;
//
//            private void chooseBoatToUpdate() throws Exceptions.BoatNotFoundException {
//                System.out.println("All the boats:");
//                printBoats().execute();
//                System.out.println("choose boat to edit");
//                serialNumber = getNumberFromUser();
//                oldBoat = engine.getBoat(serialNumber);
//            }
//
//            private void askForNewName(){
//                boatName = oldBoat.getName();
//                if(isTrue("Do you want to change Name?"))
//                    boatName = getStringFromUser();
//            }
//
//            private void askForNewNumOfRowers(){
//                numOfRowers = oldBoat.getNumOfRowers();
//                if(isTrue("Do you want to change rowers?"))
//                    numOfRowers = (Boat.Rowers) chooseFromOptins(Boat.Rowers.values());
//
//                if (numOfRowers.equals(Boat.Rowers.ONE)){
//                    hasCoxswain = false;
//                    askForCoxswain = false;
//                }
//
//                if (numOfRowers.equals(Boat.Rowers.EIGHT)){
//                    hasCoxswain = true;
//                    askForCoxswain = false;
//                }
//            }
//
//            private void askForNewNumOfPaddles(){
//                numOfPaddles = oldBoat.getNumOfPaddles();
//                if(isTrue("Do you want to change type of paddles?"))
//                    numOfPaddles = (Boat.Paddles) chooseFromOptins(Boat.Paddles.values());
//            }
//
//            private void askForNewPrivate(){
//                isPrivate = oldBoat.getPrivate();
//                if(isTrue("Do you want to change private?"))
//                    isPrivate = getBoolFromUser();
//            }
//
//            private void askForNewWide(){
//                isWide = oldBoat.getWide();
//                if(isTrue("Do you want to change Wide?"))
//                    isWide = getBoolFromUser();
//            }
//
//            private void askForNewCoxswain(){
//                hasCoxswain = oldBoat.getHasCoxswain();
//                if(isTrue("Do you want to change Coxswain?"))
//                    hasCoxswain = getBoolFromUser();
//            }
//
//            private void askForNewMarine(){
//                isMarine = oldBoat.getMarine();
//                if(isTrue("Do you want to change Marine?"))
//                    isMarine = getBoolFromUser();
//            }
//
//            private void askForNewDisabled(){
//                isDisabled = oldBoat.getDisabled();
//                if(isTrue("Do you want to change Disabled?"))
//                    isDisabled = getBoolFromUser();
//            }
//
//            private void askForValues(){
//                askForNewName();
//                askForNewNumOfRowers();
//                askForNewNumOfPaddles();
//                askForNewPrivate();
//                askForNewWide();
//                if (askForCoxswain)
//                    askForNewCoxswain();
//                askForNewMarine();
//                askForNewDisabled();
//            }
//
//            @Override
//            public void execute() {
//                try{
//                    chooseBoatToUpdate();
//                    askForValues();
//
//                    engine.updateBoat(serialNumber, boatName, numOfRowers, numOfPaddles, isPrivate, isWide,
//                            hasCoxswain, isMarine, isDisabled);
//
//                } catch (Exceptions.BoatNotFoundException e){
//                    System.out.println("Boat not found");
//                } catch (Exceptions.IllegalBoatValueExeption e){
//                    System.out.println("Error: " + e.getMessage());
//                }
//            }
//
//        };
//    }


}
