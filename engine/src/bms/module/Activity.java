package bms.module;

import bms.engine.list.manager.Exceptions;

import java.time.LocalTime;

public class Activity implements ActivityView {
    private static int counter = 0;

    private int id;
    private String name;
    private LocalTime startTime = null;
    private LocalTime finishTime = null;
    private String boatType;

    public Activity(LocalTime startTime, LocalTime finishTime) {
        this.setId(0);
        this.setName("");
        this.setBoatType("");
        this.setStartTime(startTime);
        this.setFinishTime(finishTime);
    }

//    public Activity(String name, LocalTime startTime, LocalTime finishTime) {
//        this.setId(counter++);
//        this.setName(name);
//        this.setStartTime(startTime);
//        this.setFinishTime(finishTime);
//        this.setBoatType("");
//    }

    public Activity(String name, LocalTime startTime, LocalTime finishTime, String boatType) {
        this.setId(counter++);
        this.setName(name);
        this.setStartTime(startTime);
        this.setFinishTime(finishTime);
        this.setBoatType(boatType);
    }

    public Activity(ActivityView other) {
        this.setId(other.getId());
        this.setName(other.getName());
        this.setStartTime(other.getStartTime());
        this.setFinishTime(other.getFinishTime());
        this.setBoatType(other.getBoatType());
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", boatType='" + boatType + '\'' +
                '}';
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
    public String getBoatType() {
        return boatType;
    }

    public boolean isOverlapping(ActivityView other){
//        boolean timesAreEqual = getStartTime().equals(other.getStartTime()) &&
//                getFinishTime().equals(other.getFinishTime());
        boolean otherIsBefore = other.getFinishTime().isBefore(getStartTime());
        boolean otherIsAfter = other.getStartTime().isAfter(getFinishTime());

        return !(otherIsBefore || otherIsAfter);
    }


    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(LocalTime startTime) {
        if (getFinishTime() != null)
            if(startTime.isAfter(getFinishTime()))
                throw new Exceptions.IllegalActivityValueException("Start time must be before finish time");
        this.startTime = startTime;
    }

    public void setFinishTime(LocalTime finishTime) {
        if (getStartTime() != null)
            if(finishTime.isBefore(getStartTime()))
                throw new Exceptions.IllegalActivityValueException("Finish time must be after start time");
        this.finishTime = finishTime;
    }

    public void setBoatType(String boatType) {
        this.boatType = boatType;
    }

}
