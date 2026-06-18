package designpatterns.observer.services;

import designpatterns.observer.models.Order;
import designpatterns.observer.observers.OrderObserver;

public class LoyaltyService implements OrderObserver {

    private static final double POINTS_PER_RUPEE = 0.1;

    @Override
    public void onOrderUpdate(Order order) {
        if (order.getStatus() == Order.Status.DELIVERED) {
            int points = (int) (order.getAmount() * POINTS_PER_RUPEE);
            System.out.println("  [LoyaltyService]   Added " + points
                    + " SuperCoins to account " + order.getCustomerId());
        } else if (order.getStatus() == Order.Status.CANCELLED) {
            System.out.println("  [LoyaltyService]   No SuperCoins awarded — order cancelled.");
        }
    }
}
