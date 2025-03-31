import java.math.BigDecimal;

public class Order {

    String orderId, email;
    BigDecimal amount;

    public Order(String orderId, BigDecimal amount, String email) {
        this.orderId = orderId;
        this.amount = amount;
        this.email = email;
    }
}
