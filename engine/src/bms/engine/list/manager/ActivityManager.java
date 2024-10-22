package bms.engine.list.manager;

import bms.module.Activity;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalTime;
import java.util.*;

public class ActivityManager {
    @XmlElement(name="activity", required = true)
    private List<Activity> activities = new ArrayList<Activity>();

    public void addActivity(Activity activity) throws Activity.AlreadyExistsException, Activity.IllegalValueException {

        Activity tmpActivity = getActivity(activity.getName(), activity.getStartTime(), activity.getFinishTime());

        if (tmpActivity != null)
            throw new Activity.AlreadyExistsException("Activity already exists: '" + activity.getName() +
                    "' for time " + activity.getStartTime() + " - " + activity.getFinishTime());

        activities.add(new Activity(activity));
        Collections.sort(activities, (Comparator.comparing(Activity::getStartTime)));
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
            isNameEquals = activity.getName().equalsIgnoreCase(name);
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

    public void updateActivity(Activity newActivity) throws Activity.NotFoundException, Activity.AlreadyExistsException, Activity.IllegalValueException {
        int id = newActivity.getId();
        Activity currentActivity = getActivity(id);

        if (currentActivity == null)
            throw new Activity.NotFoundException();

        boolean isNameEquals = currentActivity.getName().equalsIgnoreCase(newActivity.getName());
        boolean isStartTimeEquals = currentActivity.getStartTime().equals(newActivity.getStartTime());
        boolean isFinishTimeEquals = currentActivity.getFinishTime().equals(newActivity.getFinishTime());

        if (!(isNameEquals && isStartTimeEquals && isFinishTimeEquals))
            validateActivityParameters(newActivity.getName(), newActivity.getStartTime(), newActivity.getFinishTime());

        activities.set(activities.indexOf(getActivity(id)), new Activity(newActivity));
    }

    private void validateActivityParameters(String name, LocalTime startTime, LocalTime finishTime)
            throws Activity.AlreadyExistsException {
        if (getActivity(name, startTime, finishTime) != null)
            throw new Activity.AlreadyExistsException("Activity with the same name, start time and finish time already exists.");
    }

    public void eraseAll(){
        activities.clear();
    }

}
