package br.com.ecommerce;

import java.util.UUID;

public class CorrelationId {
    private final String  id;

    public CorrelationId(String title) {
        id = title + "("  + UUID.randomUUID() + ")";
    }

    @Override
    public String toString() {
        return "br.com.ecommerce.CorrelationId{" +
                "id='" + id + '\'' +
                '}';
    }

    public CorrelationId continueWith(String title){
        return new CorrelationId(id + "-" + title);
    }
}
