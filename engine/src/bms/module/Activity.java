package bms.module;

import bms.engine.list.manager.Exceptions;
import bms.module.adapter.LocalTimeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalTime;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "activity")
public class Activity implements ActivityView {
    private static int counter = 0;

    //@XmlAttribute
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

    public Activity(LocalTime startTime, LocalTime finishTime) {
        this.setId(0);
        this.setName(null);
        this.setBoatType(null);
        this.setStartTime(startTime);
        this.setFinishTime(finishTime);
    }

    public Activity(String name, LocalTime startTime, LocalTime finishTime) {
        this.setId(counter++);
        this.setName(name);
        this.setStartTime(startTime);
        this.setFinishTime(finishTime);
        this.setBoatType(null);
    }

    public Activity(ActivityView other) {
        this.setId(other.getId());
        this.setName(other.getName());
        this.setStartTime(other.getStartTime());
        this.setFinishTime(other.getFinishTime());
        this.setBoatType(other.getBoatType());
    }


    public void printActivity()
    {
        System.out.println("Activity Name: (" + name + ") at " + startTime + "-" + finishTime +
                ((boatType != null) ? " BoatTye: " + boatType : "") );
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", boatType=" + boatType +
                '}';
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
    public void setName(String name) {
        if (name != null) {
            if(name.trim().isEmpty())
                throw new Exceptions.IllegalActivityValueException("Name cannot be empty");
            this.name = name.trim();
        }
        this.name = name;
    }

    public void setStartTime(LocalTime startTime) {
        if (getFinishTime() != null)
            if(startTime.isAfter(getFinishTime()) || startTime.equals(getFinishTime()))
                throw new Exceptions.IllegalActivityValueException("Start time must be before finish time");
        this.startTime = startTime;
    }

    public void setFinishTime(LocalTime finishTime) {
        if (getStartTime() != null)
            if(finishTime.isBefore(getStartTime()) || finishTime.equals(getStartTime()))
                throw new Exceptions.IllegalActivityValueException("Finish time must be after start time");
        this.finishTime = finishTime;
    }

    public void setBoatType(BoatView.BoatType boatType) {
        this.boatType = boatType;
    }

}
