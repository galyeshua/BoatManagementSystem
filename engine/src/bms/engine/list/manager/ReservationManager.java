package bms.engine.list.manager;

import bms.module.Activity;
import bms.module.Member;
import bms.module.Reservation;
import bms.module.ReservationView;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReservationManager {
    private List<Reservation> reservations = new ArrayList<Reservation>();

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void deleteReservation(int id) {
        Reservation reservation = getReservation(id);
        reservations.remove(reservation);
    }

    public Collection<Reservation> getReservations() {
        return reservations;
    }

    public Reservation getReservation(int id) {
        for (Reservation reservation : reservations) {
            if(reservation.getId() == id)
                return reservation;
        }
        return null;
    }

    public void updateReservation(Reservation newReservation) {
        int serialNumber = newReservation.getId();
        Reservation currentReservation = getReservation(serialNumber);

        if (currentReservation == null)
            throw new Exceptions.ReservationNotFoundException();

        validateReservationNotApproved(currentReservation.getIsApproved());

        reservations.set(serialNumber, newReservation);
    }

    public void validateReservationNotApproved(boolean isApproved){
        if (isApproved)
            throw new Exceptions.ReservationAlreadyApprovedException("Your Reservation Already Approved");

    }


}
