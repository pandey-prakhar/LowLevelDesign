package designpatterns.Strategy.withFactory;

public class ShoppingCart {
    private String itemName;
    private double totalAmount;
    private PaymentStrategy paymentStrategy;

    public ShoppingCart(String itemName, double totalAmount) {
        this.itemName = itemName;
        this.totalAmount = totalAmount;
    }

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void setPaymentType(PaymentType type) {
        this.paymentStrategy = PaymentStrategyFactory.getStrategy(type);
    }

    public void checkout(String paymentId) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Please select a payment method before checkout.");
        }
        System.out.println("Checking out: " + itemName);
        paymentStrategy.pay(totalAmount, paymentId);
    }
}
