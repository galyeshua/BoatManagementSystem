package bms.module;

import bms.exception.General;
import bms.module.adapter.LocalDateAdapter;
import bms.module.adapter.LocalDateTimeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "reservation")
public class Reservation implements ReservationView, Serializable {
    private static int counter = 0;

    private int id;

    @XmlElement
    private Activity activity;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    @XmlAttribute(required = true)
    private LocalDate activityDate;

    @XmlAttribute(required = true)
    private List<Boat.Rowers> boatType;

    @XmlAttribute(required = true)
    private List<Integer> participants;

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    @XmlAttribute(required = true)
    private LocalDateTime orderDate;

    @XmlAttribute(required = true)
    private int orderedMemberID;

    @XmlAttribute(required = true)
    private boolean isApproved;

    @XmlAttribute(required = true)
    private Integer allocatedBoatID;

    private Reservation(){ }

    public Reservation(Activity activity, LocalDate activityDate, LocalDateTime orderDate, int orderedMemberID) {
        this.setId(counter++);
        this.setActivity(activity);
        this.setActivityDate(activityDate);
        this.setOrderDate(orderDate);
        this.setOrderedMemberID(orderedMemberID);
        this.setApproved(false);
        this.participants = new ArrayList<Integer>();
        this.boatType = new ArrayList<Boat.Rowers>();
        this.setAllocatedBoatID(null);
    }

    public Reservation(ReservationView other) throws Member.AlreadyExistsException {
        this.setId(other.getId());
        this.setActivity(other.getActivity());
        this.setActivityDate(other.getActivityDate());
        this.setOrderDate(other.getOrderDate());
        this.setOrderedMemberID(other.getOrderedMemberID());
        this.setApproved(other.getIsApproved());
        this.setBoatType(other.getBoatType());
        this.setParticipants(other.getParticipants());
        this.setAllocatedBoatID(other.getAllocatedBoatID());
    }

    public Integer getAllocatedBoatID() {
        return allocatedBoatID;
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

    public void setAllocatedBoatID(Integer boatID){
        this.allocatedBoatID = boatID;
        this.setApproved(boatID != null);
    }

    public void allocateNewId() { this.setId(counter++); }
    private void setId(int id) {this.id = id;}
    public void setActivity(Activity activity) {this.activity = activity;}
    public void setActivityDate(LocalDate activityDate) {this.activityDate = activityDate;}
    public void setBoatType(List<Boat.Rowers> boatType) {this.boatType = new ArrayList<Boat.Rowers>(boatType);}
    public void setParticipants(List<Integer> participants) throws Member.AlreadyExistsException {
        this.participants = new ArrayList<Integer>();
        for (Integer memberID : participants)
            addParticipant(memberID);
    }

    public void setOrderDate(LocalDateTime orderDate) {this.orderDate = orderDate;}
    public void setOrderedMemberID(int orderedMemberID) {this.orderedMemberID = orderedMemberID;}
    private void setApproved(boolean approved) {isApproved = approved;}

    public void addParticipant(Integer memberID) throws Member.AlreadyExistsException {
        if (participants.contains(memberID))
            throw new Member.AlreadyExistsException("Member already exist in list");
        participants.add(memberID);
    }
    public void deleteParticipant(Integer memberID) throws General.ListCannotBeEmptyException {
        if (participants.size() == 1)
            throw new General.ListCannotBeEmptyException();
        participants.remove(memberID);
    }

    public void addBoatType(Boat.Rowers type) throws Boat.IllegalValueException {
        if (boatType.contains(type))
            throw new Boat.IllegalValueException("Boat type already exist in list");
        boatType.add(type);
    }

    public void deleteBoatType(Boat.Rowers type) throws General.ListCannotBeEmptyException {
        if (boatType.size() == 1)
            throw new General.ListCannotBeEmptyException();
        boatType.remove(type);
    }

    public static class NotFoundException extends Exception { }

    public static class AlreadyExistsException extends Exception {
        public AlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class IllegalValueException extends Exception{
        public IllegalValueException(String message) {
            super(message);
        }
    }

    public static class AlreadyApprovedException extends Exception {
        public AlreadyApprovedException(String message) {
            super(message);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id &&
                orderedMemberID == that.orderedMemberID &&
                isApproved == that.isApproved &&
                Objects.equals(activity, that.activity) &&
                Objects.equals(activityDate, that.activityDate) &&
                Objects.equals(boatType, that.boatType) &&
                Objects.equals(participants, that.participants) &&
                Objects.equals(orderDate, that.orderDate) &&
                Objects.equals(allocatedBoatID, that.allocatedBoatID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, activity, activityDate, boatType, participants, orderDate, orderedMemberID, isApproved, allocatedBoatID);
    }
}
