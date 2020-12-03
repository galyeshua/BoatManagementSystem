package bms.engine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;

import bms.engine.list.manager.BoatManager;
import bms.engine.list.manager.ActivityManager;
import bms.engine.list.manager.Exceptions;
import bms.engine.list.manager.MemberManager;

import bms.module.*;


public class Engine implements BMSEngine{
    BoatManager boats = new BoatManager();
    MemberManager members = new MemberManager();
    ActivityManager activities = new ActivityManager();


    @Override
    public void addBoat(int serialNumber, String name, Boat.Rowers numOfRowers, Boat.Paddles numOfPaddles, boolean isPrivate,
                        boolean isWide, boolean hasCoxswain, boolean isMarine, boolean isDisabled)
            throws Exceptions.BoatAlreadyExistsException, Exceptions.IllegalBoatValueException {

        Boat boat = new Boat(serialNumber, name,numOfRowers,numOfPaddles,isPrivate,isWide,hasCoxswain,isMarine,isDisabled);
        boats.addBoat(boat);
    }

    @Override
    public void deleteBoat(int serialNumber) throws Exceptions.BoatNotFoundException {
        boats.deleteBoat(serialNumber);
    }

    @Override
    public Collection<BoatView> getBoats() {
        //return boats.getBoats();
        return Collections.unmodifiableCollection(boats.getBoats());
    }

    @Override
    public BoatView getBoat(int serialNumber)  {
        return boats.getBoat(serialNumber);
    }

    @Override
    public BoatView getBoat(String name)  {
        return boats.getBoat(name);
    }

    public void updateBoat(Boat newBoat) throws Exceptions.BoatNotFoundException {
        boats.updateBoat(newBoat);
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
    public Collection<MemberView> getMembers() {
        return Collections.unmodifiableCollection(members.getMembers());
    }

    @Override
    public MemberView getMember(int serialNumber) {
        return members.getMember(serialNumber);
    }

    @Override
    public MemberView getMember(String email) {
        return members.getMember(email);
    }

    @Override
    public void updateMember(Member newMember) throws Exceptions.MemberNotFoundException{
        members.updateMember(newMember);
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
    public Collection<ActivityView> getActivities() {
        return Collections.unmodifiableCollection(activities.getActivities());
    }

    @Override
    public ActivityView getActivity(int id) {
        return activities.getActivity(id);
    }


    public void updateActivity(Activity newActivity)
            throws Exceptions.ActivityNotFoundException  {
        activities.updateActivity(newActivity);

    }

}
