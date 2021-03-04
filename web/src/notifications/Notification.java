package notifications;

import java.time.LocalDateTime;

public class Notification {
    private static int counter = 0;

    private int id;
    private String message;
    private LocalDateTime creationTime;

    public Notification(String message) throws CannotBeEmpty {
        this.setId(counter++);
        this.setMessage(message);
        this.creationTime = LocalDateTime.now();
    }

    private void setId(int id) {
        this.id = id;
    }

    private void setMessage(String message) throws CannotBeEmpty {
        if(message.trim().length() == 0)
            throw new CannotBeEmpty();
        this.message = message.trim();
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public static class CannotBeEmpty extends Exception {}

}
