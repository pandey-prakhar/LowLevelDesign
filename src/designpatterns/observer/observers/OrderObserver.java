package designpatterns.observer.observers;

import designpatterns.observer.models.Order;

/**
 * Observer interface — every subscriber must implement this.
 * Called the "Observer" or "Listener" in GoF.
 */
public interface OrderObserver {
    void onOrderUpdate(Order order);
}
