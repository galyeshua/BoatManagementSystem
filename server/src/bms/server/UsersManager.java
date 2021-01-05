package bms.server;

import bms.engine.LoginHandler;
import bms.module.Member;
import bms.module.MemberView;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public class UsersManager implements LoginHandler {
    private Map<Integer, Session> activeUsers;
    private static int sessionCounter = 0;

    public UsersManager(){
        activeUsers = new Hashtable<>();
    }

    private void validateUser(Session session) throws Member.AlreadyLoginException {
        if(session.sessionIsActive())
            throw new Member.AlreadyLoginException();
    }

    @Override
    public Integer createSessionForUser(MemberView user) throws Member.AlreadyLoginException {
        Session session;
        Integer sessionID = sessionCounter++;

        if ((session = getSession(user)) != null)
            validateUser(session);

        System.out.println("Server > new user login : " + user.getName());
        activeUsers.put(sessionID, new Session(user));

        return sessionID;
    }

    @Override
    public void endSessionForUser(Integer sessionID) {
        System.out.println("Server > user is logged out : " + activeUsers.get(sessionID).user.getName());
        activeUsers.remove(sessionID);
    }

    public Session getSession(Integer sessionID){
        return activeUsers.get(sessionID);
    }

    public Session getSession(MemberView user){
        for(Session session : activeUsers.values())
            if(session.user.equals(user))
                return session;
       return null;
    }

    public Collection<Session> getSessions(){
        return Collections.unmodifiableCollection(activeUsers.values());
    }



}
