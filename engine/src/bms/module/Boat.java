package bms.module;

import bms.engine.list.manager.Exceptions;

public class Boat {
    private static Integer counter = 1;

    public enum Rowers {
        ONE("1"), TWO("2"), FOUR("4"), EIGHT("8") ;
        private String num;

        Rowers(String num) {
            this.num = num;
        }
        private String getNum(){
            return this.num;
        }
    }
    public enum Paddles { SINGLE, DOUBLE }


    private int serialNumber;
    private String name;
    private Rowers numOfRowers;
    private Paddles numOfPaddles;
    private boolean isPrivate;
    private boolean isWide;
    private boolean hasCoxswain;
    private boolean isMarine;
    private boolean isDisabled;


    public Boat(String name, Rowers numOfRowers, Paddles numOfPaddles, Boolean isPrivate,
                Boolean isWide, Boolean hasCoxswain, Boolean isMarine, Boolean isDisabled)
            throws Exceptions.IllegalBoatValueException {

        this.setSerialNumber(Boat.counter++);
        this.setName(name);
        this.setNumOfRowers(numOfRowers);
        this.setNumOfPaddles(numOfPaddles);
        this.setPrivate(isPrivate);
        this.setWide(isWide);
        this.setHasCoxswain(hasCoxswain);
        this.setMarine(isMarine);
        this.setDisabled(isDisabled);
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

    @Override
    public String toString() {
        return "Boat{" +
                "serialNumber=" + serialNumber +
                ", name='" + name + '\'' +
                ", numOfRowers=" + numOfRowers +
                ", numOfPaddles=" + numOfPaddles +
                ", isPrivate=" + isPrivate +
                ", isWide=" + isWide +
                ", hasCoxswain=" + hasCoxswain +
                ", isMarine=" + isMarine +
                ", isDisabled=" + isDisabled +
                '}';
    }


    public static int getCounter() {
        return Boat.counter;
    }
    public int getSerialNumber() {
        return serialNumber;
    }
    public String getName() {
        return name;
    }
    public Rowers getNumOfRowers() {
        return numOfRowers;
    }
    public Paddles getNumOfPaddles() {
        return numOfPaddles;
    }
    public boolean getPrivate() {
        return isPrivate;
    }
    public boolean getWide() {
        return isWide;
    }
    public boolean getHasCoxswain() {
        return hasCoxswain;
    }
    public boolean getMarine() {
        return isMarine;
    }
    public boolean getDisabled() {
        return isDisabled;
    }



    public static void setCounter(int counter) {
        Boat.counter = counter;
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
    public void setNumOfPaddles(Paddles numOfPaddles) {
        this.numOfPaddles = numOfPaddles;
    }
    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
    public void setWide(boolean wide) {
        isWide = wide;
    }

    public void setHasCoxswain(boolean hasCoxswain) throws Exceptions.IllegalBoatValueException{
        if (this.numOfRowers.equals(Rowers.ONE))
            if (hasCoxswain)
                throw new Exceptions.IllegalBoatValueException("Boat with size ONE cannot have coxswain");

        if (this.numOfRowers.equals(Rowers.EIGHT))
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

}
