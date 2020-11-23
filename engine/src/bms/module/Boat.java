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


    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setWide(Boolean wide) {
        isWide = wide;
    }

    public void setHasCoxswain(Boolean hasCoxswain) {
        this.hasCoxswain = hasCoxswain;
    }

    public void setMarine(Boolean marine) {
        isMarine = marine;
    }

    public void setDisabled(Boolean disabled) {
        isDisabled = disabled;
    }

}
