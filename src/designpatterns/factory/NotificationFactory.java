package designpatterns.factory;

public class NotificationFactory {

    public static Notification create(NotificationType type, String target) {
        switch (type) {
            case EMAIL: return new EmailNotification(target);
            case SMS:   return new SMSNotification(target);
            case PUSH:  return new PushNotification(target);
            default: throw new IllegalArgumentException("Unknown notification type: " + type);
        }
    }
}
