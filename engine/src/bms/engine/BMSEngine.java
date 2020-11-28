package bms.engine;

import java.time.LocalDateTime;
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

//    void updateBoat(int serialNumber,String name, Boat.Rowers numOfRowers, Boat.Paddles numOfPaddles,
//                    boolean isPrivate, boolean isWide, boolean hasCoxswain, boolean isMarine, boolean isDisabled)
//            throws Exceptions.BoatNotFoundException, Exceptions.IllegalBoatValueExeption;

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









    public void addActivity(String name, LocalDateTime startTime, LocalDateTime finishTime, String boatType);
    public void deleteActivity(Integer serialNumber);
    public void updateActivity(Integer serialNumber,String name, LocalDateTime startTime, LocalDateTime finishTime, String boatType);
    public Collection<Activity> getActivities();
    public Activity getActivity(Integer serialNumber);


}
