package bms.engine;

import bms.module.Member;
import bms.module.MemberView;

public interface LoginHandler {

    Integer createSessionForUser(MemberView user) throws Member.AlreadyLoginException;

    void endSessionForUser(Integer sessionID);
}
