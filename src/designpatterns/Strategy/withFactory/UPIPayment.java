package designpatterns.Strategy.withFactory;

public class UPIPayment implements PaymentStrategy {

    @Override
    public void pay(double amount, String upiId) {
        System.out.println("Paid Rs." + amount + " via UPI to " + upiId);
    }
}
