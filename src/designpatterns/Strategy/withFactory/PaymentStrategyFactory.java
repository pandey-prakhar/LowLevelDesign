package designpatterns.Strategy.withFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class PaymentStrategyFactory {

    // Stores lambdas (recipes), not objects — lazy: nothing is created at startup
    private static final Map<PaymentType, Supplier<PaymentStrategy>> CREATORS = new EnumMap<>(PaymentType.class);

    static {
        CREATORS.put(PaymentType.UPI,         UPIPayment::new);
        CREATORS.put(PaymentType.CREDIT_CARD,  CreditCardPayment::new);
        CREATORS.put(PaymentType.NET_BANKING,  NetBankingPayment::new);
    }

    public static PaymentStrategy getStrategy(PaymentType type) {
        Supplier<PaymentStrategy> creator = CREATORS.get(type);
        if (creator == null) {
            throw new IllegalArgumentException("No strategy registered for: " + type);
        }
        return creator.get();
    }
}
