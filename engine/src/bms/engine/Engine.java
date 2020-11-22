package bms.engine;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bms.module.Activity;
import bms.module.Boat;
import bms.module.Member;

public class Engine implements BMSEngine{
    @Override
    public void addBoat(String name, String type, Boolean isPrivate, Boolean isWide, Boolean hasCoxswain, Boolean isMarine) {

    }

    @Override
    public void deleteBoat(Integer serialNumber) {

    }

    @Override
    public void updateBoat(String name, String type, Boolean isPrivate, Boolean isWide, Boolean hasCoxswain, Boolean isMarine, Boolean isDisabled) {

    }

    @Override
    public Collection<Boat> getBoats() {
        return null;
    }

    @Override
    public Boat getBoat(Integer serialNumber) {
        return null;
    }

    @Override
    public void addMember(String name, Integer age, String notes, Member.Level level, LocalDateTime joinDate, LocalDateTime expireDate, Boolean hasPrivateBoat, Integer boatSerialNumber, String phoneNumber, String email, String password, Boolean isManager) {

    }

    @Override
    public void deleteMember(Integer serialNumber) {

    }

    @Override
    public void updateMember(String name, Integer age, String notes, Member.Level level, LocalDateTime expireDate, Boolean hasPrivateBoat, Integer boatSerialNumber, String phoneNumber, String email, String password, Boolean isManager) {

    }

    @Override
    public Collection<Member> getMembers() {
        return null;
    }

    @Override
    public Member getMember(Integer serialNumber) {
        return null;
    }

    @Override
    public Member getMember(String email) {
        return null;
    }

    @Override
    public void addActivity(String name, LocalDateTime startTime, LocalDateTime finishTime, String boatType) {

    }

    @Override
    public void deleteActivity(Integer serialNumber) {

    }

    @Override
    public void updateActivity(String name, LocalDateTime startTime, LocalDateTime finishTime, String boatType) {

    }

    @Override
    public Collection<Activity> getActivities() {
        return null;
    }

    @Override
    public Member getActivity(Integer serialNumber) {
        return null;
    }
//    private Map<Integer, Boat> boats = new HashMap<Integer, Boat>();
//    private Map<Integer, Member> members = new HashMap<Integer, Member>();


//    @Override
//    public void addBoat(Integer serialNumber) {
//        Boat boat = new Boat(serialNumber, "Gal", "2X", true, false, true,
//                false, false);
//
//        boats.put(serialNumber, boat);
//    }
//
//    @Override
//    public void deleteBoat(Integer serialNumber) {
//        boats.remove(serialNumber);
//    }
//
//    //@Override
//    //public void updateBoat() {
//    //}
//
//    @Override
//    public Collection<Boat> getBoats() {
//        return boats.values();
//    }
//
//    @Override
//    public Boat getBoat(Integer serialNumber) {
//        return boats.get(serialNumber);
//    }
//
//
//
//
//
//    @Override
//    public void addMember(Integer serialNumber) {
//        Member member = new Member(serialNumber, "Gal", 20, "", Member.Level.BEGINNER, LocalDateTime.now(),
//                LocalDateTime.now(), true, 1, "0544444444", "gal@gmail.com",
//                "1234", true);
//
//        members.put(serialNumber, member);
//    }
//
//    @Override
//    public void deleteMember(Integer serialNumber) {
//        members.remove(serialNumber);
//    }
//
//    @Override
//    public Collection<Member> getMembers() {
//        return members.values();
//    }
//
//    @Override
//    public Member getMember(Integer serialNumber) {
//        return members.get(serialNumber);
//    }

}
