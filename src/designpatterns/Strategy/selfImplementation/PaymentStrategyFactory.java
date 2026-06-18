package designpatterns.Strategy.selfImplementation;

import java.util.HashMap;
import java.util.Map;

public class PaymentStrategyFactory {
    private static Map<PaymentType, Payable> strategies = new HashMap<>();
    static {
        strategies.put(PaymentType.UPI, new UpiPayment("prakhar@hdfc"));
        strategies.put(PaymentType.Card, new CardPayment(892383499433L));
        strategies.put(PaymentType.NetBanking, new NetBanking("HDFC", 3834983));
    }

    public static Payable getStrategy(PaymentType paymentType){
        Payable strategy =  strategies.get(paymentType);
        if(strategy==null) throw new IllegalStateException("No strategy registered for: " + paymentType);
        return strategy;
    }
}
