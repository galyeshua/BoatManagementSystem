package bms.engine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import bms.engine.list.manager.Exceptions;
import bms.module.*;


public interface BMSEngine {

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


//
//    void addReservation();
//    void deleteReservation(int id);
//    Collection<Reservation> getReservations();
//    Reservation getReservation(int id);
//


}
