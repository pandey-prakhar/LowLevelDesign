package designpatterns.Strategy.withFactory;

public class App {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart("iPhone 15", 79999.00);

        // Same strategy type, different UPI IDs — works cleanly now
        cart.setPaymentType(PaymentType.UPI);
        cart.checkout("prakhar@hdfc");
        cart.checkout("prakhar@oksbi");

        // Different user, different card — same strategy instance, different data
        cart.setPaymentType(PaymentType.CREDIT_CARD);
        cart.checkout("4111111111111234");
        cart.checkout("5500005555555559");

        cart.setPaymentType(PaymentType.NET_BANKING);
        cart.checkout("HDFC");
    }
}
