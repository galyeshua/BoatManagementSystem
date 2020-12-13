package bms.utils.menu;

import bms.engine.BMSEngine;
import bms.engine.Exceptions;
import bms.module.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static bms.utils.InputUtils.*;
import static bms.utils.printFormat.*;

public class Commands {
    public static BMSEngine engine;
    static MemberView user;

    public static void setEngine(BMSEngine engine) {
        Commands.engine = engine;
    }
    public static void setLoggedInUser(MemberView user) {
        Commands.user = user;
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
                if (engine.getBoats().isEmpty())
                    System.out.println("No Boats");
                else {
                    int i = 0;
                    for (BoatView boat : engine.getBoats()) {
                        System.out.print("[" + i + "] ");
                        printBoatForManager(boat);
                        i++;

                    }

                }
            }
        };
    }


    public static Command addBoat(){
        return new Command() {
            Boat boat;

            private void askForRequiredDataAndCreateBoat() throws Boat.IllegalValueException {
                System.out.println("Enter Serial Number:");
                int serialNumber = getNumberFromUser(1);
                System.out.println("Enter Boat name:");
                String boatName = getStringFromUser();
                System.out.println("Choose boat type:");
                Boat.BoatType boatType = (Boat.BoatType) chooseFromOptions(Boat.BoatType.values());

                boat = new Boat(serialNumber, boatName, boatType);
            }

            private void askForOptionalValues(){
                System.out.println("is private?");
                boat.setPrivate(getBoolFromUser());
                System.out.println("is Wide?");
                boat.setWide(getBoolFromUser());
                System.out.println("is Marine?");
                boat.setMarine(getBoolFromUser());
                System.out.println("is Disabled?");
                boat.setDisabled(getBoolFromUser());
            }

            @Override
            public void execute() {
                try{
                    askForRequiredDataAndCreateBoat();
                    askForOptionalValues();
                    engine.addBoat(boat);
                } catch (Boat.AlreadyExistsException | Boat.IllegalValueException e){
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
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                }catch (Boat.AlreadyAllocatedException e){
                    System.out.println("The boat is allocated for an approved reservation");
                }

            }
        };
    }


    public static Command chooseAndEditBoat() {
        return new Command() {
            int serialNumber;
            BoatView boat;

            private void chooseBoatToUpdate() throws Boat.NotFoundException {
                System.out.println("All the boats:");
                printBoats().execute();
                System.out.println("choose boat to edit by entering a serial number ");
                serialNumber = getNumberFromUser();
                boat = engine.getBoat(serialNumber);
                if (boat == null)
                    throw new Boat.NotFoundException();
            }

            @Override
            public void execute() {
                try{
                    chooseBoatToUpdate();
                    new MenuUtils.openEditBoatMenu(serialNumber).execute();
                } catch (Boat.NotFoundException e){
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
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Boat.IllegalValueException | Boat.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Boat already allocated to another reservation");
                } catch (Boat.BelongsToMember e){
                    System.out.println("The boat belongs to a member, please edit member's private boat first");
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
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Boat.IllegalValueException | Boat.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Boat already allocated to another reservation");
                } catch (Boat.BelongsToMember e){
                    System.out.println("The boat belongs to a member, please edit member's private boat first");
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
                    System.out.println("Does the boat private?");
                    isPrivate = getBoolFromUser();
                    newBoat.setPrivate(isPrivate);
                    engine.updateBoat(newBoat);
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Boat.IllegalValueException | Boat.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Boat already allocated to another reservation");
                } catch (Boat.BelongsToMember e){
                    System.out.println("The boat belongs to a member, please edit member's private boat first");
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
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Boat.IllegalValueException | Boat.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Boat already allocated to another reservation");
                } catch (Boat.BelongsToMember e){
                    System.out.println("The boat belongs to a member, please edit member's private boat first");
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
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Boat.IllegalValueException | Boat.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Boat already allocated to another reservation");
                } catch (Boat.BelongsToMember e){
                    System.out.println("The boat belongs to a member, please edit member's private boat first");
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
                } catch (Boat.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Boat.IllegalValueException | Boat.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Boat already allocated to another reservation");
                } catch (Boat.BelongsToMember e){
                    System.out.println("The boat belongs to a member, please edit member's private boat first");
                }
            }
        };
    }


    public static Command chooseAndEditMember() {
        return new Command() {
            int serialNumber;
            MemberView member;

            private void chooseMemberToUpdate() throws Member.NotFoundException {
                System.out.println("All the members:");
                printMembers().execute();
                System.out.println("Choose member to edit by entering a Serial Number");
                serialNumber = getNumberFromUser();
                member = engine.getMember(serialNumber);
                if (member == null)
                    throw new Member.NotFoundException();
            }

            @Override
            public void execute() {
                try{
                    chooseMemberToUpdate();
                    new MenuUtils.openEditMemberMenu(serialNumber).execute();
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                }
            }
        };
    }

    public static Command addMember() {
        return new Command() {
            Member member;

            private void askForRequiredDataAndCreateMember() throws Member.IllegalValueException {
                System.out.println("Enter Serial Number:");
                int serialNumber = getNumberFromUser(1);
                System.out.println("Enter name:");
                String name = getStringFromUser();
                System.out.println("Enter email:");
                String email = getStringFromUser();
                System.out.println("Enter password:");
                String password = getStringFromUser();

                member = new Member(serialNumber, name, email, password);
            }

            private void askForOptionalValues() throws Member.IllegalValueException {
                System.out.println("Enter Age:");
                member.setAge(getNumberFromUser(1));
                System.out.println("Enter Level:");
                member.setLevel((Member.Level) chooseFromOptions(Member.Level.values()));
                System.out.println("is manager?");
                member.setManager(getBoolFromUser());
                System.out.println("Enter Notes:");
                member.setNotes(getStringFromUser());
                System.out.println("Enter phone number:");
                member.setPhoneNumber(getStringFromUser());
                System.out.println("Does member have private boat?");
                if (getBoolFromUser()){
                    System.out.println("Enter Boat Serial Number:");
                    int boatSerialNumber = getNumberFromUser(1);
                    member.setBoatSerialNumber(boatSerialNumber);
                } else
                    member.setBoatSerialNumber(null);
            }

            @Override
            public void execute() {
                try{
                    askForRequiredDataAndCreateMember();
                    askForOptionalValues();
                    engine.addMember(member);
                } catch (Member.AlreadyExistsException | Member.IllegalValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command printMembers() {
        return new Command() {
            @Override
            public void execute() {
                if (engine.getMembers().isEmpty())
                    System.out.println("No Members");
                else {
                    int i = 0;
                    for (MemberView member : engine.getMembers()) {
                        System.out.print("[" + i + "] ");
                        printMemberForManager(member);
                        i++;
                    }
                }
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
                } catch (Member.NotFoundException | Member.AlreadyHaveApprovedReservationsException e){
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
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
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

                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
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
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
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
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editMemberPrivateBoat(int serialNumber) {
        return new Command() {
            Member newMember;

            @Override
            public void execute() {
                try{
                    newMember = new Member(engine.getMember(serialNumber));
                    System.out.println("Does member have private boat?");

                    if (getBoolFromUser()){
                        System.out.println("Enter Boat Serial Number:");
                        int boatSerialNumber = getNumberFromUser(1);
                        newMember.setBoatSerialNumber(boatSerialNumber);
                    } else
                        newMember.setBoatSerialNumber(null);
                    engine.updateMember(newMember);
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
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
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
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
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
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
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
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
                } catch (Member.NotFoundException e){
                    System.out.println("Member not found");
                } catch (Member.IllegalValueException | Member.AlreadyExistsException e){
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

            private void chooseActivityToUpdate() throws Activity.NotFoundException {
                printActivities().execute();
                System.out.println("choose activity to edit");
                ArrayList<ActivityView> activities = new ArrayList<ActivityView>(engine.getActivities());

                int activityIndex = getNumberFromUser(0, activities.size() - 1);
                id = activities.get(activityIndex).getId();
                activity = engine.getActivity(id);

                if (activity == null)
                    throw new Activity.NotFoundException();
            }

            @Override
            public void execute() {
                try{
                    chooseActivityToUpdate();
                    new MenuUtils.openEditActivityMenu(id).execute();
                } catch (Activity.NotFoundException e){
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
            private BoatView.BoatType boatType;


            private void askForValues(){
                boatType = null;

                System.out.println("Enter Name:");
                name = getStringFromUser();
                System.out.println("Enter startTime:");
                startTime = getLocalTimeFromUser();
                System.out.println("Enter finishTime:");
                finishTime = getLocalTimeFromUser();
                System.out.println("do you want to specify boat type?");
                if(getBoolFromUser())
                    boatType = (BoatView.BoatType) chooseFromOptions(BoatView.BoatType.values());
            }

            @Override
            public void execute() {
                try{
                    askForValues();
                    Activity activity = new Activity(name,startTime,finishTime);
                    activity.setBoatType(boatType);
                    engine.addActivity(activity);
                } catch (Activity.AlreadyExistsException | Activity.IllegalValueException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command printActivities() {
        return new Command() {
            @Override
            public void execute() {
                if (engine.getActivities().isEmpty())
                    System.out.println("No Activities");
                else {
                    int i = 0;
                    for (ActivityView activity : engine.getActivities()) {
                        System.out.println("[" + i + "] " + activity.getStartTime() + " - " + activity.getFinishTime()
                                + ". Activity name: " + activity.getName() + ((activity.getBoatType() != null) ? " (Boat type: " + activity.getBoatType() + ")" : ""));
                        i++;
                    }
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
                } catch (Activity.NotFoundException e){
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
                } catch (Activity.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Activity.IllegalValueException | Activity.AlreadyExistsException e){
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
                } catch (Activity.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Activity.IllegalValueException | Activity.AlreadyExistsException e){
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
                    newActivity.setFinishTime(finishTime);
                    engine.updateActivity(newActivity);
                } catch (Activity.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Activity.IllegalValueException | Activity.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }

    public static Command editActivityBoatType(int id) {
        return new Command() {
            BoatView.BoatType boatType;
            Activity newActivity;

            @Override
            public void execute() {
                try{
                    boatType = null;
                    newActivity = new Activity(engine.getActivity(id));
                    System.out.println("Do you want to specify boat type?");
                    if(getBoolFromUser())
                        boatType = (BoatView.BoatType) chooseFromOptions(BoatView.BoatType.values());
                    newActivity.setBoatType(boatType);
                    engine.updateActivity(newActivity);
                } catch (Activity.NotFoundException e){
                    System.out.println("Boat not found");
                } catch (Activity.IllegalValueException | Activity.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        };
    }


    //////////////////////////////////////////////////////////////////////



    public static Command chooseAndEditReservationForCurrentUser() {
        return new Command() {
            int id;
            ReservationView reservation;

            private void chooseReservationToUpdate() throws Reservation.NotFoundException {
                System.out.println("Choose reservation:");
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(engine.getFutureUnapprovedReservationsForCurrentUser());
                if (reservations.isEmpty())
                    throw new Exceptions.EmptyReservationListException();

                int i = 0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }

                System.out.println("choose reservation to edit");
                int reservationIndex = getNumberFromUser(0, reservations.size() - 1);
                id = reservations.get(reservationIndex).getId();
                reservation = engine.getReservation(id);

                if (reservation == null)
                    throw new Reservation.NotFoundException();
            }
           // openEditReservationMenu
            @Override
            public void execute() {
                try{
                    chooseReservationToUpdate();
                    new MenuUtils.openEditReservationMenu(id, false).execute();
                } catch (Reservation.NotFoundException e){
                    System.out.println("Reservation not found");
                } catch (Exceptions.EmptyReservationListException e){
                    System.out.println("No Reservations found");
                }
            }
        };
    }


    public static Command chooseAndEditReservationForManager() {
        return new Command() {
            int id;
            ReservationView reservation;

            private void chooseReservationToUpdate() throws Reservation.NotFoundException {
                System.out.println("Choose reservation:");
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(engine.getUnapprovedReservationsForWeek(LocalDate.now()));
                if (reservations.isEmpty())
                    throw new Exceptions.EmptyReservationListException();

                int i=0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }

                System.out.println("choose reservation to edit");
                int reservationIndex = getNumberFromUser(0, reservations.size() - 1);
                id = reservations.get(reservationIndex).getId();
                reservation = engine.getReservation(id);

                if (reservation == null)
                    throw new Reservation.NotFoundException();
            }
            @Override
            public void execute() {
                try{
                    chooseReservationToUpdate();
                    new MenuUtils.openEditReservationMenu(id, true).execute();
                } catch (Reservation.NotFoundException e){
                    System.out.println("Reservation not found");
                } catch (Exceptions.EmptyReservationListException e){
                    System.out.println("No Reservations found");
                }
            }
        };
    }



    public static Command chooseAndUnapproveReservationForManager() {
        return new Command() {
            int id;
            ReservationView reservation;

            private void chooseReservationToUnapprove() throws Reservation.NotFoundException {
                System.out.println("Choose reservation:");
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(engine.getApprovedReservationsForWeek(LocalDate.now()));
                if (reservations.isEmpty())
                    throw new Exceptions.EmptyReservationListException();

                int i=0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }

                System.out.println("choose reservation to edit");
                int reservationIndex = getNumberFromUser(0, reservations.size() - 1);
                id = reservations.get(reservationIndex).getId();
                reservation = engine.getReservation(id);

                if (reservation == null)
                    throw new Reservation.NotFoundException();
            }
            @Override
            public void execute() {
                try{
                    chooseReservationToUnapprove();
                    if(engine.getBoat(reservation.getAllocatedBoatID()).getPrivate())
                        throw new Boat.BelongsToMember();
                    engine.unapprovedReservation(id);
                    System.out.println("successfully unapproved reservation");
                } catch (Reservation.NotFoundException e){
                    System.out.println("Reservation not found");
                } catch (Exceptions.EmptyReservationListException e){
                    System.out.println("No Reservations found");
                } catch (Member.AccessDeniedException e) {
                    System.out.println("You dont have permissions to do that");
                } catch (Boat.BelongsToMember belongsToMember) {
                    System.out.println("cannot unapprove reservation with private boat");
                }
            }
        };
    }

    public static Command addReservation() {
        return new Command() {
            private Activity activity;
            private LocalDate activityDate;
            private List<Boat.Rowers> boatType;
            private List<Integer> participants;
            private LocalDateTime orderDate = LocalDateTime.now();
            private int orderedMemberID = user.getSerialNumber();

            private List<ActivityView> activities = new ArrayList<ActivityView>(engine.getActivities());
            List<MemberView> members;

            private int getMemberSerialNumberFromListByName(List<MemberView> members, String name){
                if(members.size() == 1)
                    return members.get(0).getSerialNumber();

                int i=0;
                for (MemberView member : members){
                    System.out.println("[" + i + "] " + member.getName() + " - " + member.getAge());
                    i++;
                }
                int memberIndex = getNumberFromUser(0, members.size() - 1);
                return members.get(memberIndex).getSerialNumber();
            }

            private String getNameFromUser() {
                String name;
                do{
                    System.out.println("Enter Member name");
                    name = getStringFromUser();
                    members = new ArrayList<MemberView>(engine.getMembers(name));
                    if(members.isEmpty())
                        System.out.println("Could not find member");
                } while(members.isEmpty());
                return name;
            }

            private void askForValues() throws Activity.IllegalValueException {
                String name;
                boatType = new ArrayList<Boat.Rowers>();
                participants = new ArrayList<Integer>();

                System.out.println("Do the reservation is for you?");
                if(getBoolFromUser())
                    participants.add(user.getSerialNumber());
                else{
                    name=getNameFromUser();
                    participants.add(getMemberSerialNumberFromListByName(members, name));
                }

                if (activities.isEmpty()){
                    System.out.println("Enter startTime:");
                    LocalTime startTime = getLocalTimeFromUser();
                    System.out.println("Enter finishTime:");
                    LocalTime finishTime = getLocalTimeFromUser();

                    activity = new Activity(startTime, finishTime);
                } else {
                    System.out.println("Available activities:");
                    printActivities().execute();
                    System.out.println("choose number from the list");
                    int activityIndex = getNumberFromUser(0, activities.size() - 1);

                    activity = new Activity(activities.get(activityIndex));
                }

                System.out.println("Enter activity Date:");
                activityDate = getLocalDateFromUser();

                do {
                    System.out.println("Enter boat Type:");
                    boatType.add((Boat.Rowers) chooseFromOptions(Boat.Rowers.values()));
                    System.out.println("Do you want to add more types?");
                } while(getBoolFromUser());

                if (!(boatType.contains(BoatView.Rowers.ONE) && boatType.size() == 1)){
                    System.out.println("Do you want to add rowers?");
                    while(getBoolFromUser()){
                        name=getNameFromUser();
                        participants.add(getMemberSerialNumberFromListByName(members, name));
                        System.out.println("Do you want to add more rowers?");
                    }
                }

            }

            @Override
            public void execute() {
               try{
                    askForValues();
                    Reservation reservation = new Reservation(activity, activityDate, orderDate, orderedMemberID);
                    for (Integer memberID: participants)
                        reservation.addParticipant(memberID);

                    for (BoatView.Rowers boatType: boatType)
                        reservation.addBoatType(boatType);

                    engine.addReservation(reservation);
               } catch (Reservation.AlreadyExistsException | Activity.IllegalValueException |
                       Member.AlreadyExistsException | Reservation.IllegalValueException | Boat.IllegalValueException e){
                   System.out.println("Error: " + e.getMessage());
               } catch (Reservation.NotFoundException e) {
                   System.out.println("Reservation not found");
               } catch (Reservation.AlreadyApprovedException e) {
                   System.out.println("Reservation already approved");
               }
            }
        };
    }

    public static Command printReservations(LocalDate date) {
        return new Command() {
            @Override
            public void execute() {
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(engine.getReservationsByDate(date));
                if (reservations.isEmpty())
                    System.out.println("No reservations for this date");
                int i = 0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }
            }
        };
    }


    public static Command printReservationsForWeek(LocalDate startDate) {
        return new Command() {
            @Override
            public void execute() {
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(engine.getReservationsForWeek(startDate));
                if (reservations.isEmpty())
                    System.out.println("No reservations for this week");
                int i = 0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }
            }
        };
    }


    public static Command printUnapprovedReservations(LocalDate date) {
        return new Command() {
            @Override
            public void execute() {
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(engine.getUnapprovedReservationsByDate(date));
                if (reservations.isEmpty())
                    System.out.println("No Unapproved reservations for this date");
                int i = 0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }
            }
        };
    }


    public static Command printUnapprovedReservationsForWeek(LocalDate startDate) {
        return new Command() {
            @Override
            public void execute() {
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(engine.getUnapprovedReservationsForWeek(startDate));
                if (reservations.isEmpty())
                    System.out.println("No Unapproved reservations for this week");
                int i = 0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }
            }
        };
    }

    public static Command printApprovedReservations(LocalDate date) {
        return new Command() {
            @Override
            public void execute() {
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(engine.getApprovedReservationsByDate(date));
                if (reservations.isEmpty())
                    System.out.println("No Approved reservations for today");
                int i = 0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }
            }
        };
    }

    public static Command printFutureReservationForCurrentUser() {
        return new Command() {
            @Override
            public void execute() {
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(engine.getFutureReservationsForCurrentUser());
                if (reservations.isEmpty())
                    System.out.println("No reservations");
                int i = 0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }
            }
        };
    }

    public static Command printReservationHistoryForCurrentUser() {
        return new Command() {
            @Override
            public void execute() {
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(engine.getReservationsHistoryForCurrentUser());
                if (reservations.isEmpty())
                    System.out.println("No reservations");
                int i = 0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }
            }
        };
    }



    public static Command deleteReservation() {
        return new Command() {
            int serialNumber;

            @Override
            public void execute() {
                printReservationsForWeek(LocalDate.now()).execute();
                System.out.println("choose Reservation to delete");
                serialNumber = getNumberFromUser();

                try{
                    engine.deleteReservation(serialNumber);
                } catch (Reservation.NotFoundException e){
                    System.out.println("Reservation not found");
                } catch (Reservation.AlreadyApprovedException e){
                    System.out.println("Reservation already approved");
                }
            }
        };
    }


    public static Command editReservationActivity(int id) {
        return new Command() {
            Activity activity;
            Reservation newReservation;
            private List<ActivityView> activities = new ArrayList<ActivityView>(engine.getActivities());

            private void askForActivity() throws Activity.IllegalValueException {
                if (activities.isEmpty()){
                    System.out.println("Enter startTime:");
                    LocalTime startTime = getLocalTimeFromUser();
                    System.out.println("Enter finishTime:");
                    LocalTime finishTime = getLocalTimeFromUser();

                    activity = new Activity(startTime, finishTime);
                } else {
                    printActivities().execute();



                    System.out.println("choose activity:");
                    int activityIndex = getNumberFromUser(0, activities.size() - 1);

                    activity = new Activity(activities.get(activityIndex));
                }
            }

            @Override
            public void execute() {
                try {
                    newReservation = new Reservation(engine.getReservation(id));
                    askForActivity();
                    newReservation.setActivity(activity);
                    engine.updateReservation(newReservation);
                }catch (Reservation.NotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Reservation.IllegalValueException | Member.AccessDeniedException |
                        Activity.IllegalValueException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Reservation.AlreadyApprovedException e){
                    System.out.println("Cannot edit Approved Reservation" );
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
                }catch (Reservation.NotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Reservation.IllegalValueException | Member.AccessDeniedException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Reservation.AlreadyApprovedException e){
                    System.out.println("Cannot edit Approved Reservation" );
                }
            }
        };
    }


    public static Command showReservationBoatType(int id) {
        return new Command() {
            @Override
            public void execute() {
                ReservationView reservation = engine.getReservation(id);
                List<BoatView.Rowers> boatTypes = reservation.getBoatType();

                System.out.println("All types");
                for (BoatView.Rowers boatType : boatTypes)
                    System.out.println(boatType);
            }
        };
    }

    public static Command addReservationBoatType(int id) {
        return new Command() {
            Reservation newReservation;

            @Override
            public void execute() {
                try {
                    newReservation = new Reservation(engine.getReservation(id));
                    System.out.println("Enter new type");
                    newReservation.addBoatType((Boat.Rowers) chooseFromOptions(Boat.Rowers.values()));
                    engine.updateReservation(newReservation);
                }catch (Reservation.NotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Reservation.IllegalValueException | Member.AccessDeniedException |
                        Boat.IllegalValueException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Reservation.AlreadyApprovedException e){
                    System.out.println("Cannot edit Approved Reservation" );
                }
            }
        };
    }

    public static Command deleteReservationBoatType(int id) {
        return new Command() {
            Reservation newReservation;

            @Override
            public void execute() {
                try {
                    newReservation = new Reservation(engine.getReservation(id));
                    System.out.println("choose type to delete");
                    newReservation.deleteBoatType((Boat.Rowers) chooseFromOptions(newReservation.getBoatType().toArray()));
                    engine.updateReservation(newReservation);
                }catch (Reservation.NotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Reservation.IllegalValueException | Member.AccessDeniedException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Reservation.AlreadyApprovedException e){
                    System.out.println("Cannot edit Approved Reservation" );
                } catch (Exceptions.ListCannotBeEmptyException e){
                    System.out.println("List must have at least one type" );
                }
            }
        };
    }

    public static Command showReservationPerticipents(int id) {
        return new Command() {
            @Override
            public void execute() {
                ReservationView reservation = engine.getReservation(id);
                List<Integer> participants = reservation.getParticipants();

                System.out.println("All Rowers:");
                for (int memberID : participants)
                    printParticipantsShort(memberID);
            }
        };
    }

    public static Command addReservationPerticipent(int id) {
        return new Command() {
            Reservation newReservation;
            String name;
            List<MemberView> members;

            private int getMemberSerialNumberFromListByName(List<MemberView> members, String name){
                if(members.size() == 1)
                    return members.get(0).getSerialNumber();

                int i=0;
                for (MemberView member : members){
                    System.out.println("[" + i + "] " + member.getName() + " - " + member.getAge());
                    i++;
                }
                int memberIndex = getNumberFromUser(0, members.size() - 1);
                return members.get(memberIndex).getSerialNumber();
            }

            private String getNameFromUser(){
                String name;
                do{
                    System.out.println("Enter Member name");
                    name = getStringFromUser();
                    members = new ArrayList<MemberView>(engine.getMembers(name));
                    if(members.isEmpty())
                        System.out.println("Could not find member");
                } while(members.isEmpty());
                return name;
            }

            @Override
            public void execute() {
                try {
                    newReservation = new Reservation(engine.getReservation(id));
                    System.out.println("Enter new member");
                    name=getNameFromUser();
                    newReservation.addParticipant(getMemberSerialNumberFromListByName(members, name));
                    engine.updateReservation(newReservation);
                }catch (Reservation.NotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Reservation.IllegalValueException | Member.AlreadyExistsException e){
                        System.out.println("Error: " + e.getMessage());
                } catch (Reservation.AlreadyApprovedException e){
                    System.out.println("Cannot edit Approved Reservation" );
                } catch (Member.AccessDeniedException e ){
                    System.out.println("You dont have perrmissions to do that");
                }
            }
        };
    }


    public static Command deleteReservationPerticipent(int id) {
        return new Command() {
            Reservation newReservation;

            @Override
            public void execute() {
                try {
                    newReservation = new Reservation(engine.getReservation(id));
                    System.out.println("choose rower to delete");
                    int i=0;
                    for (int memberID : newReservation.getParticipants()){
                        System.out.print("[" + i + "] ");
                        printParticipantsShort(memberID);
                        i++;
                    }
                    int memberIndex = getNumberFromUser(0, newReservation.getParticipants().size()-1);
                    newReservation.deleteParticipant(newReservation.getParticipants().get(memberIndex));
                    engine.updateReservation(newReservation);
                }catch (Reservation.NotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Reservation.IllegalValueException | Member.AccessDeniedException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Reservation.AlreadyApprovedException e){
                    System.out.println("Cannot edit Approved Reservation" );
                } catch (Exceptions.ListCannotBeEmptyException e){
                    System.out.println("List must have at least one participant" );
                }
            }
        };
    }

    private static void printErrorsAfterLoading(){
        if (engine.getXmlImportErrors().size() > 0){
            System.out.println("The following errors occurred:");
            for (String error : engine.getXmlImportErrors())
                System.out.println(error);
        } else
            System.out.println("Data loaded successfully");
    }


    public static Command addActivitiesFromFile() {
        return new Command() {
            String filePath;
            @Override
            public void execute() {
                System.out.println("Enter file path");
                filePath = getStringFromUser();
                try{
                    engine.loadActivitiesFromFile(filePath);
                    printErrorsAfterLoading();
                } catch (Exceptions.IllegalFileTypeException e){
                    System.out.println("File must be in xml format");
                } catch (Exceptions.FileNotFoundException e){
                    System.out.println("Cant find file " + filePath);
                } catch (JAXBException e) {
                    System.out.println("Error: file is not valid. " + e.getLinkedException().getMessage());
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static Command replaceActivitiesFromFile() {
        return new Command() {
            String filePath;
            boolean areYouSure;
            @Override
            public void execute() {
                System.out.println("Warning: this operation will erase all reservations. Do you want to continue?");
                areYouSure = getBoolFromUser();

                if (areYouSure){
                    System.out.println("Enter file path");
                    filePath = getStringFromUser();
                    try{
                        engine.eraseAndLoadActivitiesFromFile(filePath);
                        printErrorsAfterLoading();
                    } catch (Exceptions.IllegalFileTypeException e){
                        System.out.println("File must be in xml format");
                    } catch (Exceptions.FileNotFoundException e){
                        System.out.println("Cant find file " + filePath);
                    } catch (JAXBException e) {
                        e.getLinkedException().getMessage();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }


    public static Command exportActivitiesToFile() {
        return new Command() {
            String filePath;
            @Override
            public void execute() {
                System.out.println("Enter file path include name and extension");
                filePath = getStringFromUser();
                try {
                    engine.saveActivitiesToFile(filePath);
                    System.out.println("successfully saved");
                } catch (Exceptions.FileAlreadyExistException e){
                    System.out.println("Error: File with the same name already exist at this location. cannot export data.");
                } catch (JAXBException e) {
                    System.out.println(e.getLinkedException().getMessage());
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    public static Command addBoatsFromFile() {
        return new Command() {
            String filePath;
            @Override
            public void execute() {
                System.out.println("Enter file path");
                filePath = getStringFromUser();
                try{
                    engine.loadBoatsFromFile(filePath);
                    printErrorsAfterLoading();
                } catch (Exceptions.IllegalFileTypeException e){
                    System.out.println("File must be in xml format");
                } catch (Exceptions.FileNotFoundException e){
                    System.out.println("Cant find file " + filePath);
                } catch (JAXBException e) {
                    e.getLinkedException().getMessage();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static Command replaceBoatsFromFile() {
        return new Command() {
            String filePath;
            boolean areYouSure;
            @Override
            public void execute() {
                System.out.println("Warning: this operation will erase all reservations. Do you want to continue?");
                areYouSure = getBoolFromUser();

                if (areYouSure){
                    System.out.println("Enter file path");
                    filePath = getStringFromUser();
                    try{
                        engine.eraseAndLoadBoatsFromFile(filePath);
                        printErrorsAfterLoading();
                    } catch (Exceptions.IllegalFileTypeException e){
                        System.out.println("File must be in xml format");
                    } catch (Exceptions.FileNotFoundException e){
                        System.out.println("Cant find file " + filePath);
                    } catch (JAXBException e) {
                        e.getLinkedException().getMessage();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public static Command exportBoatsToFile() {
        return new Command() {
            String filePath;
            @Override
            public void execute() {
                System.out.println("Enter file path include name and extension");
                filePath = getStringFromUser();
                try {
                    engine.saveBoatsToFile(filePath);
                    System.out.println("successfully saved");
                } catch (Exceptions.FileAlreadyExistException e){
                    System.out.println("Error: File with the same name already exist at this location. cannot export data.");
                } catch (JAXBException e) {
                    System.out.println(e.getLinkedException().getMessage());
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static Command addMembersFromFile() {
        return new Command() {
            String filePath;
            @Override
            public void execute() {
                System.out.println("Enter file path");
                filePath = getStringFromUser();
                try{
                    engine.loadMembersFromFile(filePath);
                    printErrorsAfterLoading();
                } catch (Exceptions.IllegalFileTypeException e){
                    System.out.println("File must be in xml format");
                } catch (Exceptions.FileNotFoundException e){
                    System.out.println("Cant find file " + filePath);
                } catch (JAXBException e) {
                    e.getLinkedException().getMessage();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static Command replaceMembersFromFile() {
        return new Command() {
            String filePath;
            boolean areYouSure;
            @Override
            public void execute() {
                System.out.println("Warning: this operation will erase all reservations. Do you want to continue?");
                areYouSure = getBoolFromUser();

                if (areYouSure){
                    System.out.println("Enter file path");
                    filePath = getStringFromUser();
                    try{
                        engine.eraseAndLoadMembersFromFile(filePath);
                        printErrorsAfterLoading();
                    } catch (Exceptions.IllegalFileTypeException e){
                        System.out.println("File must be in xml format");
                    } catch (Exceptions.FileNotFoundException e){
                        System.out.println("Cant find file " + filePath);
                    } catch (JAXBException e) {
                        e.getLinkedException().getMessage();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public static Command exportMembersToFile() {
        return new Command() {
            String filePath;
            @Override
            public void execute() {
                System.out.println("Enter file path include name and extension");
                filePath = getStringFromUser();
                try {
                    engine.saveMembersToFile(filePath);
                    System.out.println("successfully saved");
                } catch (Exceptions.FileAlreadyExistException e){
                    System.out.println("Error: File with the same name already exist at this location. cannot export data.");
                } catch (DatatypeConfigurationException e){
                    System.out.println(e.getMessage());
                } catch (JAXBException e) {
                    System.out.println(e.getLinkedException().getMessage());
                } catch (SAXException e) {
                    e.printStackTrace();
                }

            }
        };
    }




    public static Command chooseAndDeleteReservationForCurrentUser() {
        return new Command() {
            int id;
            ReservationView reservation;

            private void chooseReservationToDelete() throws Reservation.NotFoundException {
                System.out.println("Choose reservation:");
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(engine.getFutureUnapprovedReservationsForCurrentUser());
                if (reservations.isEmpty())
                    throw new Exceptions.EmptyReservationListException();

                int i=0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }

                System.out.println("choose reservation to delete");
                int reservationIndex = getNumberFromUser(0, reservations.size() - 1);
                id = reservations.get(reservationIndex).getId();
                reservation = engine.getReservation(id);

                if (reservation == null)
                    throw new Reservation.NotFoundException();
            }
            @Override
            public void execute() {
                try{
                    chooseReservationToDelete();
                    engine.deleteReservation(id);
                } catch (Reservation.NotFoundException e){
                    System.out.println("Reservation not found");
                } catch (Exceptions.EmptyReservationListException e){
                    System.out.println("No Reservations found");
                } catch (Reservation.AlreadyApprovedException e) {
                    System.out.println("Reservation already approved");
                }
            }
        };
    }

    public static Command chooseAndDeleteReservationForManager() {
        return new Command() {
            int id;
            ReservationView reservation;

            private void chooseReservationToDelete() throws Reservation.NotFoundException {
                System.out.println("Choose reservation:");
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(engine.getUnapprovedReservationsForWeek(LocalDate.now()));
                if (reservations.isEmpty())
                    throw new Exceptions.EmptyReservationListException();

                int i=0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }
                System.out.println("choose reservation to delete");
                int reservationIndex = getNumberFromUser(0, reservations.size() - 1);
                id = reservations.get(reservationIndex).getId();
                reservation = engine.getReservation(id);

                if (reservation == null)
                    throw new Reservation.NotFoundException();
            }
            @Override
            public void execute() {
                try{
                    chooseReservationToDelete();
                    engine.deleteReservation(id);
                } catch (Reservation.NotFoundException e){
                    System.out.println("Reservation not found");
                } catch (Exceptions.EmptyReservationListException e){
                    System.out.println("There are no unapproved reservations for this week");
                } catch (Reservation.AlreadyApprovedException e) {
                    System.out.println("Reservation already approved");
                }
            }
        };
    }


    public static Command splitReservationParticipants(int id) {
        return new Command(){
            private ReservationView currentReservation = engine.getReservation(id);

            private List<Integer> restOfParticipants = new ArrayList<Integer>(currentReservation.getParticipants());
            private List<Integer> participants = new ArrayList<Integer>();

            private void askForParticipants(){
                do {
                    System.out.println("Choose rower for new reservation");
                    int i=0;
                    for(Integer memberID : restOfParticipants){
                        MemberView member = engine.getMember(memberID);
                        System.out.println("[" + i + "] " + member.getName() + " (" + member.getAge() + ")");
                        i++;
                    }
                    int index = getNumberFromUser(0, restOfParticipants.size() - 1);

                    MemberView member = engine.getMember(restOfParticipants.get(index));
                    participants.add(member.getSerialNumber());

                    restOfParticipants.remove(index);

                    if (restOfParticipants.size()>1)
                        System.out.println("Do you want to add more?");

                } while (restOfParticipants.size()>1 && getBoolFromUser());
            }

            @Override
            public void execute() {
                if (currentReservation.getParticipants().size() == 1)
                    System.out.println("Cannot split reservation with one participent");
                else {
                    askForParticipants();
                    try {
                        engine.splitReservation(id, participants);
                        System.out.println("the reservation has been split");
                    } catch (Member.AlreadyExistsException | Reservation.IllegalValueException e){
                        System.out.println("Error: " + e.getMessage());
                    } catch (Reservation.AlreadyExistsException e) {
                        System.out.println("Reservation already exists");
                    } catch (Reservation.AlreadyApprovedException e) {
                        System.out.println("Reservation already approved");
                    } catch (Reservation.NotFoundException e) {
                        System.out.println("Reservation not found");
                    }
                }
            }
        };
    }


    public static Command viewAvailableBoats() {
        return new Command() {
            @Override
            public void execute() {
                System.out.println("All the boats that not Disabled:");
                int i=0;
                for(BoatView boat : engine.getAllAvailableBoats()) {
                    System.out.print("[" + i + "] ");
                    printBoatForManager(boat);
                    i++;
                }
            }
        };
    }

    public static Command allocateBoatAndConfirm(int id) {
        return new Command() {
            ReservationView currentReservation = engine.getReservation(id);
            int boatID;

            private void chooseBoat(){
                System.out.println("All available and boats for reservation at this time:");
                List<BoatView> availableBoats = new ArrayList<BoatView>(engine.getUnprivateAvailableBoats(currentReservation.getActivityDate(), currentReservation.getActivity()));

                if(availableBoats.isEmpty())
                    throw new Exceptions.EmptyReservationListException();

                int i=0;
                for(BoatView boat : availableBoats) {
                    System.out.print("[" + i + "] ");
                    printBoatForManager(boat);
                    i++;
                }

                int index = getNumberFromUser(0, availableBoats.size() - 1);
                boatID = availableBoats.get(index).getSerialNumber();
            }

            @Override
            public void execute() {

                try{
                    chooseBoat();
                    engine.approveReservation(id, boatID);
                } catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Error: boat already allocated to another reservation");
                } catch (Reservation.IllegalValueException e){
                    System.out.println(e.getMessage());
                } catch (Exceptions.EmptyReservationListException e){
                    System.out.println("There are no available boats fot this time.");
                }
            }
        };
    }

    public static Command chooseReservationToAllocateBoat() {
        return new Command() {
            int id;
            ReservationView reservation;

            private void chooseReservation() throws Reservation.NotFoundException {
                System.out.println("Choose reservation:");
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(engine.getUnapprovedReservationsForWeek(LocalDate.now()));
                if (reservations.isEmpty())
                    throw new Exceptions.EmptyReservationListException();

                int i=0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }
                System.out.println("choose reservation to Allocate Boat");
                int reservationIndex = getNumberFromUser(0, reservations.size() - 1);
                id = reservations.get(reservationIndex).getId();
                reservation = engine.getReservation(id);

                if (reservation == null)
                    throw new Reservation.NotFoundException();
            }
            @Override
            public void execute() {
                try{
                    chooseReservation();
                    allocateBoatAndConfirm(id).execute();
                } catch (Reservation.NotFoundException e){
                    System.out.println("Reservation not found");
                } catch (Exceptions.EmptyReservationListException e){
                    System.out.println("There are no unapproved reservations for this week");
                }
            }
        };
    }
}


