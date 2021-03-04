package utils;

import bms.module.Member;

import javax.servlet.http.HttpServletRequest;

public class AuthorizationUtils {
    public static boolean isManager(HttpServletRequest request){
        Member currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null)
            return false;

        return currentUser.getManager();
    }
}
