package bms.module;

import java.time.LocalDateTime;

public class Activity {

    private String name;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private String boatType;

    public Activity(String name, LocalDateTime startTime, LocalDateTime finishTime, String boatType) {
        this.name = name;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.boatType = boatType;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "name='" + name + '\'' +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", boatType='" + boatType + '\'' +
                '}';
    }


    public String getName() {
        return name;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public LocalDateTime getFinishTime() {
        return finishTime;
    }
    public String getBoatType() {
        return boatType;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }
    public void setBoatType(String boatType) {
        this.boatType = boatType;
    }
}
