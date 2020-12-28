package bms.module;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
import java.util.stream.Stream;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "boat")
public class Boat implements BoatView {
    @XmlAttribute(required = true)
    private int serialNumber;

    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute(required = true)
    private Rowers numOfRowers;

    @XmlAttribute
    private Paddles numOfPaddles;

    @XmlAttribute
    private Boolean isPrivate;

    @XmlAttribute
    private Boolean isWide;

    @XmlAttribute(required = true)
    private Boolean hasCoxswain;

    @XmlAttribute
    private Boolean isMarine;

    @XmlAttribute
    private Boolean isDisabled;

    private Boat(){}

    public Boat(int serialNumber, String name, BoatType boatType) throws IllegalValueException {
        this.setSerialNumber(serialNumber);
        this.setName(name);
        this.setType(boatType);
        this.setPrivate(false);
        this.setWide(false);
        this.setMarine(false);
        this.setDisabled(false);
    }

    public Boat(int serialNumber, String name, Rowers numOfRowers, Paddles numOfPaddles,
                Boolean isPrivate, Boolean isWide, Boolean hasCoxswain, Boolean isMarine,
                Boolean isDisabled) throws IllegalValueException {
        this.setSerialNumber(serialNumber);
        this.setName(name);
        this.setNumOfRowers(numOfRowers);
        this.setNumOfPaddles(numOfPaddles);
        this.setPrivate(isPrivate);
        this.setWide(isWide);
        this.setHasCoxswain(hasCoxswain);
        this.setMarine(isMarine);
        this.setDisabled(isDisabled);
    }

    public Boat(BoatView other) throws IllegalValueException {
        this.setSerialNumber(other.getSerialNumber());
        this.setName(other.getName());
        this.setNumOfRowers(other.getNumOfRowers());
        this.setNumOfPaddles(other.getNumOfPaddles());
        this.setPrivate(other.getPrivate());
        this.setWide(other.getWide());
        this.setHasCoxswain(other.getHasCoxswain());
        this.setMarine(other.getMarine());
        this.setDisabled(other.getDisabled());
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
    public Rowers getNumOfRowers() {
        return numOfRowers;
    }
    @Override
    public Paddles getNumOfPaddles() {
        return numOfPaddles;
    }
    @Override
    public boolean getPrivate() {
        return isPrivate;
    }
    @Override
    public boolean getWide() {
        return isWide;
    }
    @Override
    public boolean getHasCoxswain() {
        return hasCoxswain;
    }
    @Override
    public boolean getMarine() {
        return isMarine;
    }
    @Override
    public boolean getDisabled() {
        return isDisabled;
    }

    @Override
    public String getFormattedCode(){
        String result = "";
        result += getNumOfRowers().getNum();
        if (getNumOfPaddles().equals(Paddles.DOUBLE))
            result += 'X';
        else
            result += '-';

        if (getHasCoxswain())
            result += '+';

        result = result.replace("-+", "+");

        if (getWide())
            result += " wide";

        if (getMarine())
            result += " costal";

        return result;
    }

    public BoatType getType(){
        return Stream.of(BoatType.values())
                .filter(b -> b.HasCoxswain() == getHasCoxswain())
                .filter(b -> b.getNumOfPaddles() == getNumOfPaddles())
                .filter(b -> b.getNumOfRowers() == getNumOfRowers())
                .findFirst().get();
    }

    public int getAllowedNumOfRowers(){
        int res = Integer.parseInt(getNumOfRowers().getNum());
        if(getHasCoxswain())
            res += 1;
        return res;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
    public void setName(String name) throws IllegalValueException {
        if (name.trim().isEmpty())
            throw new IllegalValueException("Name cannot be empty");
        this.name = name.trim();
    }

    public void setNumOfRowers(Rowers numOfRowers) {
        this.numOfRowers = numOfRowers;
    }
    public void setNumOfPaddles(Paddles numOfPaddles) throws IllegalValueException {
        if (getNumOfRowers().equals(Rowers.ONE))
            if (numOfPaddles.equals(Paddles.SINGLE))
                throw new IllegalValueException("Boat with size ONE cannot have Single Paddles");
        this.numOfPaddles = numOfPaddles;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setWide(boolean wide) {
        isWide = wide;
    }

    public void setHasCoxswain(boolean hasCoxswain) throws IllegalValueException {
        if (getNumOfRowers().equals(Rowers.ONE))
            if (hasCoxswain)
                throw new IllegalValueException("Boat with size ONE cannot have coxswain");

        if (getNumOfRowers().equals(Rowers.EIGHT))
            if (!hasCoxswain)
                throw new IllegalValueException("Boat with size EIGHT must have coxswain");

        this.hasCoxswain = hasCoxswain;
    }

    public void setMarine(boolean marine) {
        isMarine = marine;
    }
    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
    public void setType(BoatType type) throws IllegalValueException {
        this.setNumOfRowers(type.getNumOfRowers());
        this.setNumOfPaddles(type.getNumOfPaddles());
        this.setHasCoxswain(type.HasCoxswain());
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

    public static class AlreadyAllocatedException extends Exception { }

    public static class BelongsToMember extends Exception { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boat boat = (Boat) o;
        return serialNumber == boat.serialNumber &&
                name.equalsIgnoreCase(boat.name) &&
                numOfRowers == boat.numOfRowers &&
                numOfPaddles == boat.numOfPaddles &&
                Objects.equals(isPrivate, boat.isPrivate) &&
                Objects.equals(isWide, boat.isWide) &&
                Objects.equals(hasCoxswain, boat.hasCoxswain) &&
                Objects.equals(isMarine, boat.isMarine) &&
                Objects.equals(isDisabled, boat.isDisabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber, name, numOfRowers, numOfPaddles, isPrivate, isWide, hasCoxswain, isMarine, isDisabled);
    }
}
