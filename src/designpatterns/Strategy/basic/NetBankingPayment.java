package designpatterns.Strategy.basic;

public class NetBankingPayment implements PaymentStrategy {
    private String bankName;
    private String accountNumber;

    public NetBankingPayment(String bankName, String accountNumber) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid Rs." + amount + " via Net Banking through " + bankName);
    }
}
