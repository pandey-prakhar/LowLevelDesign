package designpatterns.Strategy.withFactory;

public interface PaymentStrategy {
    void pay(double amount, String paymentId);
}
