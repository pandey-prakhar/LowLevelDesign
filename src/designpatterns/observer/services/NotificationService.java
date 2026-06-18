package designpatterns.observer.services;

import designpatterns.observer.models.Order;
import designpatterns.observer.observers.OrderObserver;

public class NotificationService implements OrderObserver {

    @Override
    public void onOrderUpdate(Order order) {
        String message = null;
        switch (order.getStatus()) {
            case CONFIRMED: message = "Your order #" + order.getOrderId() + " is confirmed!"; break;
            case SHIPPED:   message = "Your order #" + order.getOrderId() + " has been shipped!"; break;
            case DELIVERED: message = "Your order #" + order.getOrderId() + " is delivered. Enjoy!"; break;
            case CANCELLED: message = "Your order #" + order.getOrderId() + " has been cancelled."; break;
            default:        break;
        }
        if (message != null) {
            System.out.println("  [NotificationService] SMS to " + order.getCustomerId()
                    + ": \"" + message + "\"");
        }
    }
}
