package bms.module;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Reservation implements ReservationView{
    private int id;
    private int memberID;
    private Activity activity; //
    private LocalDate activityDate;
    private List<Boat.Rowers> boatType;
    private List<Member> participants; //
    private LocalDateTime orderDate;
    private int orderedMemberID;
    private boolean isApproved;



    public Reservation(int memberID, Activity activity, LocalDate activityDate, List<BoatView.Rowers> boatType,
                       List<Member> participants, LocalDateTime orderDate, int orderedMemberID) {
        this.memberID = memberID;
        this.activity = activity;
        this.activityDate = activityDate;
        this.boatType = boatType;
        this.participants = participants;
        this.orderDate = orderDate;
        this.orderedMemberID = orderedMemberID;
    }

    public Reservation(ReservationView other){
        this.setId(other.getId());
        this.setMemberID(other.getMemberID());
        this.setActivity(other.getActivity());
        this.setActivityDate(other.getActivityDate());
        this.setBoatType(other.getBoatType());
        this.setParticipants(other.getParticipants());
        this.setOrderDate(other.getOrderDate());
        this.setOrderedMemberID(other.getOrderedMemberID());

    }

    public int getId() {
        return id;
    }
    public int getMemberID() {
        return memberID;
    }
    public Activity getActivity() {
        return activity;
    }
    public LocalDate getActivityDate() {
        return activityDate;
    }
    public List<Boat.Rowers> getBoatType() {
        return boatType;
    }
    public List<Member> getParticipants() {
        return participants;
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


    public void setId(int id) {this.id = id;}
    public void setMemberID(int memberID) {this.memberID = memberID;}
    public void setActivity(Activity activity) {this.activity = activity;}
    public void setActivityDate(LocalDate activityDate) {this.activityDate = activityDate;}
    public void setBoatType(List<Boat.Rowers> boatType) {this.boatType = boatType;}
    public void setParticipants(List<Member> participants) {this.participants = participants;}
    public void setOrderDate(LocalDateTime orderDate) {this.orderDate = orderDate;}
    public void setOrderedMemberID(int orderedMemberID) {this.orderedMemberID = orderedMemberID;}
    public void setApproved(boolean approved) {isApproved = approved;}

}
