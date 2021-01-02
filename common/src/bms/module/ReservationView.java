package bms.module;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationView {

    Integer getAllocatedBoatID();

    int getId();

    Activity getActivity();

    LocalDate getActivityDate();

    List<Boat.Rowers> getBoatType();

    List<Integer> getParticipants();

    LocalDateTime getOrderDate();

    int getOrderedMemberID();

    boolean getIsApproved();

    boolean isMemberInReservation(int memberID);

    boolean isParticipantsListEmpty();

    boolean isCollide(ReservationView other);

}
