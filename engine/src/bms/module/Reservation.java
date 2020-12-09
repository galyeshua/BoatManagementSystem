package bms.module;

import bms.engine.list.manager.Exceptions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Reservation implements ReservationView {
    private static int counter = 0;

    private int id;
    private Activity activity;
    private LocalDate activityDate;
    private List<Boat.Rowers> boatType;
    private List<Integer> participants;
    private LocalDateTime orderDate;
    private int orderedMemberID;
    private boolean isApproved;



    public Reservation(Activity activity, LocalDate activityDate, LocalDateTime orderDate, int orderedMemberID) {
        this.setId(counter++);
        this.setActivity(activity);
        this.setActivityDate(activityDate);
        this.setOrderDate(orderDate);
        this.setOrderedMemberID(orderedMemberID);
        this.setApproved(false);
        this.participants = new ArrayList<Integer>();
        this.boatType = new ArrayList<Boat.Rowers>();
    }

//    public Reservation(Activity activity, LocalDate activityDate, List<BoatView.Rowers> boatType,
//                       List<Integer> participants, LocalDateTime orderDate, int orderedMemberID) {
//        this.setId(counter++);
//        this.setActivity(activity);
//        this.setActivityDate(activityDate);
//        this.setOrderDate(orderDate);
//        this.setOrderedMemberID(orderedMemberID);
//        this.setApproved(false);
//        this.participants = new ArrayList<Integer>();
//        this.boatType = new ArrayList<Boat.Rowers>();
//    }

    public Reservation(ReservationView other){
        this.setId(other.getId());
        this.setActivity(other.getActivity());
        this.setActivityDate(other.getActivityDate());
        this.setOrderDate(other.getOrderDate());
        this.setOrderedMemberID(other.getOrderedMemberID());
        this.setApproved(other.getIsApproved());
        this.setBoatType(other.getBoatType());
        this.setParticipants(other.getParticipants());
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", activity=" + activity +
                ", activityDate=" + activityDate +
                ", boatType=" + boatType +
                ", participants=" + participants +
                ", orderDate=" + orderDate +
                ", orderedMemberID=" + orderedMemberID +
                ", isApproved=" + isApproved +
                '}';
    }

    public int getId() {
        return id;
    }
    public Activity getActivity() {
        return activity;
    }
    public LocalDate getActivityDate() {
        return activityDate;
    }
    public List<Boat.Rowers> getBoatType() {
        return Collections.unmodifiableList(boatType);
    }
    public List<Integer> getParticipants() {
        return Collections.unmodifiableList(participants);
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public int getOrderedMemberID() {
        return orderedMemberID;
    }
    public boolean getIsApproved() {
        return isApproved;
    }
    public boolean isMemberInReservation(int memberID){
        return participants.contains(memberID);
    }
    public boolean isParticipantsListEmpty(){
        return participants.isEmpty();
    }
    public boolean isCollide(ReservationView other){
        boolean isTimeOverlapping = getActivity().isOverlapping(other.getActivity());
        return isTimeOverlapping;
    }

    private void setId(int id) {this.id = id;}
    public void setActivity(Activity activity) {this.activity = activity;}
    public void setActivityDate(LocalDate activityDate) {this.activityDate = activityDate;}
    public void setBoatType(List<Boat.Rowers> boatType) {this.boatType = new ArrayList<Boat.Rowers>(boatType);}
    public void setParticipants(List<Integer> participants) {this.participants = new ArrayList<Integer>(participants);}
    public void setOrderDate(LocalDateTime orderDate) {this.orderDate = orderDate;}
    public void setOrderedMemberID(int orderedMemberID) {this.orderedMemberID = orderedMemberID;}
    public void setApproved(boolean approved) {isApproved = approved;}

    public void addParticipant(Integer memberID){
        if (participants.contains(memberID))
            throw new Exceptions.MemberAlreadyExistsException("Member already exist in list");
        participants.add(memberID);
    }
    public void deleteParticipant(Integer memberID){
        if (participants.size() == 1)
            throw new Exceptions.ListCannotBeEmptyException();
        participants.remove(memberID);
    }

    public void addBoatType(Boat.Rowers type){
        if (boatType.contains(type))
            throw new Exceptions.IllegalBoatValueException("Boat type already exist in list");
        boatType.add(type);
    }
    public void deleteBoatType(Boat.Rowers type){
        if (boatType.size() == 1)
            throw new Exceptions.ListCannotBeEmptyException();
        boatType.remove(type);
    }

}
