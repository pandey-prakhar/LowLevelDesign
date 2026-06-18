package designpatterns.observer.models;

public class Order {

    public enum Status {
        PLACED, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }

    private final String orderId;
    private final String customerId;
    private final double amount;
    private Status status;

    public Order(String orderId, String customerId, double amount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.status = Status.PLACED;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOrderId()     { return orderId; }
    public String getCustomerId()  { return customerId; }
    public double getAmount()      { return amount; }
    public Status getStatus()      { return status; }

    @Override
    public String toString() {
        return "Order[id=" + orderId + ", customer=" + customerId
                + ", amount=₹" + amount + ", status=" + status + "]";
    }
}
