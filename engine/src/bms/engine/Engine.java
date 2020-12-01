package bms.engine;

import java.time.LocalDate;
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
                          LocalDate joinDate, LocalDate expireDate, boolean hasPrivateBoat,
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
    public void deleteActivity(int id)
            throws Exceptions.ActivityAlreadyExistsException {

        activities.deleteActivity(id);
    }

    @Override
    public Collection<Activity> getActivities() {
        return activities.getActivities();
    }

    @Override
    public Activity getActivity(int id) {
        return activities.getActivity(id);
    }


    public void updateActivityName(int id, String name)
            throws Exceptions.ActivityNotFoundException  {
        activities.setActivityName(id, name);

    }
    public void updateActivityStartTime(int id, LocalTime startTime)
            throws Exceptions.ActivityNotFoundException{
        activities.setActivityStartTime(id, startTime);
    }

    public void updateActivityFinishTime(int id, LocalTime finishTime)
            throws Exceptions.ActivityNotFoundException {
        activities.setActivityFinishTime(id, finishTime);
    }

    public void updateActivityBoatType(int id, String boatType)
            throws Exceptions.ActivityNotFoundException{
        activities.setActivityBoatType(id, boatType);
    }


}
