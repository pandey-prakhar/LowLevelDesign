package designpatterns.observer.services;

import designpatterns.observer.models.Order;
import designpatterns.observer.observers.OrderObserver;

public class InvoiceService implements OrderObserver {

    @Override
    public void onOrderUpdate(Order order) {
        if (order.getStatus() == Order.Status.CONFIRMED) {
            System.out.println("  [InvoiceService]  Generating invoice for order "
                    + order.getOrderId() + " — ₹" + order.getAmount());
        }
    }
}
