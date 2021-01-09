package bms.network;

import bms.module.MemberView;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Session implements SessionView, Serializable {
    private MemberView user;
    private LocalDateTime expiredTime;
    private int sessionTimeInMinutes = 5;
    private int sessionID;

    public Session(MemberView user, int sessionID) {
        this.sessionID = sessionID;
        this.user = user;
        updateExpiredTime();
    }

    public Session(SessionView other) {
        this.sessionID = other.getSessionID();
        this.user = other.getUser();
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
    public MemberView getUser() {
        return user;
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
