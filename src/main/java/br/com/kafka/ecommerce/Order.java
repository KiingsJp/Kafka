package br.com.kafka.ecommerce;

import java.math.BigDecimal;

public class Order {

    String userId, orderId;
    BigDecimal amount;

    public Order(String userId, String orderId, BigDecimal amount) {
        this.userId = userId;
        this.orderId = orderId;
        this.amount = amount;
    }
}
