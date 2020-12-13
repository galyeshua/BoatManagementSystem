package bms.module;

import bms.engine.list.manager.Exceptions;
import bms.module.adapter.LocalDateAdapter;
import bms.module.adapter.LocalTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.regex.Pattern;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "member")
public class Member implements MemberView {


    @XmlAttribute(required = true)
    private int serialNumber;

    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute
    private Integer age;

    @XmlAttribute
    private String notes;

    @XmlAttribute
    private Level level;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    @XmlAttribute(required = true)
    private LocalDate joinDate;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    @XmlAttribute(required = true)
    private LocalDate expireDate;

    @XmlAttribute
    private boolean hasPrivateBoat;

    @XmlAttribute
    private Integer boatSerialNumber;

    @XmlAttribute
    private String phoneNumber;

    @XmlAttribute(required = true)
    private String email;

    @XmlAttribute(required = true)
    private String password;

    @XmlAttribute
    private boolean isManager;

    private Member() {}

    public Member(int serialNumber, String name, String email, String password) {
        this.setSerialNumber(serialNumber);
        this.setName(name);
        this.setPassword(password);
        this.setEmail(email);

        this.setBoatSerialNumber(null);
        this.setAge(null);
        this.setLevel(Level.BEGINNER);
        this.setManager(false);
        //this.setHasPrivateBoat(false);
        this.setPhoneNumber(null);
        this.setNotes(null);
        this.setJoinDate(LocalDate.now());
        this.setExpireDate(LocalDate.now().plusYears(1));
    }

//
//    public Member(int serialNumber, String name, int age, String notes, Level level, LocalDate joinDate,
//                  LocalDate expireDate, boolean hasPrivateBoat, int boatSerialNumber, String phoneNumber,
//                  String email, String password, boolean isManager) {
//        this.setSerialNumber(serialNumber);
//        this.setName(name);
//        this.setAge(age);
//        this.setNotes(notes);
//        this.setLevel(level);
//        this.setJoinDate(joinDate);
//        this.setExpireDate(expireDate);
//        this.setHasPrivateBoat(hasPrivateBoat);
//        this.setBoatSerialNumber(boatSerialNumber);
//        this.setPhoneNumber(phoneNumber);
//        this.setEmail(email);
//        this.setPassword(password);
//        this.setManager(isManager);
//    }

    public Member(MemberView other) {
        this.setSerialNumber(other.getSerialNumber());
        this.setName(other.getName());
        this.setAge(other.getAge());
        this.setNotes(other.getNotes());
        this.setLevel(other.getLevel());
        this.setJoinDate(other.getJoinDate());
        this.setExpireDate(other.getExpireDate());
        //this.setHasPrivateBoat(other.getHasPrivateBoat());
        this.setBoatSerialNumber(other.getBoatSerialNumber());
        this.setPhoneNumber(other.getPhoneNumber());
        this.setEmail(other.getEmail());
        this.setPassword(other.getPassword());
        this.setManager(other.getManager());
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

    public void printMember(){
        System.out.println("Member S/N: " +serialNumber +
                ", name: " + name +
                ((age != null) ? ", age: " + age : "")+
                ((notes != null) ? ", notes: " + notes : "")+
                ((level != null) ? ", level: " + level : "")+
                ((joinDate != null) ? ", joinDate: " + joinDate : "")+
                ((expireDate != null) ? ", expireDate: " + expireDate : "")+
                ", hasPrivateBoat: " + printYesOrNo(hasPrivateBoat) +
                ((hasPrivateBoat)? ", boatSerialNumber: " + boatSerialNumber : "")+
                ((phoneNumber != null) ? ", phoneNumber: " + phoneNumber : "")+
                ((email != null) ? ", email: " + email : "")+
                ((password != null) ? ", password: " + password : "")+
                ", isManager: " + printYesOrNo(isManager));
    }
    public String printYesOrNo(boolean attribute){
        if (attribute)
            return "Yes";
        return "No";
    }

    @Override
    public int getSerialNumber() {
        return serialNumber;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public Integer getAge() {
        return age;
    }
    @Override
    public String getNotes() {
        return notes;
    }
    @Override
    public Level getLevel() {
        return level;
    }
    @Override
    public LocalDate getJoinDate() {
        return joinDate;
    }
    @Override
    public LocalDate getExpireDate() {
        return expireDate;
    }
    @Override
    public boolean getHasPrivateBoat() {
        return hasPrivateBoat;
    }
    @Override
    public Integer getBoatSerialNumber() {
        return boatSerialNumber;
    }
    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }
    @Override
    public String getEmail() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public boolean getManager() {
        return isManager;
    }



    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
    public void setName(String name) {
        if (name.trim().isEmpty())
            throw new Exceptions.IllegalMemberValueException("Name cannot be empty");
        this.name = name.trim();
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setLevel(Level level) {
        this.level = level;
    }
    public void setJoinDate(LocalDate joinDate) {
        if (getExpireDate() != null)
            if(joinDate.isAfter(getExpireDate()) || joinDate.isEqual(getExpireDate()))
                throw new Exceptions.IllegalMemberValueException("Join date must be before Expired date");
        this.joinDate = joinDate;
    }
    public void setExpireDate(LocalDate expireDate) {

        if (getJoinDate() != null)
            if(expireDate.isBefore(getJoinDate()) || expireDate.isEqual(getJoinDate())){
                throw new Exceptions.IllegalMemberValueException("Expired date must be after Join date");
            }
        this.expireDate = expireDate;
    }
    private void setHasPrivateBoat(boolean hasPrivateBoat) {
        if(hasPrivateBoat==true && getBoatSerialNumber() == null)
            throw new Exceptions.IllegalMemberValueException("Private boat serial number not specified");
        this.hasPrivateBoat = hasPrivateBoat;
    }
    public void setBoatSerialNumber(Integer boatSerialNumber) {
        this.boatSerialNumber = boatSerialNumber;
        if(boatSerialNumber == null)
            setHasPrivateBoat(false);
        else
            setHasPrivateBoat(true);
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setEmail(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(email).matches())
            throw new Exceptions.IllegalMemberValueException("Email address is not valid");

        this.email = email;
    }
    public void setPassword(String password) {
        if (password.trim().isEmpty())
            throw new Exceptions.IllegalMemberValueException("Password cannot be empty");
        this.password = password.trim();
    }
    public void setManager(boolean manager) {
        isManager = manager;
    }
}
