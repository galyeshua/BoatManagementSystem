package bms.engine;

import java.time.LocalDateTime;
import java.util.Collection;

import bms.module.Activity;
import bms.module.Boat;
import bms.module.Member;

public interface BMSEngine {

    public void addBoat(String name, String type, Boolean isPrivate, Boolean isWide,
                        Boolean hasCoxswain, Boolean isMarine);
    public void deleteBoat(Integer serialNumber);
    public void updateBoat(Integer serialNumber,String name, String type, Boolean isPrivate, Boolean isWide,
                           Boolean hasCoxswain, Boolean isMarine, Boolean isDisabled);
    public Collection<Boat> getBoats();
    public Boat getBoat(Integer serialNumber);



    public void addMember(String name, Integer age, String notes, Member.Level level,
                          LocalDateTime joinDate, LocalDateTime expireDate, Boolean hasPrivateBoat,
                          Integer boatSerialNumber, String phoneNumber, String email, String password,
                          Boolean isManager);
    public void deleteMember(Integer serialNumber);
    public void updateMember(Integer serialNumber,String name, Integer age, String notes, Member.Level level,
                             LocalDateTime expireDate, Boolean hasPrivateBoat, Integer boatSerialNumber,
                             String phoneNumber, String email, String password, Boolean isManager);
    public Collection<Member> getMembers();
    public Member getMember(Integer serialNumber);
    public Member getMember(String email);


    public void addActivity(String name, LocalDateTime startTime, LocalDateTime finishTime, String boatType);
    public void deleteActivity(Integer serialNumber);
    public void updateActivity(Integer serialNumber,String name, LocalDateTime startTime, LocalDateTime finishTime, String boatType);
    public Collection<Activity> getActivities();
    public Activity getActivity(Integer serialNumber);

}
