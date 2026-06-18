package designpatterns.factory;

public class PushNotification implements Notification {

    private String deviceToken;

    public PushNotification(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public void send(String message) {
        System.out.println("Sending PUSH to device [" + deviceToken + "] : " + message);
    }
}
