package bms.engine;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import bms.engine.list.manager.*;

import bms.module.*;


public class Engine implements BMSEngine{
    Member currentUser = null;
    BoatManager boats = new BoatManager();
    MemberManager members = new MemberManager();
    ActivityManager activities = new ActivityManager();
    ReservationManager reservations = new ReservationManager();

    @Override
    public MemberView getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(MemberView currentUser) {
        this.currentUser = new Member(currentUser);
    }

    @Override
    public void addBoat(int serialNumber, String name, Boat.Rowers numOfRowers, Boat.Paddles numOfPaddles, boolean isPrivate,
                        boolean isWide, boolean hasCoxswain, boolean isMarine, boolean isDisabled)
            throws Exceptions.BoatAlreadyExistsException, Exceptions.IllegalBoatValueException {

        Boat boat = new Boat(serialNumber, name,numOfRowers,numOfPaddles,isPrivate,isWide,hasCoxswain,isMarine,isDisabled);
        boats.addBoat(boat);
    }

    @Override
    public void deleteBoat(int serialNumber) throws Exceptions.BoatNotFoundException {
        boats.deleteBoat(serialNumber);
    }

    @Override
    public Collection<BoatView> getBoats() {
        //return boats.getBoats();
        return Collections.unmodifiableCollection(boats.getBoats());
    }

    @Override
    public BoatView getBoat(int serialNumber)  {
        return boats.getBoat(serialNumber);
    }

    @Override
    public BoatView getBoat(String name)  {
        return boats.getBoat(name);
    }

    public void updateBoat(Boat newBoat) throws Exceptions.BoatNotFoundException {
        boats.updateBoat(newBoat);
    }





    @Override
    public void addMember(int serialNumber, String name, int age, String notes, Member.Level level,
                          LocalDate joinDate, LocalDate expireDate, boolean hasPrivateBoat,
                          int boatSerialNumber, String phoneNumber, String email, String password,
                          boolean isManager) {

        Member member = new Member(serialNumber,name,age,notes,level,joinDate,expireDate,hasPrivateBoat,
                boatSerialNumber,phoneNumber,email,password,isManager);

        members.addMember(member);
    }

    @Override
    public void deleteMember(int serialNumber) throws Exceptions.MemberNotFoundException {
        members.deleteMember(serialNumber);
    }

    @Override
    public Collection<MemberView> getMembers() {
        return Collections.unmodifiableCollection(members.getMembers());
    }

    @Override
    public Collection<MemberView> getMembers(String name) {
        return Collections.unmodifiableCollection(
                members.getMembers()
                        .stream()
                        .filter(m -> m.getName().equals(name))
                        .collect(Collectors.toList())
        );
    }


    @Override
    public MemberView getMember(int serialNumber) {
        return members.getMember(serialNumber);
    }

    @Override
    public MemberView getMember(String email) {
        return members.getMember(email);
    }

    @Override
    public void updateMember(Member newMember) throws Exceptions.MemberNotFoundException{
        members.updateMember(newMember);
    }






    @Override
    public void addActivity(String name, LocalTime startTime, LocalTime finishTime, String boatType)
            throws Exceptions.ActivityAlreadyExistsException {
        Activity activity = new Activity(name,startTime,finishTime,boatType);
        activities.addActivity(activity);
    }

    @Override
    public void addActivity(String name, LocalTime startTime, LocalTime finishTime)
            throws Exceptions.ActivityAlreadyExistsException {
        addActivity(name,startTime,finishTime, "");
    }

    @Override
    public void deleteActivity(int id)
            throws Exceptions.ActivityAlreadyExistsException {

        activities.deleteActivity(id);
    }

    @Override
    public Collection<ActivityView> getActivities() {
        return Collections.unmodifiableCollection(activities.getActivities());
    }

    @Override
    public ActivityView getActivity(int id) {
        return activities.getActivity(id);
    }


    public void updateActivity(Activity newActivity)
            throws Exceptions.ActivityNotFoundException  {
        activities.updateActivity(newActivity);

    }


    @Override
    public void addReservation(Reservation newReservation) throws Exceptions.IllegalReservationValueException{
        try{
            reservations.addReservation(newReservation);
        } catch (Exceptions.MemberAlreadyInApprovedReservationsException e){
            MemberView member = getMember(e.getMemberID());
            throw new Exceptions.IllegalReservationValueException("Member '" + member.getName() + "' already have an approved reservation for this time");
        }
    }


    @Override
    public void deleteReservation(int id)
    throws Exceptions.ReservationAlreadyExistsException{
        reservations.deleteReservation(id);
    }


    @Override
    public Collection<ReservationView> getReservations() {
        return Collections.unmodifiableCollection(reservations.getReservations());
    }


    private Stream<Reservation> getReservationsForCurrentUser(){
        int userID = currentUser.getSerialNumber();
        return reservations.getReservations()
                .stream()
                .filter(r -> r.getParticipants().contains(userID) || r.getOrderedMemberID()==userID);
    }


    @Override
    public Collection<ReservationView> getFutureUnapprovedReservationsForCurrentUser() {
        return Collections.unmodifiableCollection(
                getReservationsForCurrentUser()
                        .filter(r -> r.getActivityDate().isAfter(LocalDate.now() ))
                        .filter(r -> !r.getIsApproved())
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getFutureReservationsForCurrentUser() {
        return Collections.unmodifiableCollection(
                getReservationsForCurrentUser()
                        .filter(r -> r.getActivityDate().isAfter(LocalDate.now() ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getReservationsHistoryForCurrentUser() {
        return Collections.unmodifiableCollection(
                getReservationsForCurrentUser()
                        .filter(r -> r.getActivityDate().isBefore(LocalDate.now() ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getReservationsByDate(LocalDate date) {
        return Collections.unmodifiableCollection(
                reservations.getReservations()
                        .stream()
                        .filter(r -> r.getActivityDate().equals(date))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getReservationsForWeek(LocalDate startDate) {
        LocalDate finishDate = startDate.plusDays(7);
        return Collections.unmodifiableCollection(
                reservations.getReservations()
                        .stream()
                        .filter(r -> r.getActivityDate().isEqual(startDate) || r.getActivityDate().isAfter(startDate))
                        .filter(r -> r.getActivityDate().isBefore(finishDate))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getApprovedReservationsByDate(LocalDate date) {
        return Collections.unmodifiableCollection(
                getReservationsByDate(date)
                        .stream()
                        .filter(ReservationView::getIsApproved)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getApprovedReservationsForWeek(LocalDate startDate) {
        LocalDate finishDate = startDate.plusDays(7);
        return Collections.unmodifiableCollection(
                getReservationsForWeek(startDate)
                        .stream()
                        .filter(ReservationView::getIsApproved)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getUnapprovedReservationsByDate(LocalDate date) {
        return Collections.unmodifiableCollection(
                getReservationsByDate(date)
                        .stream()
                        .filter(r -> !r.getIsApproved())
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<ReservationView> getUnapprovedReservationsForWeek(LocalDate startDate) {
        LocalDate finishDate = startDate.plusDays(7);
        return Collections.unmodifiableCollection(
                getReservationsForWeek(startDate)
                        .stream()
                        .filter(r -> !r.getIsApproved())
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void unapproveReservation(int id){
        Reservation reservation = reservations.getReservation(id);
        validateUserRole();

        if(reservation==null)
            throw new Exceptions.ReservationNotFoundException();

        if (reservation.getIsApproved())
            reservation.setApproved(false);
    }

    @Override
    public ReservationView getReservation(int id) { return reservations.getReservation(id);    }

    @Override
    public void updateReservation(Reservation newReservation) throws Exceptions.ReservationNotFoundException{
        Reservation safeReservation = newReservation;

        int reservationID = newReservation.getId();
        Reservation oldReservation = reservations.getReservation(reservationID);

        validateUserRole();
        if (!oldReservation.isMemberInReservation(currentUser.getSerialNumber()))
            throw new Exceptions.MemberAccessDeniedException();

        if (!currentUser.getManager()){
            safeReservation = new Reservation(oldReservation);

            safeReservation.setActivity(newReservation.getActivity());
            safeReservation.setActivityDate(newReservation.getActivityDate());
            safeReservation.setBoatType(newReservation.getBoatType());
            safeReservation.setParticipants(newReservation.getParticipants());
        }

        // check if objects are equals and throw access denied if not

        try{
            reservations.updateReservation(safeReservation);
        } catch (Exceptions.MemberAlreadyInApprovedReservationsException e){
            MemberView member = getMember(e.getMemberID());
            throw new Exceptions.IllegalReservationValueException("Member '" + member.getName() + "' already have an approved reservation for this time");
        }
    }

    private void validateUserRole(){
        if (!currentUser.getManager())
            throw new Exceptions.MemberAccessDeniedException();
    }

}
