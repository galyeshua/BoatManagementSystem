package bms.utils.commands;

import bms.exception.General;
import bms.module.*;
import bms.utils.MenuUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import static bms.utils.InputUtils.*;
import static bms.utils.printFormatUtils.*;


public class ReservationCommands {

    public static Command chooseAndEditReservationForCurrentUser() {
        return new Command() {
            int id;
            ReservationView reservation;

            private void chooseReservationToUpdate() throws Reservation.NotFoundException, General.ListIsEmptyException {
                System.out.println("Choose reservation:");
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(MenuUtils.engine.getFutureUnapprovedReservationsForCurrentUser());
                if (reservations.isEmpty())
                    throw new General.ListIsEmptyException();

                int i = 0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }

                System.out.println("choose reservation to edit");
                int reservationIndex = getNumberFromUser(0, reservations.size() - 1);
                id = reservations.get(reservationIndex).getId();
                reservation = MenuUtils.engine.getReservation(id);

                if (reservation == null)
                    throw new Reservation.NotFoundException();
            }
            @Override
            public void execute() {
                try{
                    chooseReservationToUpdate();
                    new MenuUtils.openEditReservationMenu(id, false).execute();
                } catch (Reservation.NotFoundException e){
                    System.out.println("Reservation not found");
                } catch (General.ListIsEmptyException e){
                    System.out.println("No Reservations found");
                }
            }
        };
    }

    public static Command chooseAndEditReservationForManager() {
        return new Command() {
            int id;
            ReservationView reservation;

            private void chooseReservationToUpdate() throws Reservation.NotFoundException, General.ListIsEmptyException {
                System.out.println("Choose reservation:");
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(MenuUtils.engine.getUnapprovedReservationsForWeek(LocalDate.now()));
                if (reservations.isEmpty())
                    throw new General.ListIsEmptyException();

                int i=0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }

                System.out.println("choose reservation to edit");
                int reservationIndex = getNumberFromUser(0, reservations.size() - 1);
                id = reservations.get(reservationIndex).getId();
                reservation = MenuUtils.engine.getReservation(id);

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
                } catch (General.ListIsEmptyException e){
                    System.out.println("No Reservations found");
                }
            }
        };
    }

    public static Command chooseAndUnapproveReservationForManager() {
        return new Command() {
            int id;
            ReservationView reservation;

            private void chooseReservationToUnapprove() throws Reservation.NotFoundException, General.ListIsEmptyException {
                System.out.println("Choose reservation:");
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(MenuUtils.engine.getApprovedReservationsForWeek(LocalDate.now()));
                if (reservations.isEmpty())
                    throw new General.ListIsEmptyException();

                int i=0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }

                System.out.println("choose reservation to edit");
                int reservationIndex = getNumberFromUser(0, reservations.size() - 1);
                id = reservations.get(reservationIndex).getId();
                reservation = MenuUtils.engine.getReservation(id);

                if (reservation == null)
                    throw new Reservation.NotFoundException();
            }
            @Override
            public void execute() {
                try{
                    chooseReservationToUnapprove();
                    if(MenuUtils.engine.getBoat(reservation.getAllocatedBoatID()).getPrivate())
                        throw new Boat.BelongsToMember();
                    MenuUtils.engine.unapprovedReservation(id);
                    System.out.println("successfully unapproved reservation");
                } catch (Reservation.NotFoundException e){
                    System.out.println("Reservation not found");
                } catch (General.ListIsEmptyException e){
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
            private LocalDateTime orderDate;
            private int orderedMemberID;
            private List<ActivityView> activities;
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
                    members = new ArrayList<MemberView>(MenuUtils.engine.getMembers(name));
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
                    participants.add(MenuUtils.user.getSerialNumber());
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
                    ActivityCommands.printActivities().execute();
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
                orderDate = LocalDateTime.now();
                orderedMemberID = MenuUtils.user.getSerialNumber();
                activities = new ArrayList<ActivityView>(MenuUtils.engine.getActivities());

                try{
                    askForValues();
                    Reservation reservation = new Reservation(activity, activityDate, orderDate, orderedMemberID);
                    for (Integer memberID: participants)
                        reservation.addParticipant(memberID);

                    for (BoatView.Rowers boatType: boatType)
                        reservation.addBoatType(boatType);

                    MenuUtils.engine.addReservation(reservation);
                    System.out.println("Reservation created successfully");
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
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(MenuUtils.engine.getReservationsByDate(date));
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
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(MenuUtils.engine.getReservationsForWeek(startDate));
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
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(MenuUtils.engine.getUnapprovedReservationsByDate(date));
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
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(MenuUtils.engine.getUnapprovedReservationsForWeek(startDate));
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
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(MenuUtils.engine.getApprovedReservationsByDate(date));
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
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(MenuUtils.engine.getFutureReservationsForCurrentUser());
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
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(MenuUtils.engine.getReservationsHistoryForCurrentUser());
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

    public static Command editReservationActivity(int id) {
        return new Command() {
            Activity activity;
            Reservation newReservation;
            private List<ActivityView> activities;

            private void askForActivity() throws Activity.IllegalValueException {
                if (activities.isEmpty()){
                    System.out.println("Enter startTime:");
                    LocalTime startTime = getLocalTimeFromUser();
                    System.out.println("Enter finishTime:");
                    LocalTime finishTime = getLocalTimeFromUser();

                    activity = new Activity(startTime, finishTime);
                } else {
                    ActivityCommands.printActivities().execute();

                    System.out.println("choose activity:");
                    int activityIndex = getNumberFromUser(0, activities.size() - 1);

                    activity = new Activity(activities.get(activityIndex));
                }
            }

            @Override
            public void execute() {
                activities = new ArrayList<ActivityView>(MenuUtils.engine.getActivities());

                try {
                    newReservation = new Reservation(MenuUtils.engine.getReservation(id));
                    askForActivity();
                    newReservation.setActivity(activity);
                    MenuUtils.engine.updateReservation(newReservation);
                    System.out.println("Updated successfully");
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
                    newReservation = new Reservation(MenuUtils.engine.getReservation(id));
                    activityDate = getLocalDateFromUser();
                    newReservation.setActivityDate(activityDate);
                    MenuUtils.engine.updateReservation(newReservation);
                    System.out.println("Updated successfully");
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
                ReservationView reservation = MenuUtils.engine.getReservation(id);
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
                    newReservation = new Reservation(MenuUtils.engine.getReservation(id));
                    System.out.println("Enter new type");
                    newReservation.addBoatType((Boat.Rowers) chooseFromOptions(Boat.Rowers.values()));
                    MenuUtils.engine.updateReservation(newReservation);
                    System.out.println("Added successfully");
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
                    newReservation = new Reservation(MenuUtils.engine.getReservation(id));
                    System.out.println("choose type to delete");
                    newReservation.deleteBoatType((Boat.Rowers) chooseFromOptions(newReservation.getBoatType().toArray()));
                    MenuUtils.engine.updateReservation(newReservation);
                    System.out.println("deleed successfully");
                }catch (Reservation.NotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Reservation.IllegalValueException | Member.AccessDeniedException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Reservation.AlreadyApprovedException e){
                    System.out.println("Cannot edit Approved Reservation" );
                } catch (Reservation.ListCannotBeEmptyException e){
                    System.out.println("List must have at least one type" );
                }
            }
        };
    }

    public static Command showReservationPerticipents(int id) {
        return new Command() {
            @Override
            public void execute() {
                ReservationView reservation = MenuUtils.engine.getReservation(id);
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
                    members = new ArrayList<MemberView>(MenuUtils.engine.getMembers(name));
                    if(members.isEmpty())
                        System.out.println("Could not find member");
                } while(members.isEmpty());
                return name;
            }

            @Override
            public void execute() {
                try {
                    newReservation = new Reservation(MenuUtils.engine.getReservation(id));
                    System.out.println("Enter new member");
                    name=getNameFromUser();
                    newReservation.addParticipant(getMemberSerialNumberFromListByName(members, name));
                    MenuUtils.engine.updateReservation(newReservation);
                    System.out.println("Added successfully");
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
                    newReservation = new Reservation(MenuUtils.engine.getReservation(id));
                    System.out.println("choose rower to delete");
                    int i=0;
                    for (int memberID : newReservation.getParticipants()){
                        System.out.print("[" + i + "] ");
                        printParticipantsShort(memberID);
                        i++;
                    }
                    int memberIndex = getNumberFromUser(0, newReservation.getParticipants().size()-1);
                    newReservation.deleteParticipant(newReservation.getParticipants().get(memberIndex));
                    MenuUtils.engine.updateReservation(newReservation);
                    System.out.println("Deleted successfully");
                }catch (Reservation.NotFoundException e){
                    System.out.println("Reservation Not Found" );
                } catch (Reservation.IllegalValueException | Member.AccessDeniedException | Member.AlreadyExistsException e){
                    System.out.println("Error: " + e.getMessage());
                } catch (Reservation.AlreadyApprovedException e){
                    System.out.println("Cannot edit Approved Reservation" );
                } catch (Reservation.ListCannotBeEmptyException e){
                    System.out.println("List must have at least one participant" );
                }
            }
        };
    }

    public static Command chooseAndDeleteReservationForCurrentUser() {
        return new Command() {
            int id;
            ReservationView reservation;

            private void chooseReservationToDelete() throws Reservation.NotFoundException, General.ListIsEmptyException {
                System.out.println("Choose reservation:");
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(MenuUtils.engine.getFutureUnapprovedReservationsForCurrentUser());
                if (reservations.isEmpty())
                    throw new General.ListIsEmptyException();

                int i=0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }

                System.out.println("choose reservation to delete");
                int reservationIndex = getNumberFromUser(0, reservations.size() - 1);
                id = reservations.get(reservationIndex).getId();
                reservation = MenuUtils.engine.getReservation(id);

                if (reservation == null)
                    throw new Reservation.NotFoundException();
            }
            @Override
            public void execute() {
                try{
                    chooseReservationToDelete();
                    MenuUtils.engine.deleteReservation(id);
                    System.out.println("Deleted successfully");
                } catch (Reservation.NotFoundException e){
                    System.out.println("Reservation not found");
                } catch (General.ListIsEmptyException e){
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

            private void chooseReservationToDelete() throws Reservation.NotFoundException, General.ListIsEmptyException {
                System.out.println("Choose reservation:");
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(MenuUtils.engine.getUnapprovedReservationsForWeek(LocalDate.now()));
                if (reservations.isEmpty())
                    throw new General.ListIsEmptyException();

                int i=0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }
                System.out.println("choose reservation to delete");
                int reservationIndex = getNumberFromUser(0, reservations.size() - 1);
                id = reservations.get(reservationIndex).getId();
                reservation = MenuUtils.engine.getReservation(id);

                if (reservation == null)
                    throw new Reservation.NotFoundException();
            }
            @Override
            public void execute() {
                try{
                    chooseReservationToDelete();
                    MenuUtils.engine.deleteReservation(id);
                    System.out.println("Deleted successfully");
                } catch (Reservation.NotFoundException e){
                    System.out.println("Reservation not found");
                } catch (General.ListIsEmptyException e){
                    System.out.println("There are no unapproved reservations for this week");
                } catch (Reservation.AlreadyApprovedException e) {
                    System.out.println("Reservation already approved");
                }
            }
        };
    }

    public static Command splitReservationParticipants(int id) {
        return new Command(){
            private ReservationView currentReservation;
            private List<Integer> restOfParticipants;
            private List<Integer> participants;

            private void askForParticipants(){
                do {
                    System.out.println("Choose rower for new reservation");
                    int i=0;
                    for(Integer memberID : restOfParticipants){
                        MemberView member = MenuUtils.engine.getMember(memberID);
                        System.out.println("[" + i + "] " + member.getName() + " (" + member.getAge() + ")");
                        i++;
                    }
                    int index = getNumberFromUser(0, restOfParticipants.size() - 1);

                    MemberView member = MenuUtils.engine.getMember(restOfParticipants.get(index));
                    participants.add(member.getSerialNumber());

                    restOfParticipants.remove(index);

                    if (restOfParticipants.size()>1)
                        System.out.println("Do you want to add more?");

                } while (restOfParticipants.size()>1 && getBoolFromUser());
            }

            @Override
            public void execute() {
                currentReservation = MenuUtils.engine.getReservation(id);
                restOfParticipants = new ArrayList<Integer>(currentReservation.getParticipants());
                participants = new ArrayList<Integer>();

                if (currentReservation.getParticipants().size() == 1)
                    System.out.println("Cannot split reservation with one participent");
                else {
                    askForParticipants();
                    try {
                        MenuUtils.engine.splitReservation(id, participants);
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

    public static Command allocateBoatAndConfirm(int id) {
        return new Command() {
            ReservationView currentReservation;
            int boatID;

            private void chooseBoat() throws General.ListIsEmptyException {
                boolean adminWantsToAllocateDefaultBoat = false;
                List<BoatView> suitableBoatsForReservation = new ArrayList<BoatView>(MenuUtils.engine.getAllAvailableBoatsForReservation(currentReservation));
                List<BoatView> allAvailableBoatsForTime = new ArrayList<BoatView>(MenuUtils.engine.getUnprivateAvailableBoats(currentReservation.getActivityDate(), currentReservation.getActivity()));

                if(allAvailableBoatsForTime.isEmpty())
                    throw new General.ListIsEmptyException();

                if(suitableBoatsForReservation.isEmpty()){
                    System.out.println("There are not suitable boats for this reservation by the number of rowers or size of boat.");
                } else {
                    System.out.println("Do you want to allocate this boat?");
                    printBoatForManager(suitableBoatsForReservation.get(0));
                    boatID = suitableBoatsForReservation.get(0).getSerialNumber();
                    adminWantsToAllocateDefaultBoat = getBoolFromUser();
                }

                if(!adminWantsToAllocateDefaultBoat){
                    System.out.println("All available and boats for the requested time:");
                    int i=0;
                    for(BoatView boat : allAvailableBoatsForTime) {
                        System.out.print("[" + i + "] ");
                        printBoatForManager(boat);
                        i++;
                    }

                    int index = getNumberFromUser(0, allAvailableBoatsForTime.size() - 1);
                    boatID = allAvailableBoatsForTime.get(index).getSerialNumber();
                }
            }

            @Override
            public void execute() {
                currentReservation = MenuUtils.engine.getReservation(id);

                try{
                    chooseBoat();
                    MenuUtils.engine.approveReservation(id, boatID);
                    System.out.println("boat allocated successfully");
                } catch (Boat.AlreadyAllocatedException e){
                    System.out.println("Error: boat already allocated to another reservation");
                } catch (Reservation.IllegalValueException e){
                    System.out.println(e.getMessage());
                } catch (General.ListIsEmptyException e){
                    System.out.println("There are no available boats fot this time.");
                }
            }
        };
    }

    public static Command chooseReservationToAllocateBoat() {
        return new Command() {
            int id;
            ReservationView reservation;

            private void chooseReservation() throws Reservation.NotFoundException, General.ListIsEmptyException {
                System.out.println("Choose reservation:");
                ArrayList<ReservationView> reservations = new ArrayList<ReservationView>(MenuUtils.engine.getUnapprovedReservationsForWeek(LocalDate.now()));
                if (reservations.isEmpty())
                    throw new General.ListIsEmptyException();

                int i=0;
                for (ReservationView reservation : reservations) {
                    System.out.print("[" + i + "] ");
                    printReservation(reservation);
                    i++;
                }
                System.out.println("choose reservation to Allocate Boat");
                int reservationIndex = getNumberFromUser(0, reservations.size() - 1);
                id = reservations.get(reservationIndex).getId();
                reservation = MenuUtils.engine.getReservation(id);

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
                } catch (General.ListIsEmptyException e){
                    System.out.println("There are no unapproved reservations for this week");
                }
            }
        };
    }
}
