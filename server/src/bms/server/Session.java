package bms.server;

import bms.module.MemberView;
import java.time.LocalDateTime;

public class Session {
    MemberView user;
    LocalDateTime expired;
    int sessionTimeInMinutes = 5;

    public Session(MemberView user) {
        this.user = user;
        updateExpired();
    }

    public void setSessionTimeInMinutes(int sessionTimeInMinutes) {
        this.sessionTimeInMinutes = sessionTimeInMinutes;
    }

    public void updateExpired(){
        this.expired = LocalDateTime.now().plusMinutes(sessionTimeInMinutes);
    }

    public boolean sessionIsActive(){
        return LocalDateTime.now().isBefore(expired);
    }
}
