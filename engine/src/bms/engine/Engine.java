package bms.engine;

import java.time.LocalDateTime;
import java.util.Collection;

import bms.engine.list.manager.BoatManager;
import bms.engine.list.manager.ActivityManager;
import bms.engine.list.manager.MemberManager;

import bms.module.Activity;
import bms.module.Boat;
import bms.module.Member;



public class Engine implements BMSEngine{
    BoatManager boats = new BoatManager();
    MemberManager members = new MemberManager();
    ActivityManager activities = new ActivityManager();

    @Override
    public void addBoat(String name, String type, Boolean isPrivate, Boolean isWide, Boolean hasCoxswain, Boolean isMarine) {
        Boat boat = new Boat(0,name,type,isPrivate,isWide,hasCoxswain,isMarine,false);
        boats.add(boat);
    }

    @Override
    public void deleteBoat(Integer serialNumber) {
        boats.delete(serialNumber);
    }

    @Override
    public Collection<Boat> getBoats() {
       return boats.getAll();
    }

    @Override
    public Boat getBoat(Integer serialNumber) {
        return boats.get(serialNumber);
    }

    @Override
    public void updateBoat(Integer serialNumber,String name, String type, Boolean isPrivate, Boolean isWide,
                           Boolean hasCoxswain, Boolean isMarine, Boolean isDisabled) {
        Boat old = boats.get(serialNumber);
        if(name != null)
            old.setName(name);
        if(type != null)
            old.setType(type);
        if(isPrivate != null)
            old.setPrivate(isPrivate);
        if(isWide != null)
            old.setWide(isWide);
        if(hasCoxswain != null)
            old.setHasCoxswain(hasCoxswain);
        if(isMarine != null)
            old.setMarine(isMarine);
        if(isDisabled != null)
            old.setDisabled(isDisabled);
        boats.update(old);
    }


    @Override
    public void addMember(String name, Integer age, String notes, Member.Level level, LocalDateTime joinDate, LocalDateTime expireDate,
                   Boolean hasPrivateBoat, Integer boatSerialNumber, String phoneNumber, String email, String password, Boolean isManager) {
        Member member = new Member(0,name,age,notes,level,joinDate,expireDate,hasPrivateBoat,boatSerialNumber,phoneNumber,email,password,isManager);
        members.add(member);
    }

    @Override
    public void deleteMember(Integer serialNumber) {
        members.delete(serialNumber);
    }

    @Override
    public Collection<Member> getMembers() {
        return members.getAll();
    }

    @Override
    public Member getMember(Integer serialNumber) {
        return members.get(serialNumber);
    }

    @Override
    public Member getMember(String email) {
        Collection<Member> members = getMembers();
        for(Member member : members)
            if(member.getEmail() == email)
                return member;
        return null;
    }

    @Override
    public void updateMember(Integer serialNumber,String name, Integer age, String notes, Member.Level level, LocalDateTime expireDate, Boolean hasPrivateBoat,
                             Integer boatSerialNumber, String phoneNumber, String email, String password, Boolean isManager) {
        Member old = members.get(serialNumber);
        if(name != null)
            old.setName(name);
        if(age != null)
            old.setAge(age);
        if(notes != null)
            old.setNotes(notes);
        if(level != null)
            old.setLevel(level);
        if(expireDate != null)
            old.setExpireDate(expireDate);
        if(hasPrivateBoat != null)
            old.setHasPrivateBoat(hasPrivateBoat);
        if(boatSerialNumber != null)
            old.setBoatSerialNumber(boatSerialNumber);
        if(phoneNumber != null)
            old.setPhoneNumber(phoneNumber);
        if(email != null)
            old.setEmail(email);
        if(password != null)
            old.setPassword(password);
        if(isManager != null)
            old.setManager(isManager);
        members.update(old);


    }



    @Override
    public void addActivity(String name, LocalDateTime startTime, LocalDateTime finishTime, String boatType) {
        Activity activity=new Activity(0,name,startTime,finishTime,boatType);
        activities.add(activity);
    }

    @Override
    public void deleteActivity(Integer serialNumber) {
        activities.delete(serialNumber);
    }

    @Override
    public Collection<Activity> getActivities() {
        return activities.getAll();
    }

    @Override
    public Activity getActivity(Integer serialNumber) {
        return activities.get(serialNumber);
    }


    @Override
    public void updateActivity(Integer serialNumber,String name, LocalDateTime startTime, LocalDateTime finishTime, String boatType) {
        Activity old = activities.get(serialNumber);
       if (old==null)
           System.out.println("boo");
       else {

           if (name != null)
               old.setName(name);
           if (startTime != null)
               old.setStartTime(startTime);
           if (finishTime != null)
               old.setFinishTime(finishTime);
           if (boatType != null)
               old.setBoatType(boatType);
           activities.update(old);
       }
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
