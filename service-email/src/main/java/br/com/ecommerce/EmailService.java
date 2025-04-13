package br.com.ecommerce;

import br.com.ecommerce.consumer.ConsumerService;
import br.com.ecommerce.consumer.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService implements ConsumerService<String> {

    public static void main(String[] args){
        new ServiceRunner<>(EmailService::new).start(5);
    }

    @Override
    public String getConsumerGroup() {
        return EmailService.class.getSimpleName();
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_SEND_EMAIL";
    }

    @Override
    public void parse(ConsumerRecord<String, Message<String>> record) {
        System.out.println("------------------------------------------");
        System.out.println("Send email");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Email sent");
    }
}
