package bms.engine;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Override
    public void updateMemberName(int serialNumber, String name) throws Exceptions.MemberNotFoundException {
        members.setMemberName(serialNumber, name);
    }

    public void updateMemberAge(int serialNumber, int age) throws Exceptions.MemberNotFoundException {
        members.setMemberAge(serialNumber, age);
    }
    @Override
    public void updateMemberNotes(int serialNumber, String notes) throws Exceptions.MemberNotFoundException {
        members.setMemberNotes(serialNumber,notes);
    }

    @Override
    public void updateMemberLevel(int serialNumber, Member.Level level) throws Exceptions.MemberNotFoundException {
        members.setMemberLevel(serialNumber,level);
    }

    @Override
    public void updateMemberPrivateBoat(int serialNumber, boolean hasBoat) throws Exceptions.MemberNotFoundException {
        members.setMemberPrivateBoat(serialNumber,hasBoat);
    }

    @Override
    public void updateBoatSerialNumber(int serialNumber, int boatSerialNumber) {
        members.setMemberBoatSerialNumber(serialNumber,boatSerialNumber);
    }

    @Override
    public void updateMemberPhone(int serialNumber, String phone) throws Exceptions.MemberNotFoundException {
        members.setMemberPhone(serialNumber,phone);
    }

    @Override
    public void updateMemberEmail(int serialNumber, String email) throws Exceptions.MemberNotFoundException {
        members.setMemberEmail(serialNumber,email);
    }

    @Override
    public void updateMemberPassword(int serialNumber, String password) throws Exceptions.MemberNotFoundException {
        members.setMemberPassword(serialNumber,password);
    }

    @Override
    public void updateMemberRole(int serialNumber, boolean isManager) throws Exceptions.MemberNotFoundException {
        members.setMemberRole(serialNumber,isManager);
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
    public void deleteActivity(String name, LocalTime startTime, LocalTime finishTime)
            throws Exceptions.ActivityAlreadyExistsException {
        activities.deleteActivity(name, startTime, finishTime);
    }

    @Override
    public Collection<Activity> getActivities() {
        return activities.getActivities();
    }

    @Override
    public Activity getActivity(String name, LocalTime startTime, LocalTime finishTime) {
        return activities.getActivity(name, startTime, finishTime);
    }


    public void updateActivityName(String name, LocalTime startTime, LocalTime finishTime, String name1)
            throws Exceptions.ActivityNotFoundException  {
        activities.setActivityName(name,startTime,finishTime,name1);

    }
    public void updateActivityStartTime(String name, LocalTime startTime, LocalTime finishTime,  LocalTime startTime1)
            throws Exceptions.ActivityNotFoundException{
        activities.setActivityStartTime(name,startTime,finishTime,startTime1);
    }

    public void updateActivityFinishTime(String name, LocalTime startTime, LocalTime finishTime, LocalTime finishTime1)
            throws Exceptions.ActivityNotFoundException {
        activities.setActivityFinishTime(name,startTime,finishTime,finishTime1);
    }

    public void updateActivityBoatType(String name, LocalTime startTime, LocalTime finishTime, String boatType)
            throws Exceptions.ActivityNotFoundException{
        activities.setActivityBoatType(name,startTime,finishTime,boatType);
    }




//    @Override
//    public void updateActivity(Integer serialNumber,String name, LocalDateTime startTime, LocalDateTime finishTime, String boatType) {
//        //Activity old = activities.get(serialNumber);
////       if (old==null)
////           System.out.println("boo");
////       else {
////
////           if (name != null)
////               old.setName(name);
////           if (startTime != null)
////               old.setStartTime(startTime);
////           if (finishTime != null)
////               old.setFinishTime(finishTime);
////           if (boatType != null)
////               old.setBoatType(boatType);
////           activities.update(old);
////       }
//
//    }




}
