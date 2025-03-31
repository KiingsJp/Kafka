import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.Map;

public class FraudDetectorService {

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();
        try (
                var service = new KafkaService<>(
                        FraudDetectorService.class.getSimpleName(),
                        "ECOMMERCE_NEW_ORDER",
                        fraudService::parse,
                        Order.class,
                        Map.of()
                )
        ) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking for fraud");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(2000);

            var order = record.value();
            if (order.amount().compareTo(new BigDecimal(4000)) >= 0 ) {
                System.out.println("Fraud Detected!!!!");
                orderDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.userId(), order);
            } else {
                System.out.println("Approved!");
                orderDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.userId(), order);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Order processed");
    }

}
