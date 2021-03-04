package utils;

import bms.engine.Engine;
import bms.module.Member;
import bms.network.Session;
import notifications.managers.NotificationManager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionUtils {
    public static Member getUser (HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        Object sessionAttribute = null;
        if (session != null){
            session.setMaxInactiveInterval(600); // seconds (600 sec - 10 min)
            sessionAttribute = session.getAttribute("userObject");
        }

        return sessionAttribute != null ? (Member)sessionAttribute : null;
    }

    public static void setUser(HttpServletRequest request, HttpServletResponse resp, String email, String password)
            throws Member.InvalidUsernameOrPasswordException {
        Engine engine = (Engine)request.getServletContext().getAttribute("engine");
        Member user = (Member)engine.getMember(email);

        if ((user == null) || (!user.checkPassword(password)))
            throw new Member.InvalidUsernameOrPasswordException();

        if(isSessionActive(request))
            clearSession(request);

        // to do: user is active

        HttpSession session = request.getSession(true);
        resp.addCookie(new Cookie("userSerialNumber", String.valueOf(user.getSerialNumber())));
        session.setAttribute("userObject", user);
        //session.setAttribute("userNotifications", new NotificationManager());
    }

    public static boolean isSessionActive(HttpServletRequest request){
        return getUser(request) != null;
    }

//    public static boolean isUserLoggedIn(Member user){
//    }

    public static Member getCurrentUser(HttpServletRequest request){
        return getUser(request);
    }

    public static void clearSession (HttpServletRequest request) {

        request.getSession().invalidate();
        for(Cookie cookie: request.getCookies()){
            if(cookie.getName().equals("userSerialNumber"))
                cookie.setMaxAge(0);
        }
    }

    public static NotificationManager getUserNotificationManager (HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        Object sessionAttribute = null;
        if (session != null){
            session.setMaxInactiveInterval(30000); // seconds (300 sec - 5 min)
            sessionAttribute = session.getAttribute("userNotifications");
        }

        return sessionAttribute != null ? (NotificationManager)sessionAttribute : null;
    }

    //public static class IsAlreadyExistsException extends Exception{}
    //public static class IsNotExistsException extends Exception{}
}
