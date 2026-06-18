package designpatterns.factory;

public class App {

    public static void main(String[] args) {

        // Client only knows about the Notification interface and the factory.
        // It has zero idea that EmailNotification, SMSNotification, PushNotification exist.

        Notification email = NotificationFactory.create(NotificationType.EMAIL, "prakhar@gmail.com");
        Notification sms   = NotificationFactory.create(NotificationType.SMS,   "+91-9876543210");
        Notification push  = NotificationFactory.create(NotificationType.PUSH,  "device-token-abc123");

        email.send("Your order has been confirmed!");
        sms.send("Your OTP is 4321");
        push.send("Your delivery is 2 stops away");
    }
}
