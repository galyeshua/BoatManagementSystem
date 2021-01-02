package bms.module;



import bms.module.adapter.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "member")
public class Member implements MemberView, Serializable {


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

    public Member(int serialNumber, String name, String email, String password) throws IllegalValueException {
        this.setSerialNumber(serialNumber);
        this.setName(name);
        this.setPassword(password);
        this.setEmail(email);
        this.setBoatSerialNumber(null);
        this.setAge(null);
        this.setLevel(Level.BEGINNER);
        this.setManager(false);
        this.setPhoneNumber(null);
        this.setNotes(null);
        this.setJoinDate(LocalDate.now());
        this.setExpireDate(LocalDate.now().plusYears(1));
    }

    public Member(MemberView other) throws IllegalValueException {
        this.setSerialNumber(other.getSerialNumber());
        this.setName(other.getName());
        this.setAge(other.getAge());
        this.setNotes(other.getNotes());
        this.setLevel(other.getLevel());
        this.setJoinDate(other.getJoinDate());
        this.setExpireDate(other.getExpireDate());
        this.setBoatSerialNumber(other.getBoatSerialNumber());
        this.setPhoneNumber(other.getPhoneNumber());
        this.setEmail(other.getEmail());
        this.setPassword(other.getPassword());
        this.setManager(other.getManager());
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

    public void setName(String name) throws IllegalValueException {
        if (name.trim().isEmpty())
            throw new IllegalValueException("Name cannot be empty");
        this.name = name.trim();
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setNotes(String notes) {
        if (notes == null || (notes.trim()).isEmpty())
            this.notes = null;
        else
            this.notes = notes.trim();
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setJoinDate(LocalDate joinDate) throws IllegalValueException {
        if (getExpireDate() != null)
            if(joinDate.isAfter(getExpireDate()) || joinDate.isEqual(getExpireDate()))
                throw new IllegalValueException("Join date must be before Expired date");
        this.joinDate = joinDate;
    }

    public void setExpireDate(LocalDate expireDate) throws IllegalValueException {
        if (getJoinDate() != null)
            if(expireDate.isBefore(getJoinDate()) || expireDate.isEqual(getJoinDate())){
                throw new IllegalValueException("Expired date must be after Join date");
            }
        this.expireDate = expireDate;
    }

    private void setHasPrivateBoat(boolean hasPrivateBoat) throws IllegalValueException {
        if(hasPrivateBoat==true && getBoatSerialNumber() == null)
            throw new IllegalValueException("Private boat serial number not specified");
        this.hasPrivateBoat = hasPrivateBoat;
    }

    public void setBoatSerialNumber(Integer boatSerialNumber) throws IllegalValueException {
        this.boatSerialNumber = boatSerialNumber;
        if(boatSerialNumber == null)
            setHasPrivateBoat(false);
        else
            setHasPrivateBoat(true);
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || (phoneNumber.trim()).isEmpty())
            this.phoneNumber = null;
        else
            this.phoneNumber = phoneNumber.trim();
    }

    public void setEmail(String email) throws IllegalValueException {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(email).matches())
            throw new IllegalValueException("Email address is not valid");

        this.email = email;
    }

    public void setPassword(String password) throws IllegalValueException {
        if (password == null || password.trim().isEmpty())
            throw new IllegalValueException("Password cannot be empty");
        this.password = password.trim();
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public static class NotFoundException extends Exception { }

    public static class AlreadyExistsException extends Exception {
        public AlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class IllegalValueException extends Exception{
        public IllegalValueException(String message) {
            super(message);
        }
    }

    public static class AccessDeniedException extends Exception{ }

    public static class AlreadyHaveApprovedReservationsException extends Exception {
        int memberID;
        public AlreadyHaveApprovedReservationsException() { }
        public AlreadyHaveApprovedReservationsException(int memberID) {
            super();
            this.memberID=memberID;
        }
        public int getMemberID() {
            return memberID;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return serialNumber == member.serialNumber &&
                hasPrivateBoat == member.hasPrivateBoat &&
                isManager == member.isManager &&
                name.equalsIgnoreCase(member.name) &&
                Objects.equals(age, member.age) &&
                Objects.equals(notes, member.notes) &&
                level == member.level &&
                Objects.equals(joinDate, member.joinDate) &&
                Objects.equals(expireDate, member.expireDate) &&
                Objects.equals(boatSerialNumber, member.boatSerialNumber) &&
                Objects.equals(phoneNumber, member.phoneNumber) &&
                email.equalsIgnoreCase(member.email) &&
                Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber, name, age, notes, level, joinDate, expireDate, hasPrivateBoat, boatSerialNumber, phoneNumber, email, password, isManager);
    }
}
