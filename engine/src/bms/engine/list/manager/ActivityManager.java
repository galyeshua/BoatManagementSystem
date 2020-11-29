package bms.engine.list.manager;

import bms.module.Activity;

import java.time.LocalDateTime;
import java.util.*;

public class ActivityManager {
      private List activities =new ArrayList<Activity>();

    public void add(Activity activity) {
        activities.add(activity);
    }

    public void delete(String name, LocalDateTime startTime, LocalDateTime finishTime) {

        activities.remove(name,startTime,finishTime);
    }

    public Collection<Activity> getAll() {
        return activities;
    }

    public Activity get(String name, LocalDateTime startTime, LocalDateTime finishTime) {

        return activities.get(name,startTime,finishTime);
    }


}
