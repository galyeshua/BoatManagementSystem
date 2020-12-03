package bms.engine.list.manager;

import bms.module.Activity;
import bms.module.Reservation;

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
        Reservation reservation = getAReservation(id);
        reservations.remove(reservation);
    }

    public Collection<Reservation> getReservations() {
        return reservations;
    }

    public Reservation getAReservation(int id) {
        for (Reservation reservation : reservations) {
            if(reservation.getId() == id)
                return reservation;
        }
        return null;
    }



}
