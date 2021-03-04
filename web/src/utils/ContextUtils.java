package utils;

import bms.engine.Engine;
import bms.module.Member;
import notifications.managers.NotificationManager;

import javax.servlet.http.HttpServletRequest;

public class ContextUtils {
    public static Engine getEngine(HttpServletRequest request){
        Engine engine = (Engine)request.getServletContext().getAttribute("engine");

        Member currentUser = SessionUtils.getCurrentUser(request);
        if(currentUser != null)
            engine.setCurrentUser(currentUser.getSerialNumber());

        return engine;
    }

    public static NotificationManager getNotificationManager(HttpServletRequest request) {
        NotificationManager notificationManager = (NotificationManager)request.getServletContext().getAttribute("notificationManager");
        return notificationManager;
    }
}
