package designpatterns.Strategy.withFactory;

public class CreditCardPayment implements PaymentStrategy {

    @Override
    public void pay(double amount, String cardNumber) {
        System.out.println("Paid Rs." + amount + " via Credit Card ending in "
                + cardNumber.substring(cardNumber.length() - 4));
    }
}
