package bms.engine.list.manager;

import bms.engine.Exceptions;
import bms.module.Member;
import bms.module.Reservation;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class ReservationManager
{
    @XmlElement(name="reservation", required = true)
    private List<Reservation> reservations = new ArrayList<Reservation>();

    public void addReservation(Reservation reservation) throws Member.AlreadyHaveApprovedReservationsException,
            Reservation.AlreadyExistsException, Reservation.NotFoundException, Reservation.AlreadyApprovedException, Member.AlreadyExistsException {

        if (getReservation(reservation.getId()) != null)
            throw new Reservation.AlreadyExistsException("Reservation " + reservation.getId() + " already exist");

        removeParticipantsFromOlderReservations(reservation);

        reservations.add(new Reservation(reservation));
    }

    private void removeParticipantsFromOlderReservations(Reservation reservation)
            throws Member.AlreadyHaveApprovedReservationsException, Reservation.NotFoundException, Reservation.AlreadyApprovedException {

        List<Reservation> overlapReservations = (ArrayList<Reservation>)getOverlapReservations(reservation)
                .stream().filter(r -> r.getId() != reservation.getId()).collect(Collectors.toList());

        List<Integer> participantsForRes = reservation.getParticipants();

        for (int memberID : participantsForRes){
            for (Reservation overlapRes : overlapReservations){

                if ( overlapRes.isMemberInReservation(memberID)){
                    validateIfOldReservationsAlreadyApproved(overlapRes, memberID);

                    try{
                        overlapRes.deleteParticipant(memberID);
                    } catch ( Exceptions.ListCannotBeEmptyException e){
                        deleteReservation(overlapRes.getId());
                    }
                }
            }
        }
    }

    private void validateIfOldReservationsAlreadyApproved(Reservation overlapRes, int memberID)
            throws Member.AlreadyHaveApprovedReservationsException{
        if (overlapRes.getIsApproved())
            throw new Member.AlreadyHaveApprovedReservationsException(memberID);
    }

    private Collection<Reservation> getOverlapReservations(Reservation reservation){
        return reservations
                .stream()
                .filter(r -> r.getActivityDate().equals(reservation.getActivityDate()))
                .filter(r -> r.isCollide(reservation))
                .collect(Collectors.toList());
    }

    public void deleteReservation(int id) throws Reservation.NotFoundException, Reservation.AlreadyApprovedException {
        Reservation reservation = getReservation(id);
        if(reservation == null)
            throw new Reservation.NotFoundException();
        validateApproved(reservation);
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
            throws Member.AlreadyHaveApprovedReservationsException,
            Reservation.AlreadyApprovedException, Reservation.NotFoundException, Member.AlreadyExistsException {

        int id = newReservation.getId();

        validateApproved(newReservation);

        removeParticipantsFromOlderReservations(newReservation);

        reservations.set(reservations.indexOf(getReservation(id)), new Reservation(newReservation));
    }

    private void validateApproved(Reservation reservation) throws Reservation.AlreadyApprovedException {
        if(reservation.getIsApproved())
            throw new Reservation.AlreadyApprovedException("Cannot change approved reservation");
    }

    public void eraseAll(){
        reservations.clear();
    }

}
