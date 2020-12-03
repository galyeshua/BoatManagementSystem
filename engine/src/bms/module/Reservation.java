package bms.module;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {
    private int id;
    private Member member;
    private Activity activity; //
    private String activityTime; //
    private List<Boat.Rowers> boatType;
    private List<Member> participants; //
    private LocalDateTime orderDate;
    private Member orderMember;
    private boolean isApproved;



    public Reservation(Member member, Activity activity, String activityTime, List<BoatView.Rowers> boatType,
                       List<Member> participants, LocalDateTime orderDate, Member orderMember) {
        this.member = member;
        this.activity = activity;
        this.activityTime = activityTime;
        this.boatType = boatType;
        this.participants = participants;
        this.orderDate = orderDate;
        this.orderMember = orderMember;
    }

    public int getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Activity getActivity() {
        return activity;
    }

    public String getActivityTime() {
        return activityTime;
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

    public Member getOrderMember() {
        return orderMember;
    }

    public boolean isApproved() {
        return isApproved;
    }
}
