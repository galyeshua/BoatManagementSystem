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

}
