package designpatterns.factory;

public class EmailNotification implements Notification {

    private String toEmail;

    public EmailNotification(String toEmail) {
        this.toEmail = toEmail;
    }

    @Override
    public void send(String message) {
        System.out.println("Sending EMAIL to " + toEmail + " : " + message);
    }
}
