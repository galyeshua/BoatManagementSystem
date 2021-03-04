package utils;

import bms.engine.Engine;
import bms.module.ReservationView;
import notifications.Notification;
import notifications.managers.NotificationManager;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class NotificationUtils {

    public static void sendNotificationToReservationParticipants(HttpServletRequest req, int reservationId, String message) throws Notification.CannotBeEmpty {
        Engine engine = ContextUtils.getEngine(req);
        ReservationView reservation = engine.getReservation(reservationId);
        sendNotificationToReservationParticipants(req, reservation, message);
    }

    public static void sendNotificationToReservationParticipants(HttpServletRequest req, ReservationView reservation, String message) throws Notification.CannotBeEmpty {
        NotificationManager notificationManager = ContextUtils.getNotificationManager(req);

        List<Integer> participants = new ArrayList<>(reservation.getParticipants());
        if(!participants.contains(reservation.getOrderedMemberID()))
            participants.add(reservation.getOrderedMemberID());

        for(Integer userSerialNumber : participants)
            notificationManager.addUserNotification(userSerialNumber, message);
    }

}
