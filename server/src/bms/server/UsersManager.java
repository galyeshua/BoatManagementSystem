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

    private boolean userExistsInActiveUsersMap(int userSerialNumber){
        return activeUsers.containsKey(userSerialNumber);
    }

    private void deleteExpiredSessionsFromMap(){
        for(Session s : activeUsers.values())
            if(!s.sessionIsActive())
                deleteUserSession(s.getUserSerialNumber());
    }

    public Session getSessionForUser(int userSerialNumber) throws Session.IsNotExistsException {
        Session s = activeUsers.get(userSerialNumber);
        if(s==null)
            throw new Session.IsNotExistsException();

        return s;
    }

    @Override
    public SessionView createUserSession(String email, String password) throws Member.InvalidUsernameOrPasswordException, Member.AlreadyLoginException {
        MemberView user = engine.getMember(email);

        if ((user == null) || (!user.checkPassword(password)))
            throw new Member.InvalidUsernameOrPasswordException();

        deleteExpiredSessionsFromMap(); // if user have old session it will remove here

        if (userExistsInActiveUsersMap(user.getSerialNumber()))
            throw new Member.AlreadyLoginException();

        Session createdSession = new Session(user.getSerialNumber(), sessionCounter++);
        activeUsers.put(user.getSerialNumber(), createdSession);

        return createdSession;
    }

    @Override
    public void deleteUserSession(int userSerialNumber) {
        if(userExistsInActiveUsersMap(userSerialNumber))
            activeUsers.remove(userSerialNumber);

        deleteExpiredSessionsFromMap();
    }

    public void updateSessionExpiredTime(int userSerialNumber, int sessionID) throws Session.IsNotExistsException, Session.IsExpiredException, Member.NotFoundException {
        Session s = getSessionForUser(userSerialNumber);

        if(engine.getMember(userSerialNumber)==null){
            deleteUserSession(userSerialNumber);
            throw new Session.IsNotExistsException();
        }

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
            for(Session s : activeUsers.values()){
                MemberView user = engine.getMember(s.getUserSerialNumber());
                if (user != null)
                    System.out.println("> Session number " + s.getSessionID() + " for user '" + user.getName() +
                            "' (" + user.getEmail() + "), SN:" + user.getSerialNumber() + " expired: "
                            + s.getExpiredTime() + " (Active: " + s.sessionIsActive() + ")");
                else
                    System.out.println("user " + s.getUserSerialNumber() + "is not exists in engine");
            }
        }
    }

}
