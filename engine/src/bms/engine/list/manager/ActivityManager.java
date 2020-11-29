package bms.engine.list.manager;

import bms.module.Activity;

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


}
