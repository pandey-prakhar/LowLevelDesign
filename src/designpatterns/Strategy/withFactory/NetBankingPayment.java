package designpatterns.Strategy.withFactory;

public class NetBankingPayment implements PaymentStrategy {

    @Override
    public void pay(double amount, String bankName) {
        System.out.println("Paid Rs." + amount + " via Net Banking through " + bankName);
    }
}
