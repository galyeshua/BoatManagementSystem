package bms.module;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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



    public Reservation(Activity activity, LocalDate activityDate, List<BoatView.Rowers> boatType,
                       List<Integer> participants, LocalDateTime orderDate, int orderedMemberID) {
        this.setId(counter++);
        this.activity = activity;
        this.activityDate = activityDate;
        this.boatType = boatType;
        this.participants = participants;
        this.orderDate = orderDate;
        this.orderedMemberID = orderedMemberID;
        this.isApproved = false;
    }

    public Reservation(ReservationView other){
        this.setId(other.getId());
        this.setActivity(other.getActivity());
        this.setActivityDate(other.getActivityDate());
        this.setBoatType(other.getBoatType());
        this.setParticipants(other.getParticipants());
        this.setOrderDate(other.getOrderDate());
        this.setOrderedMemberID(other.getOrderedMemberID());
        this.setApproved(other.getIsApproved());
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
        return boatType;
    }
    public List<Integer> getParticipants() {
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
    public boolean isMemberInReservation(int memberID){
        return participants.contains(memberID);
    }
    public boolean isParticipantsListEmpty(){
        return participants.isEmpty();
    }
    public boolean isCollide(ReservationView other){
        //System.out.println("comparing " + get);

        //boolean isSameDate = getOrderDate().equals(other.getOrderDate());
        boolean isTimeOverlapping = getActivity().isOverlapping(other.getActivity());
        return isTimeOverlapping;
        //return isSameDate && isTimeOverlapping;
    }

    private void setId(int id) {this.id = id;}
    public void setActivity(Activity activity) {this.activity = activity;}
    public void setActivityDate(LocalDate activityDate) {this.activityDate = activityDate;}
    public void setBoatType(List<Boat.Rowers> boatType) {this.boatType = boatType;}
    public void setParticipants(List<Integer> participants) {this.participants = participants;}
    public void setOrderDate(LocalDateTime orderDate) {this.orderDate = orderDate;}
    public void setOrderedMemberID(int orderedMemberID) {this.orderedMemberID = orderedMemberID;}
    public void setApproved(boolean approved) {isApproved = approved;}

    public void deleteParticipant(Integer memberID){
        participants.remove(memberID);
    }


}
