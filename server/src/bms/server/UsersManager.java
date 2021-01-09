package bms.server;

import bms.engine.Engine;
import bms.engine.LoginHandler;
import bms.module.Member;
import bms.module.MemberView;
import bms.network.Session;
import bms.network.SessionView;

import java.util.Hashtable;
import java.util.Map;

public class UsersManager implements LoginHandler {
    Engine engine;
    private Map<Integer, Session> activeUsers; // serialnumber, session
    private static int sessionCounter = 0;


    public UsersManager(Engine engine){
        this.engine = engine;
        activeUsers = new Hashtable<>();
    }

    private void deleteExpiredSessions(){
        for(Session s : activeUsers.values())
            if(!s.sessionIsActive())
                activeUsers.remove(s.getUser().getSerialNumber());
    }

    public boolean userIsLoggedIn(MemberView user){
        return activeUsers.containsKey(user.getSerialNumber());
    }

    @Override
    public SessionView createUserSession(String email, String password) throws Member.InvalidUsernameOrPasswordException, Member.AlreadyLoginException {
        MemberView user = engine.getMember(email);

        if ((user == null) || (!user.checkPassword(password)))
            throw new Member.InvalidUsernameOrPasswordException();

        deleteExpiredSessions(); // if user have old session it will remove here

        if (userIsLoggedIn(user))
            throw new Member.AlreadyLoginException();

        activeUsers.put(user.getSerialNumber(), new Session(user, sessionCounter++));

        return new Session(activeUsers.get(user.getSerialNumber()));
    }

    @Override
    public void deleteUserSession(MemberView user) {
        if(userIsLoggedIn(user))
            activeUsers.remove(user.getSerialNumber());

        deleteExpiredSessions();
    }

    public Session getSession(MemberView user) throws Session.IsNotExistsException {
        Session s = activeUsers.get(user.getSerialNumber());
        if(s==null)
            throw new Session.IsNotExistsException();
        return s;
    }

    public void updateSessionExpiredTime(MemberView user, int sessionID) throws Session.IsNotExistsException, Session.IsExpiredException {
        Session s = getSession(user);

        boolean sessionIdIsNotEqual = s.getSessionID() != sessionID;
        boolean sessionIsExpired = !s.sessionIsActive();

        if (sessionIdIsNotEqual || sessionIsExpired)
            throw new Session.IsExpiredException();
        s.updateExpiredTime();
    }

    public void printMembers(){
        if(activeUsers.isEmpty())
            System.out.println("(No connected users)");
        else{
            System.out.println("List of connected users:");
            for(Session s : activeUsers.values())
                System.out.println("> Session number " + s.getSessionID() + " for user '" + s.getUser().getName() +
                        "' (" + s.getUser().getEmail() + "), SN:" + s.getUser().getSerialNumber() + " expired: "
                + s.getExpiredTime() + " (Active: " + s.sessionIsActive() + ")");
        }
    }

}
