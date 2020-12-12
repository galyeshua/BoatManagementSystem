package bms.module;

import bms.engine.list.manager.Exceptions;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.stream.Stream;

public class Boat implements BoatView {
    @XmlAttribute
    private int serialNumber;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private Rowers numOfRowers;
    @XmlAttribute
    private Paddles numOfPaddles;
    @XmlAttribute
    private Boolean isPrivate;
    @XmlAttribute
    private Boolean isWide;
    @XmlAttribute
    private Boolean hasCoxswain;
    @XmlAttribute
    private Boolean isMarine;
    @XmlAttribute
    private Boolean isDisabled;



    public Boat(int serialNumber, String name, BoatType boatType) throws Exceptions.IllegalBoatValueException{
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
                Boolean isDisabled) throws Exceptions.IllegalBoatValueException{
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

    public Boat(BoatView other) throws Exceptions.IllegalBoatValueException{
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

//    @Override
//    public String toString() {
//        return "Boat{" +
//                "serialNumber=" + serialNumber +
//                ", name='" + name + '\'' +
//                ", numOfRowers=" + numOfRowers +
//                ", numOfPaddles=" + numOfPaddles +
//                ", isPrivate=" + isPrivate +
//                ", isWide=" + isWide +
//                ", hasCoxswain=" + hasCoxswain +
//                ", isMarine=" + isMarine +
//                ", isDisabled=" + isDisabled +
//                '}';
//    }

    public void printBoat(){
        System.out.println("Boat S/N: " + serialNumber + ", Name: " + name +
                ", numOfRowers: " + numOfRowers +
                ", numOfPaddles: " + numOfPaddles +
                ((isPrivate != null) ? ", isPrivate: " + printYesOrNo(isPrivate) : "") +
                ((isWide != null) ? ", isWide: " + printYesOrNo(isWide) : "") +
                ((hasCoxswain != null) ? ", hasCoxswain: " + printYesOrNo(hasCoxswain) : "") +
                ((isMarine != null) ? ", isMarine: " + printYesOrNo(isMarine) : "") +
                ((isDisabled != null) ? ", isDisabled: " + printYesOrNo(isDisabled) : "") );
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
    public void setName(String name) {
        this.name = name;
    }
    public void setNumOfRowers(Rowers numOfRowers) {
        this.numOfRowers = numOfRowers;
    }
    public void setNumOfPaddles(Paddles numOfPaddles) throws Exceptions.IllegalBoatValueException{
        if (getNumOfRowers().equals(Rowers.ONE))
            if (numOfPaddles.equals(Paddles.SINGLE))
                throw new Exceptions.IllegalBoatValueException("Boat with size ONE cannot have Single Paddles");
        this.numOfPaddles = numOfPaddles;
    }
    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
    public void setWide(boolean wide) {
        isWide = wide;
    }

    public void setHasCoxswain(boolean hasCoxswain) throws Exceptions.IllegalBoatValueException{
        if (getNumOfRowers().equals(Rowers.ONE))
            if (hasCoxswain)
                throw new Exceptions.IllegalBoatValueException("Boat with size ONE cannot have coxswain");

        if (getNumOfRowers().equals(Rowers.EIGHT))
            if (!hasCoxswain)
                throw new Exceptions.IllegalBoatValueException("Boat with size EIGHT must have coxswain");

        this.hasCoxswain = hasCoxswain;
    }
    public void setMarine(boolean marine) {
        isMarine = marine;
    }
    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
    public void setType(BoatType type){
        this.setNumOfRowers(type.getNumOfRowers());
        this.setNumOfPaddles(type.getNumOfPaddles());
        this.setHasCoxswain(type.HasCoxswain());
    }

}
