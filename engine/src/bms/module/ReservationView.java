package bms.module;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationView {
    int getId();

    int getMemberID();

    Activity getActivity();

    LocalDate getActivityDate();

    List<Boat.Rowers> getBoatType();

    List<Member> getParticipants();

    LocalDateTime getOrderDate();

    int getOrderedMemberID();

    boolean getIsApproved();
}
