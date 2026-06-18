package designpatterns.Strategy.selfImplementation;

public class UpiPayment implements Payable{
    private String upiId;

    public UpiPayment(String upiId){
        this.upiId=upiId;
    }

    public void pay(double amount){
        System.out.println("Paid Rs." + amount + " via UPI to " + upiId);
    }
}
