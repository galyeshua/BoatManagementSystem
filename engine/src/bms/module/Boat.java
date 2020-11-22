package bms.module;

public class Boat {
    private Integer serialNumber;
    private String name;
    private String type;
    private Boolean isPrivate;
    private Boolean isWide;
    private Boolean hasCoxswain;
    private Boolean isMarine;
    private Boolean isDisabled;


    public Boat(Integer serialNumber, String name, String type, Boolean isPrivate, Boolean isWide,
                Boolean hasCoxswain, Boolean isMarine, Boolean isDisabled) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.type = type;
        this.isPrivate = isPrivate;
        this.isWide = isWide;
        this.hasCoxswain = hasCoxswain;
        this.isMarine = isMarine;
        this.isDisabled = isDisabled;
    }

    public void disable() {
        isDisabled = true;
    }

    public void enable() {
        isDisabled = false;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    @Override
    public String toString() {
        return "Boat{" +
                "serialNumber=" + serialNumber +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", isPrivate=" + isPrivate +
                ", isWide=" + isWide +
                ", hasCoxswain=" + hasCoxswain +
                ", isMarine=" + isMarine +
                ", isDisabled=" + isDisabled +
                '}';
    }

    public String print() {
        return "Boat{" +
                "serialNumber=" + serialNumber +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", isPrivate=" + isPrivate +
                ", isWide=" + isWide +
                ", hasCoxswain=" + hasCoxswain +
                ", isMarine=" + isMarine +
                ", isDisabled=" + isDisabled +
                '}';
    }

}
