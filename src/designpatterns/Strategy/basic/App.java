package designpatterns.Strategy.basic;

public class App {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart("iPhone 15", 79999.00);

        // User picks UPI at checkout
        cart.setPaymentStrategy(new UPIPayment("prakhar@upi"));
        cart.checkout();

        // Same cart, user switches to Credit Card
        cart.setPaymentStrategy(new CreditCardPayment("4111111111111234", "Prakhar Pandey"));
        cart.checkout();

        // Same cart, user switches to Net Banking
        cart.setPaymentStrategy(new NetBankingPayment("HDFC", "ACC123456"));
        cart.checkout();
    }
}
