package servlets;

import bms.engine.Engine;
import bms.module.Boat;
import bms.module.Member;
import com.google.gson.Gson;
import notifications.Notification;
import notifications.managers.NotificationManager;
import utils.ContextUtils;
import utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static utils.SessionUtils.getCurrentUser;


@WebServlet(name="NotificationsServlet", urlPatterns = {"/user/notifications", "/manage/notifications"})
public class NotificationServlet extends HttpServlet {
    Gson gson = new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member currentUser = getCurrentUser(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        if(currentUser == null){
            responseJson.status = "error";
            responseJson.message = "Your Session is expired. Please login again";
        } else {
            NotificationManager notificationManager = ContextUtils.getNotificationManager(req);

            GlobalAndUserNotifications notifications = new GlobalAndUserNotifications();
            notifications.globalNotifications = notificationManager.getGlobalNotifications();
            if(req.getServletPath().startsWith("/user/"))
                notifications.userNotifications = notificationManager.getUserNotifications(currentUser.getSerialNumber());

            responseJson.message = notifications;
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getServletPath().startsWith("/user/"))
            return;

        NotificationManager notificationManager = ContextUtils.getNotificationManager(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        Notification jsonNotification = gson.fromJson(lines, Notification.class);

        try {
            notificationManager.addGlobalNotification(jsonNotification.getMessage());
        } catch (Notification.CannotBeEmpty cannotBeEmpty) {
            responseJson.status = "error";
            responseJson.message = "Message cannot be empty";
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getServletPath().startsWith("/user/"))
            return;

        NotificationManager notificationManager = ContextUtils.getNotificationManager(req);
        JsonUtils.ResponseJson responseJson = new JsonUtils.ResponseJson();

        BufferedReader reader = req.getReader();
        String lines = reader.lines().collect(Collectors.joining());

        Notification jsonNotification = gson.fromJson(lines, Notification.class);

        notificationManager.deleteGlobalNotification(jsonNotification.getId());

        try(PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(responseJson));
        }
    }

    public static class GlobalAndUserNotifications{
        public GlobalAndUserNotifications(){
            userNotifications = new ArrayList<>();
        }

        Collection<Notification> userNotifications;
        Collection<Notification> globalNotifications;
    }

}
