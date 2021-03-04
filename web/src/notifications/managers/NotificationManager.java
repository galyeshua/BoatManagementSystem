package notifications.managers;

import notifications.Notification;

import java.util.*;
import java.util.stream.Collectors;

public class NotificationManager {
    private List<Notification> globalNotifications;
    private Map<Integer, List<Notification>> userNotifications;


    public NotificationManager(){
        this.globalNotifications = new ArrayList<>();
        this.userNotifications = new HashMap<>();
    }


    public Collection<Notification> getGlobalNotifications(){
        return Collections.unmodifiableCollection(globalNotifications);
    }

    public void addGlobalNotification(String message) throws Notification.CannotBeEmpty {
        this.globalNotifications.add(new Notification(message));
    }

    public void deleteGlobalNotification(int id){
        List<Notification> notificationsToDelete = this.globalNotifications.stream().filter(notification ->
                notification.getId()==id).collect(Collectors.toList()
        );
        for(Notification n : notificationsToDelete)
            this.globalNotifications.remove(n);
    }


    public Collection<Notification> getUserNotifications(int userSerialNumber){

        if(this.userNotifications.get(userSerialNumber)==null)
            return Collections.unmodifiableCollection(new ArrayList<>());

        Collection<Notification> userNotifications = new ArrayList<>(this.userNotifications.get(userSerialNumber));
        this.userNotifications.get(userSerialNumber).clear();
        this.userNotifications.remove(userSerialNumber);

        return Collections.unmodifiableCollection(userNotifications);
    }

    public void addUserNotification(int userSerialNumber, String message) throws Notification.CannotBeEmpty {
        if(this.userNotifications.get(userSerialNumber) == null)
            this.userNotifications.put(userSerialNumber, new ArrayList<Notification>());

        this.userNotifications.get(userSerialNumber).add(new Notification(message));
    }











//
//
//    public void addNotification(String message){
//        notifications.add(new Notification(message));
//    }
//
//    public void deleteNotification(int id){
//        List<Notification> notificationsToDelete = notifications.stream().filter(notification ->
//                notification.getId()==id).collect(Collectors.toList()
//        );
//        for(Notification n : notificationsToDelete)
//            notifications.remove(n);
//    }
////
//    public void deleteAllNotifications(){
//        notifications.clear();
//    }
//
}
