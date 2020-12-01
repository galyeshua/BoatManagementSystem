package bms.engine.list.manager;

import bms.module.Activity;
import bms.module.Boat;

import java.time.LocalTime;
import java.util.*;

public class ActivityManager {
    private List<Activity> activities = new ArrayList<Activity>();

    public void addActivity(Activity activity) {
        activities.add(activity);
        //activities.sort();
    }

    public void deleteActivity(String name, LocalTime startTime, LocalTime finishTime) {
        Activity activity = getActivity(name, startTime, finishTime);
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

    public void setActivityName(String name, LocalTime startTime, LocalTime finishTime, String name1) throws Exceptions.BoatNotFoundException{
        Activity activity = getActivity(name,startTime,finishTime);
        if (activity == null)
            throw new Exceptions.ActivityNotFoundException();
        activity.setName(name1);
    }

    public void setActivityStartTime(String name, LocalTime startTime, LocalTime finishTime, LocalTime startTime1) throws Exceptions.BoatNotFoundException{
        Activity activity = getActivity(name,startTime,finishTime);
        if (activity == null)
            throw new Exceptions.ActivityNotFoundException();
        activity.setStartTime(startTime1);
    }

    public void setActivityFinishTime(String name, LocalTime startTime, LocalTime finishTime, LocalTime finishTime1) throws Exceptions.BoatNotFoundException{
        Activity activity = getActivity(name,startTime,finishTime);
        if (activity == null)
            throw new Exceptions.ActivityNotFoundException();
        activity.setFinishTime(finishTime1);
    }

    public void setActivityBoatType(String name, LocalTime startTime, LocalTime finishTime, String boatType) throws Exceptions.BoatNotFoundException{
        Activity activity = getActivity(name,startTime,finishTime);
        if (activity == null)
            throw new Exceptions.ActivityNotFoundException();
        activity.setBoatType(boatType);
    }

}
