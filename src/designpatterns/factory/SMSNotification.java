package designpatterns.factory;

public class SMSNotification implements Notification {

    private String phoneNumber;

    public SMSNotification(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void send(String message) {
        System.out.println("Sending SMS to " + phoneNumber + " : " + message);
    }
}
