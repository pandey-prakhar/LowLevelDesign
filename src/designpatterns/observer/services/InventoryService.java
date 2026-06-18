package designpatterns.observer.services;

import designpatterns.observer.models.Order;
import designpatterns.observer.observers.OrderObserver;

public class InventoryService implements OrderObserver {
    @Override
    public void onOrderUpdate(Order order) {
        if (order.getStatus() == Order.Status.CONFIRMED) {
            System.out.println("  [InventoryService] Reducing stock for order "
                    + order.getOrderId());
        } else if (order.getStatus() == Order.Status.CANCELLED) {
            System.out.println("  [InventoryService] Restoring stock for cancelled order "
                    + order.getOrderId());
        }
    }
}
