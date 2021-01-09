package bms.engine;

import bms.module.Member;
import bms.module.MemberView;
import bms.network.SessionView;

public interface LoginHandler {

    SessionView createUserSession(String email, String password) throws Member.InvalidUsernameOrPasswordException, Member.AlreadyLoginException;

    void deleteUserSession(MemberView user);

}
