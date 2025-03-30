package br.com.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDetectorService {
    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();

        try(var kafkaService = new KafkaService("ECOMMERCE_NEW_ORDER", fraudService::parse, FraudDetectorService.class.getSimpleName())) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("------------------------------");
        System.out.println("New Order data:");
        System.out.println("Key: " + record.key());
        System.out.println("Value: " + record.value());
        System.out.println("OffSet: " + record.offset());
        System.out.println("Partition: " + record.partition());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted while waiting for record");
        }
    }
}
