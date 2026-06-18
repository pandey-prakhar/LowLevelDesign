package designpatterns.Strategy.selfImplementation;

public class CardPayment implements Payable{
    private long cardNumber;

    public CardPayment(long cardNumber){
        this.cardNumber=cardNumber;
    }

    public void pay(double amount){
        System.out.println("Paid Rs." + amount + " via Card ending in " + cardNumber);
    }
}
