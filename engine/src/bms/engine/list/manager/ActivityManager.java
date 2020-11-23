package bms.engine.list.manager;

import bms.module.Activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ActivityManager {
    private Map<Integer, Activity> activities = new HashMap<Integer, Activity>();

    public void add(Activity activity) {
        activities.put(activity.getSerialNumber(), activity);
    }

    public void delete(Integer serialNumber) {
        activities.remove(serialNumber);
    }

    public Collection<Activity> getAll() {
        return activities.values();
    }

    public Activity get(Integer serialNumber) {
        return activities.get(serialNumber);
    }

    public void update(Activity obj) {
        activities.replace(obj.getSerialNumber(), obj);
    }


}
