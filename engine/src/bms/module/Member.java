package bms.module;

import java.time.LocalDateTime;

public class Member {
    public enum Level { BEGINNER, MODERATE, PROFESIONAL }

    private Integer serialNumber;
    private String name;
    private Integer age;
    private String notes;
    private Level level;
    private LocalDateTime joinDate; // need to change to Date
    private LocalDateTime expireDate; // need to change to Date
    private Boolean hasPrivateBoat;
    private Integer boatSerialNumber;
    private String phoneNumber;
    private String email;
    private String password;
    private Boolean isManager;


    public Member(Integer serialNumber, String name, Integer age, String notes, Level level,
                  LocalDateTime joinDate, LocalDateTime expireDate, Boolean hasPrivateBoat, Integer boatSerialNumber,
                  String phoneNumber, String email, String password, Boolean isManager) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.age = age;
        this.notes = notes;
        this.level = level;
        this.joinDate = joinDate;
        this.expireDate = expireDate;
        this.hasPrivateBoat = hasPrivateBoat;
        this.boatSerialNumber = boatSerialNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.isManager = isManager;
    }

    @Override
    public String toString() {
        return "Member{" +
                "serialNumber=" + serialNumber +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", notes='" + notes + '\'' +
                ", level='" + level + '\'' +
                ", joinDate='" + joinDate + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", hasPrivateBoat=" + hasPrivateBoat +
                ", boatSerialNumber=" + boatSerialNumber +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isManager=" + isManager +
                '}';
    }
}
