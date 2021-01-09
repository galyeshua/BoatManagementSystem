package bms.network;

import bms.module.MemberView;

public interface SessionView  {
    MemberView getUser();

    int getSessionID();
}
