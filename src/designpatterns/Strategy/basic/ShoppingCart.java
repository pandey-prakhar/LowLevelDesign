package designpatterns.Strategy.basic;

public class ShoppingCart {
    private String itemName;
    private double totalAmount;
    private PaymentStrategy paymentStrategy;

    public ShoppingCart(String itemName, double totalAmount) {
        this.itemName = itemName;
        this.totalAmount = totalAmount;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void checkout() {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Please select a payment method before checkout.");
        }
        System.out.println("Checking out: " + itemName);
        paymentStrategy.pay(totalAmount);
    }
}
