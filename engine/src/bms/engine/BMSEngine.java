package bms.engine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import bms.engine.list.manager.Exceptions;
import bms.module.*;


public interface BMSEngine {

    MemberView getCurrentUser();
    void setCurrentUser(MemberView currentUser);

    void addBoat(int serialNumber, String name, Boat.Rowers numOfRowers, Boat.Paddles numOfPaddles, boolean isPrivate,
                 boolean isWide, boolean hasCoxswain, boolean isMarine, boolean isDisabled)
            throws Exceptions.BoatAlreadyExistsException, Exceptions.IllegalBoatValueException;
    void deleteBoat(int serialNumber) throws Exceptions.BoatNotFoundException;
    Collection<BoatView> getBoats();
    BoatView getBoat(int serialNumber);
    BoatView getBoat(String name);

    void updateBoat(Boat newBoat) throws Exceptions.BoatNotFoundException;




    void addMember(int serialNumber, String name, int age, String notes, Member.Level level,
                   LocalDate joinDate, LocalDate expireDate, boolean hasPrivateBoat,
                   int boatSerialNumber, String phoneNumber, String email, String password,
                   boolean isManager) throws Exceptions.MemberAlreadyExistsException;
    void deleteMember(int serialNumber) throws Exceptions.MemberNotFoundException;
    Collection<MemberView> getMembers();
    Collection<MemberView> getMembers(String name);
    MemberView getMember(int serialNumber);
    MemberView getMember(String email);
    void updateMember(Member newMember) throws Exceptions.MemberNotFoundException;





    void addActivity(String name, LocalTime startTime, LocalTime finishTime, String boatType)
            throws Exceptions.ActivityAlreadyExistsException;
    void addActivity(String name, LocalTime startTime, LocalTime finishTime)
            throws Exceptions.ActivityAlreadyExistsException;
    void deleteActivity(int id)
            throws Exceptions.ActivityNotFoundException;
    Collection<ActivityView> getActivities();
    ActivityView getActivity(int id);
    void updateActivity(Activity newActivity) throws Exceptions.ActivityNotFoundException;



    void addReservation(Activity activity, LocalDate activityDate,
                        List<Boat.Rowers> boatType, List<Integer> participants,
                        LocalDateTime orderDate, int orderMemberID);
    void deleteReservation(int id);
    Collection<ReservationView> getReservations();
    Collection<ReservationView> getFutureUnapprovedReservationsForCurrentUser();
    Collection<ReservationView> getFutureReservationsForCurrentUser();
    Collection<ReservationView> getReservationsHistoryForCurrentUser();
    Collection<ReservationView> getReservationsByDate(LocalDate date);
    Collection<ReservationView> getReservationsForWeek(LocalDate startDate);
    Collection<ReservationView> getApprovedReservationsByDate(LocalDate date);
    Collection<ReservationView> getUnapprovedReservationsByDate(LocalDate date);
    Collection<ReservationView> getUnapprovedReservationsForWeek(LocalDate startDate);
    void unapproveReservation(int id);
    ReservationView getReservation(int id);
    public void updateReservation(Reservation newReservation) throws Exceptions.ReservationNotFoundException;



    }
