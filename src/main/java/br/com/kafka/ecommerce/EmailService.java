package br.com.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {
    public static void main(String[] args) {
        var emailService = new EmailService();

        try(var kafkaService = new KafkaService("ECOMMERCE_SEND_EMAIL", emailService::parse, EmailService.class.getSimpleName())) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("------------------------------");
        System.out.println("Email data:");
        System.out.println("Key: " + record.key());
        System.out.println("Value: " + record.value());
        System.out.println("OffSet: " + record.offset());
        System.out.println("Partition: " + record.partition());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted while waiting for record");
        }
    }
}
