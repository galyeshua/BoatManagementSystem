package bms.engine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import bms.engine.list.manager.Exceptions;
import bms.module.Activity;
import bms.module.Boat;
import bms.module.BoatView;
import bms.module.Member;


public interface BMSEngine {

    void addBoat(int serialNumber, String name, Boat.Rowers numOfRowers, Boat.Paddles numOfPaddles, boolean isPrivate,
                 boolean isWide, boolean hasCoxswain, boolean isMarine, boolean isDisabled)
            throws Exceptions.BoatAlreadyExistsException, Exceptions.IllegalBoatValueException;
    void deleteBoat(int serialNumber) throws Exceptions.BoatNotFoundException;
    Collection<BoatView> getBoats();
    BoatView getBoat(int serialNumber);
    BoatView getBoat(String name);

    void updateBoat(Boat newBoat) throws Exceptions.BoatNotFoundException;
//
//    void updateBoatName(int serialNumber, String name) throws Exceptions.BoatNotFoundException;
//    void updateBoatNumOfRowers(int serialNumber, Boat.Rowers numOfRowers) throws Exceptions.BoatNotFoundException;
//    void updateBoatNumOfPaddles(int serialNumber, Boat.Paddles numOfPaddles) throws Exceptions.BoatNotFoundException;
//    void updateBoatPrivate(int serialNumber, boolean isPrivate) throws Exceptions.BoatNotFoundException;
//    void updateBoatWide(int serialNumber, boolean isWide) throws Exceptions.BoatNotFoundException;
//    void updateBoatCoxswain(int serialNumber, boolean hasCoxswain)
//            throws Exceptions.BoatNotFoundException,Exceptions.IllegalBoatValueException;
//    void updateBoatMarine(int serialNumber, boolean isMarine) throws Exceptions.BoatNotFoundException;
//    void updateBoatDisabled(int serialNumber, boolean isDisabled) throws Exceptions.BoatNotFoundException;




    void addMember(int serialNumber, String name, int age, String notes, Member.Level level,
                   LocalDate joinDate, LocalDate expireDate, boolean hasPrivateBoat,
                   int boatSerialNumber, String phoneNumber, String email, String password,
                   boolean isManager) throws Exceptions.MemberAlreadyExistsException;
    void deleteMember(int serialNumber) throws Exceptions.MemberNotFoundException;
    Collection<Member> getMembers();
    Member getMember(int serialNumber);
    Member getMember(String email);

    void updateMemberName(int serialNumber, String name) throws Exceptions.MemberNotFoundException;
    void updateMemberAge(int serialNumber, int age) throws Exceptions.MemberNotFoundException;
    void updateMemberNotes(int serialNumber, String notes) throws Exceptions.MemberNotFoundException;
    void updateMemberLevel(int serialNumber, Member.Level level) throws Exceptions.MemberNotFoundException;
    void updateMemberPrivateBoat(int serialNumber, boolean hasBoat) throws Exceptions.MemberNotFoundException;
    void updateBoatSerialNumber(int serialNumber,int boatSerialNumber);
    void updateMemberPhone(int serialNumber, String phone) throws Exceptions.MemberNotFoundException;
    void updateMemberEmail(int serialNumber, String email) throws Exceptions.MemberNotFoundException;
    void updateMemberPassword(int serialNumber, String password) throws Exceptions.MemberNotFoundException;
    void updateMemberRole(int serialNumber, boolean isManager) throws Exceptions.MemberNotFoundException;




    void addActivity(String name, LocalTime startTime, LocalTime finishTime, String boatType)
            throws Exceptions.ActivityAlreadyExistsException;
    void addActivity(String name, LocalTime startTime, LocalTime finishTime)
            throws Exceptions.ActivityAlreadyExistsException;
    void deleteActivity(int id)
            throws Exceptions.ActivityNotFoundException;
    Collection<Activity> getActivities();
    Activity getActivity(int id);

    void updateActivityName(int id, String name) throws Exceptions.ActivityNotFoundException;
    void updateActivityStartTime(int id, LocalTime StartTime)throws Exceptions.ActivityNotFoundException;
    void updateActivityFinishTime(int id, LocalTime finishTime)throws Exceptions.ActivityNotFoundException;
    void updateActivityBoatType(int id, String boatType) throws Exceptions.ActivityNotFoundException;

}
