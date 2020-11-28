package bms.engine;

import java.time.LocalDateTime;
import java.util.Collection;

import bms.engine.list.manager.BoatManager;
import bms.engine.list.manager.ActivityManager;
import bms.engine.list.manager.Exceptions;
import bms.engine.list.manager.MemberManager;

import bms.module.Activity;
import bms.module.Boat;
import bms.module.Member;


public class Engine implements BMSEngine{
    BoatManager boats = new BoatManager();
    MemberManager members = new MemberManager();
    ActivityManager activities = new ActivityManager();


    @Override
    public void addBoat(String name, Boat.Rowers numOfRowers, Boat.Paddles numOfPaddles, boolean isPrivate,
                        boolean isWide, boolean hasCoxswain, boolean isMarine, boolean isDisabled)
            throws Exceptions.BoatAlreadyExistsException, Exceptions.IllegalBoatValueException {

        Boat boat = new Boat(name,numOfRowers,numOfPaddles,isPrivate,isWide,hasCoxswain,isMarine,isDisabled);
        boats.addBoat(boat);
    }

    @Override
    public void deleteBoat(int serialNumber) throws Exceptions.BoatNotFoundException {
        boats.deleteBoat(serialNumber);
    }

    @Override
    public Collection<Boat> getBoats() {
       return boats.getBoats();
    }

    @Override
    public Boat getBoat(int serialNumber)  {
        return boats.getBoat(serialNumber);
    }

    @Override
    public Boat getBoat(String name)  {
        return boats.getBoat(name);
    }

//    @Override
//    public void updateBoat(int serialNumber,String name, Boat.Rowers numOfRowers, Boat.Paddles numOfPaddles,
//                           boolean isPrivate, boolean isWide, boolean hasCoxswain, boolean isMarine,
//                           boolean isDisabled)
//            throws Exceptions.BoatNotFoundException, Exceptions.IllegalBoatValueExeption{
//
//        Boat oldBoat = getBoat(serialNumber);
//
//        Boat newBoat = new Boat(serialNumber, name, numOfRowers, numOfPaddles, isPrivate, isWide, hasCoxswain,
//                isMarine, isDisabled);
//
//        boats.updateBoat(oldBoat.getSerialNumber(), newBoat);
//        // Update XML
//    }

    public void updateBoatName(int serialNumber, String name) throws Exceptions.BoatNotFoundException{
        boats.setBoatName(serialNumber, name);
    }

    public void updateBoatNumOfRowers(int serialNumber, Boat.Rowers numOfRowers) throws Exceptions.BoatNotFoundException{
        boats.setBoatNumOfRowers(serialNumber, numOfRowers);
    }

    public void updateBoatNumOfPaddles(int serialNumber, Boat.Paddles numOfPaddles) throws Exceptions.BoatNotFoundException{
        boats.setBoatNumOfPaddles(serialNumber, numOfPaddles);
    }

    public void updateBoatPrivate(int serialNumber, boolean isPrivate) throws Exceptions.BoatNotFoundException{
        boats.setBoatPrivate(serialNumber, isPrivate);
    }

    public void updateBoatWide(int serialNumber, boolean isWide) throws Exceptions.BoatNotFoundException{
        boats.setBoatWide(serialNumber, isWide);
    }

    public void updateBoatCoxswain(int serialNumber, boolean hasCoxswain)
            throws Exceptions.BoatNotFoundException,Exceptions.IllegalBoatValueException {
        boats.setBoatCoxswain(serialNumber, hasCoxswain);
    }

    public void updateBoatMarine(int serialNumber, boolean isMarine) throws Exceptions.BoatNotFoundException{
        boats.setBoatMarine(serialNumber, isMarine);
    }

    public void updateBoatDisabled(int serialNumber, boolean isDisabled) throws Exceptions.BoatNotFoundException{
        boats.setBoatDisabled(serialNumber, isDisabled);
    }








    @Override
    public void addMember(int serialNumber, String name, int age, String notes, Member.Level level,
                          LocalDateTime joinDate, LocalDateTime expireDate, boolean hasPrivateBoat,
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
    public Collection<Member> getMembers() {
        return members.getMembers();
    }

    @Override
    public Member getMember(int serialNumber) {
        return members.getMember(serialNumber);
    }

    @Override
    public Member getMember(String email) {
        return members.getMember(email);
    }



//    @Override
//    public void updateMember(Integer serialNumber,String name, Integer age, String notes, Member.Level level, LocalDateTime expireDate, Boolean hasPrivateBoat,
//                             Integer boatSerialNumber, String phoneNumber, String email, String password, Boolean isManager) {
//        Member old = members.get(serialNumber);
//        if(name != null)
//            old.setName(name);
//        if(age != null)
//            old.setAge(age);
//        if(notes != null)
//            old.setNotes(notes);
//        if(level != null)
//            old.setLevel(level);
//        if(expireDate != null)
//            old.setExpireDate(expireDate);
//        if(hasPrivateBoat != null)
//            old.setHasPrivateBoat(hasPrivateBoat);
//        if(boatSerialNumber != null)
//            old.setBoatSerialNumber(boatSerialNumber);
//        if(phoneNumber != null)
//            old.setPhoneNumber(phoneNumber);
//        if(email != null)
//            old.setEmail(email);
//        if(password != null)
//            old.setPassword(password);
//        if(isManager != null)
//            old.setManager(isManager);
//        members.update(old);
//    }



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
