package bms.engine.list.manager;

import bms.module.Reservation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class ReservationManager {
    private List<Reservation> reservations = new ArrayList<Reservation>();

    public void addReservation(Reservation reservation)
            throws Exceptions.MemberAlreadyInApprovedReservationsException{

        removeParticipantsFromOlderReservations(reservation);

        reservations.add(reservation);
    }


    private void removeParticipantsFromOlderReservations(Reservation reservation)
            throws Exceptions.MemberAlreadyInApprovedReservationsException {
        List<Reservation> overlapReservations = (ArrayList<Reservation>)getOverlapReservations(reservation);

        System.out.println("the list");
        for (Reservation res: overlapReservations)
            System.out.println(res);
        System.out.println("finish");

        List<Integer> participantsForRes = reservation.getParticipants();

        for (int memberID : participantsForRes){
            for (Reservation overlapRes : overlapReservations){

                if ( overlapRes.isMemberInReservation(memberID)){

                    System.out.println("need to delete");
                    System.out.println(overlapRes);
                    System.out.println(memberID);
                    validateIfOldReservationsAlreadyApproved(overlapRes, memberID);
                    overlapRes.deleteParticipant(memberID);
                }

                if (overlapRes.isParticipantsListEmpty())
                    deleteReservation(overlapRes.getId());
            }
        }
    }

    private void validateIfOldReservationsAlreadyApproved(Reservation overlapRes, int memberID)
            throws Exceptions.MemberAlreadyInApprovedReservationsException{
        if (overlapRes.getIsApproved())
            throw new Exceptions.MemberAlreadyInApprovedReservationsException(memberID);
    }


    private Collection<Reservation> getOverlapReservations(Reservation reservation){
        return reservations
                .stream()
                .filter(r -> r.getActivityDate().equals(reservation.getActivityDate()))
                .filter(r -> r.isCollide(reservation))
                .filter(r -> r.getId() != reservation.getId())
                .collect(Collectors.toList());
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

    public void updateReservation(Reservation newReservation)
            throws Exceptions.MemberAlreadyInApprovedReservationsException{
        int serialNumber = newReservation.getId();

        validateApproved(newReservation);

        removeParticipantsFromOlderReservations(newReservation);

        reservations.set(serialNumber, newReservation);
    }


    private void validateApproved(Reservation reservation){
        if(reservation.getIsApproved())
            throw new Exceptions.ReservationAlreadyApprovedException("Regular User cannot change approved reservation");
    }


}
