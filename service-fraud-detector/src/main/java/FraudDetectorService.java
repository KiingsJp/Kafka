import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var fraudService = new FraudDetectorService();
        try (
                var service = new KafkaService<>(
                        FraudDetectorService.class.getSimpleName(),
                        "ECOMMERCE_NEW_ORDER",
                        fraudService::parse,
                        Map.of()
                )
        ) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Message<Order>> record) {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking for fraud");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(2000);

            var order = record.value().payload();
            var correlationId = record.value().id().continueWith(FraudDetectorService.class.getSimpleName());
            if (order.amount().compareTo(new BigDecimal(4000)) >= 0 ) {
                System.out.println("Fraud Detected!!!!");
                orderDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.email(), order, correlationId);
            } else {
                System.out.println("Approved!");
                orderDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.email(), order, correlationId);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Order processed");
    }

}
