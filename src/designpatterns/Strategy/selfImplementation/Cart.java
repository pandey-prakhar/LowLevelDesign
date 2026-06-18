package designpatterns.Strategy.selfImplementation;

public class Cart {
    private String item;
    private double price;
    private Payable paymentStategy;

    public Cart(String item, Double price){
        this.item = item;
        this.price = price;
    }

    public void setPaymentType(Payable paymentStategy){
        this.paymentStategy=paymentStategy;
    }

    public void setPayment(PaymentType type){
        this.paymentStategy=PaymentStrategyFactory.getStrategy(type);
    }

    public void checkOut(){
        if(paymentStategy==null){
            throw new IllegalStateException("Choose Payment Type.");
        }
        paymentStategy.pay(price);
    }


}
