package designpatterns.observer;

import designpatterns.observer.models.Order;
import designpatterns.observer.observers.OrderObserver;
import designpatterns.observer.publisher.OrderPublisher;
import designpatterns.observer.services.*;

public class Main {

    public static void main(String[] args) {

        // ── 1. Create publisher ──────────────────────────────────────────
        OrderPublisher publisher = new OrderPublisher();

        // ── 2. Create observers (services) ──────────────────────────────
        OrderObserver invoiceService      = new InvoiceService();
        OrderObserver inventoryService    = new InventoryService();
        OrderObserver notificationService = new NotificationService();
        OrderObserver loyaltyService      = new LoyaltyService();

        // ── 3. Subscribe all observers ───────────────────────────────────
        publisher.subscribe(invoiceService);
        publisher.subscribe(inventoryService);
        publisher.subscribe(notificationService);
        publisher.subscribe(loyaltyService);

        // ── 4. Place an order ────────────────────────────────────────────
        Order order1 = new Order("ORD-001", "CUST-42", 1500.0);
        System.out.println("Order placed: " + order1);

        // ── 5. Walk through the order lifecycle ──────────────────────────
        publisher.updateOrderStatus(order1, Order.Status.CONFIRMED);
        publisher.updateOrderStatus(order1, Order.Status.SHIPPED);
        publisher.updateOrderStatus(order1, Order.Status.DELIVERED);

        // ── 6. Demo: unsubscribe one observer mid-flight ─────────────────
        System.out.println("\n--- Loyalty service unsubscribed ---");
        publisher.unsubscribe(loyaltyService);

        Order order2 = new Order("ORD-002", "CUST-99", 800.0);
        System.out.println("\nOrder placed: " + order2);
        publisher.updateOrderStatus(order2, Order.Status.CONFIRMED);
        publisher.updateOrderStatus(order2, Order.Status.CANCELLED);
    }
}
