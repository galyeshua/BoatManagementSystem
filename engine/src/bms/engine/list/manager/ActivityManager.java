package bms.engine.list.manager;

import bms.module.Activity;

import java.time.LocalTime;
import java.util.*;

public class ActivityManager {
    private List<Activity> activities = new ArrayList<Activity>();

    public void addActivity(Activity activity) {
        activities.add(activity);
        Collections.sort(activities, ((a1, a2) -> a1.getStartTime().compareTo(a2.getStartTime())));
    }

    public void deleteActivity(int id) {
        Activity activity = getActivity(id);
        activities.remove(activity);
    }

    public Collection<Activity> getActivities() {
        return activities;
    }

    public Activity getActivity(String name, LocalTime startTime, LocalTime finishTime) {
        boolean isNameEquals;
        boolean isStartTimeEquals;
        boolean isFinishTimeEquals;

        for (Activity activity : activities) {
            isNameEquals = activity.getName().equals(name);
            isStartTimeEquals = activity.getStartTime().equals(startTime);
            isFinishTimeEquals = activity.getFinishTime().equals(finishTime);

            if(isNameEquals && isStartTimeEquals && isFinishTimeEquals)
                return activity;
        }
        return null;
    }

    public Activity getActivity(int id) {
        for (Activity activity : activities) {
            if(activity.getId() == id)
                return activity;
        }
        return null;
    }

    public void updateActivity(Activity newActivity)
            throws Exceptions.ActivityNotFoundException, Exceptions.IllegalActivityValueException {
        int id = newActivity.getId();
        Activity currentActivity = getActivity(id);

        if (currentActivity == null)
            throw new Exceptions.ActivityNotFoundException();

        boolean isNameEquals = currentActivity.getName().equals(newActivity.getName());
        boolean isStartTimeEquals = currentActivity.getStartTime().equals(newActivity.getStartTime());
        boolean isFinishTimeEquals = currentActivity.getFinishTime().equals(newActivity.getFinishTime());

        if (!(isNameEquals && isStartTimeEquals && isFinishTimeEquals))
            validateActivityParameters(newActivity.getName(), newActivity.getStartTime(), newActivity.getFinishTime());

        activities.set(id, newActivity);
    }

    private void validateActivityParameters(String name, LocalTime startTime, LocalTime finishTime) {
        if (getActivity(name, startTime, finishTime) != null)
            throw new Exceptions.IllegalActivityValueException("Activity with same name, start time and finish time already exists.");
    }
//
//
//    public void setActivityName(int id, String name) throws Exceptions.ActivityNotFoundException{
//        Activity activity = getActivity(id);
//        if (activity == null)
//            throw new Exceptions.ActivityNotFoundException();
//        activity.setName(name);
//    }
//
//    public void setActivityStartTime(int id, LocalTime startTime) throws Exceptions.ActivityNotFoundException{
//        Activity activity = getActivity(id);
//        if (activity == null)
//            throw new Exceptions.ActivityNotFoundException();
//        activity.setStartTime(startTime);
//    }
//
//    public void setActivityFinishTime(int id, LocalTime finishTime) throws Exceptions.ActivityNotFoundException{
//        Activity activity = getActivity(id);
//        if (activity == null)
//            throw new Exceptions.ActivityNotFoundException();
//        activity.setFinishTime(finishTime);
//    }
//
//    public void setActivityBoatType(int id, String boatType) throws Exceptions.ActivityNotFoundException{
//        Activity activity = getActivity(id);
//        if (activity == null)
//            throw new Exceptions.ActivityNotFoundException();
//        activity.setBoatType(boatType);
//    }

}
