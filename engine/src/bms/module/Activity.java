package bms.module;

import java.time.LocalDateTime;

public class Activity {


    private Integer serialNumber;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private String boatType;



    public Activity(Integer serialNumber, String name, LocalDateTime startTime, LocalDateTime finishTime, String boatType) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.boatType = boatType;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "serialNumber=" + serialNumber +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", boatType='" + boatType + '\'' +
                '}';
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
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
