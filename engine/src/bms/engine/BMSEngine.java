package bms.engine;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import bms.engine.list.manager.Exceptions;
import bms.module.Activity;
import bms.module.Boat;
import bms.module.Member;


public interface BMSEngine {

    void addBoat(String name, Boat.Rowers numOfRowers, Boat.Paddles numOfPaddles, boolean isPrivate,
                 boolean isWide, boolean hasCoxswain, boolean isMarine, boolean isDisabled)
            throws Exceptions.BoatAlreadyExistsException, Exceptions.IllegalBoatValueException;
    void deleteBoat(int serialNumber) throws Exceptions.BoatNotFoundException;
    Collection<Boat> getBoats();
    Boat getBoat(int serialNumber);
    Boat getBoat(String name);

    void updateBoatName(int serialNumber, String name) throws Exceptions.BoatNotFoundException;
    void updateBoatNumOfRowers(int serialNumber, Boat.Rowers numOfRowers) throws Exceptions.BoatNotFoundException;
    void updateBoatNumOfPaddles(int serialNumber, Boat.Paddles numOfPaddles) throws Exceptions.BoatNotFoundException;
    void updateBoatPrivate(int serialNumber, boolean isPrivate) throws Exceptions.BoatNotFoundException;
    void updateBoatWide(int serialNumber, boolean isWide) throws Exceptions.BoatNotFoundException;
    void updateBoatCoxswain(int serialNumber, boolean hasCoxswain)
            throws Exceptions.BoatNotFoundException,Exceptions.IllegalBoatValueException;
    void updateBoatMarine(int serialNumber, boolean isMarine) throws Exceptions.BoatNotFoundException;
    void updateBoatDisabled(int serialNumber, boolean isDisabled) throws Exceptions.BoatNotFoundException;




    void addMember(int serialNumber, String name, int age, String notes, Member.Level level,
                          LocalDateTime joinDate, LocalDateTime expireDate, boolean hasPrivateBoat,
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
    void deleteActivity(String name, LocalTime startTime, LocalTime finishTime)
            throws Exceptions.ActivityNotFoundException;
    Collection<Activity> getActivities();
    Activity getActivity(String name, LocalTime startTime, LocalTime finishTime);

    void updateActivityName(String name, LocalTime startTime, LocalTime finishTime, String name1) throws Exceptions.ActivityNotFoundException;
    void updateActivityStartTime(String name, LocalTime startTime, LocalTime finishTime, LocalTime StartTime1)throws Exceptions.ActivityNotFoundException;
    void updateActivityFinishTime(String name, LocalTime startTime, LocalTime finishTime, LocalTime finishTime1)throws Exceptions.ActivityNotFoundException;
    void updateActivityBoatType(String name, LocalTime startTime, LocalTime finishTime, String boatType) throws Exceptions.ActivityNotFoundException;


    //void updateActivity(Integer serialNumber,String name, LocalDateTime startTime, LocalDateTime finishTime, String boatType);

}
