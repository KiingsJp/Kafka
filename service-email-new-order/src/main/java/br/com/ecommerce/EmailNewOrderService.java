package br.com.ecommerce;

import br.com.ecommerce.consumer.KafkaService;
import br.com.ecommerce.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EmailNewOrderService {

    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var emailNewOrderServiceService = new EmailNewOrderService();
        try (
                var service = new KafkaService<>(
                        EmailNewOrderService.class.getSimpleName(),
                        "ECOMMERCE_NEW_ORDER",
                        emailNewOrderServiceService::parse,
                        Map.of()
                )
        ) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, preparing email");
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

        var email = record.key();
        var id = record.value().id().continueWith(EmailNewOrderService.class.getSimpleName());
        var emailCode = "Thank you for your order! We are processing your order!";
        emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, emailCode, id);
    }
}
