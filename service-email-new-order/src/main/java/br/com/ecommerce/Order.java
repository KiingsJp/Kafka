package br.com.ecommerce;

import java.math.BigDecimal;

public record Order(String orderId, BigDecimal amount, String email) {
    @Override
    public String toString() {
        return "br.com.ecommerce.Order{" +
                "orderId='" + orderId + '\'' +
                ", amount=" + amount +
                ", email='" + email + '\'' +
                '}';
    }
}
