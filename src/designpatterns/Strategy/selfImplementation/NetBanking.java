package designpatterns.Strategy.selfImplementation;

public class NetBanking implements Payable{
    private String bank;
    private long accNum;

    public NetBanking(String bank, long accNum){
        this.bank=bank;
        this.accNum=accNum;
    }

    public void pay(double amount){
        System.out.println("Paid Rs." + amount + " via Net Banking through " + bank);
    }
}
