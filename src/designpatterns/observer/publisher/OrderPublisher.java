package designpatterns.observer.publisher;

import designpatterns.observer.models.Order;
import designpatterns.observer.observers.OrderObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Publisher / Subject — owns the observer list and drives notifications.
 *
 * No interface for the publisher here because there is only one kind of
 * order publisher in this system. Add a Subject interface only when you
 * have multiple publishers that clients swap between.
 */
public class OrderPublisher {

    private final List<OrderObserver> observers = new ArrayList<>();

    // ── subscription management ──────────────────────────────────────────

    public void subscribe(OrderObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(OrderObserver observer) {
        observers.remove(observer);
    }

    // ── state change → notify ────────────────────────────────────────────

    public void updateOrderStatus(Order order, Order.Status newStatus) {
        order.setStatus(newStatus);
        System.out.println("\n[Publisher] Order status changed → " + newStatus);
        notifyAll(order);
    }

    private void notifyAll(Order order) {
        for (OrderObserver observer : observers) {
            observer.onOrderUpdate(order);
        }
    }
}
