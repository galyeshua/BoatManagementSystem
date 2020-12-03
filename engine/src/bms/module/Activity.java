package bms.module;

import java.time.LocalTime;

public class Activity implements ActivityView {
    private static int counter = 0;

    private int id;
    private String name;
    private LocalTime startTime;
    private LocalTime finishTime;
    private String boatType;

    public Activity(String name, LocalTime startTime, LocalTime finishTime) {
        this.setId(counter++);
        this.setName(name);
        this.setStartTime(startTime);
        this.setFinishTime(finishTime);
        this.setBoatType("");
    }


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


    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public void setFinishTime(LocalTime finishTime) {
        this.finishTime = finishTime;
    }
    public void setBoatType(String boatType) {
        this.boatType = boatType;
    }

}
