package br.com.kafka.ecommerce;

import java.util.UUID;

public class NewOrderMain {

    public static void main(String[] args) throws Exception {
        try(var kafkaDispatcher = new KafkaDispatcher()) {
            var key = UUID.randomUUID().toString();

            var value = key + ", 8123, 7123";
            kafkaDispatcher.send("ECOMMERCE_NEW_ORDER", key, value);

            var email = "kingsjp1207@gmail.com";
            kafkaDispatcher.send("ECOMMERCE_SEND_EMAIL", key, email);
        }
    }
}
