package bms.module;

import bms.module.adapter.LocalTimeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalTime;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "activity")
public class Activity implements ActivityView {
    private static int counter = 0;

    private int id;

    @XmlAttribute(required = true)
    private String name;

    @XmlJavaTypeAdapter(value = LocalTimeAdapter.class)
    @XmlAttribute(required = true)
    private LocalTime startTime = null;

    @XmlJavaTypeAdapter(value = LocalTimeAdapter.class)
    @XmlAttribute(required = true)
    private LocalTime finishTime = null;

    @XmlAttribute
    private BoatView.BoatType boatType = null;

    private Activity(){}

    public Activity(LocalTime startTime, LocalTime finishTime) throws IllegalValueException {
        this.setId(0);
        this.setName(null);
        this.setBoatType(null);
        this.setStartTime(startTime);
        this.setFinishTime(finishTime);
    }

    public Activity(String name, LocalTime startTime, LocalTime finishTime) throws IllegalValueException {
        this.setId(counter++);
        this.setName(name);
        this.setStartTime(startTime);
        this.setFinishTime(finishTime);
        this.setBoatType(null);
    }

    public Activity(ActivityView other) throws IllegalValueException {
        this.setId(other.getId());
        this.setName(other.getName());
        this.setStartTime(other.getStartTime());
        this.setFinishTime(other.getFinishTime());
        this.setBoatType(other.getBoatType());
    }


    public static int getCounter() {
        return counter;
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public LocalTime getStartTime() {
        return startTime;
    }
    @Override
    public LocalTime getFinishTime() {
        return finishTime;
    }
    @Override
    public BoatView.BoatType getBoatType() {
        return boatType;
    }

    public boolean isOverlapping(ActivityView other){
        boolean otherIsBefore = other.getFinishTime().isBefore(getStartTime());
        boolean otherIsAfter = other.getStartTime().isAfter(getFinishTime());

        return !(otherIsBefore || otherIsAfter);
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) throws IllegalValueException {
        if (name != null) {
            if(name.trim().isEmpty())
                throw new IllegalValueException("Name cannot be empty");
            this.name = name.trim();
        }
        this.name = name;
    }

    public void setStartTime(LocalTime startTime) throws IllegalValueException {
        if (getFinishTime() != null)
            if(startTime.isAfter(getFinishTime()) || startTime.equals(getFinishTime()))
                throw new IllegalValueException("Start time must be before finish time");
        this.startTime = startTime;
    }

    public void setFinishTime(LocalTime finishTime) throws IllegalValueException {
        if (getStartTime() != null)
            if(finishTime.isBefore(getStartTime()) || finishTime.equals(getStartTime()))
                throw new IllegalValueException("Finish time must be after start time");
        this.finishTime = finishTime;
    }

    public void setBoatType(BoatView.BoatType boatType) {
        this.boatType = boatType;
    }




    public static class IllegalValueException extends Exception{
        public IllegalValueException(String message) {
            super(message);
        }
    }
    public static class NotFoundException extends Exception { }
    public static class AlreadyExistsException extends Exception {
        public AlreadyExistsException(String message) {
            super(message);
        }
    }
}
