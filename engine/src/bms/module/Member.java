package bms.module;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Member {

    public enum Level { BEGINNER, MODERATE, PROFESSIONAL }

    private int serialNumber;
    private String name;
    private int age;
    private String notes;
    private Level level;
    private LocalDate joinDate;
    private LocalDate expireDate;
    private boolean hasPrivateBoat;
    private int boatSerialNumber;
    private String phoneNumber;
    private String email;
    private String password;
    private boolean isManager;

    public Member(int serialNumber, String name, int age, String notes, Level level, LocalDate joinDate,
                  LocalDate expireDate, boolean hasPrivateBoat, int boatSerialNumber, String phoneNumber,
                  String email, String password, boolean isManager) {
        this.setSerialNumber(serialNumber);
        this.setName(name);
        this.setAge(age);
        this.setNotes(notes);
        this.setLevel(level);
        this.setJoinDate(joinDate);
        this.setExpireDate(expireDate);
        this.setHasPrivateBoat(hasPrivateBoat);
        this.setBoatSerialNumber(boatSerialNumber);
        this.setPhoneNumber(phoneNumber);
        this.setEmail(email);
        this.setPassword(password);
        this.setManager(isManager);
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


    public int getSerialNumber() {
        return serialNumber;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    public String getNotes() {
        return notes;
    }
    public Level getLevel() {
        return level;
    }
    public LocalDate getJoinDate() {
        return joinDate;
    }
    public LocalDate getExpireDate() {
        return expireDate;
    }
    public boolean isHasPrivateBoat() {
        return hasPrivateBoat;
    }
    public int getBoatSerialNumber() {
        return boatSerialNumber;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public boolean isManager() {
        return isManager;
    }



    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setLevel(Level level) {
        this.level = level;
    }
    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }
    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }
    public void setHasPrivateBoat(boolean hasPrivateBoat) {
        this.hasPrivateBoat = hasPrivateBoat;
    }
    public void setBoatSerialNumber(int boatSerialNumber) {
        this.boatSerialNumber = boatSerialNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setManager(boolean manager) {
        isManager = manager;
    }
}
