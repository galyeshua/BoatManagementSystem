package bms.network;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Session implements SessionView, Serializable {
    private LocalDateTime expiredTime;
    private int sessionTimeInMinutes = 5;
    private int userSerialNumber;
    private int sessionID;

    public Session(int userSerialNumber, int sessionID) {
        this.sessionID = sessionID;
        this.userSerialNumber = userSerialNumber;
        updateExpiredTime();
    }

    public Session(SessionView other) {
        this.sessionID = other.getSessionID();
        this.userSerialNumber = other.getUserSerialNumber();
    }

    public void setSessionTimeInMinutes(int sessionTimeInMinutes) {
        this.sessionTimeInMinutes = sessionTimeInMinutes;
    }

    public void updateExpiredTime(){
        this.expiredTime = LocalDateTime.now().plusMinutes(sessionTimeInMinutes);
        //this.expiredTime = LocalDateTime.now().plusSeconds(10);
    }

    public boolean sessionIsActive(){
        return LocalDateTime.now().isBefore(expiredTime);
    }

    @Override
    public int getUserSerialNumber() {
        return userSerialNumber;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public int getSessionTimeInMinutes() {
        return sessionTimeInMinutes;
    }

    @Override
    public int getSessionID() {
        return sessionID;
    }

    public static class IsExpiredException extends RuntimeException{}
    public static class IsNotExistsException extends RuntimeException{}

}
