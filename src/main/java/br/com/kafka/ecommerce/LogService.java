package br.com.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;

public class LogService {
    public static void main(String[] args) {

        try(var consumer = new KafkaConsumer<String, String>(properties())) {
            consumer.subscribe(Pattern.compile("ECOMMERCE.*"));
            while (true) {
                try {
                    var records = consumer.poll(Duration.ofMillis(100));
                    if(!records.isEmpty()) {
                        System.out.println("Records found: " + records.count());
                        records.forEach(record -> {
                            System.out.println("------------------------------");
                            System.out.println("LOG data:");
                            System.out.println("Topic: " + record.topic());
                            System.out.println("Key: " + record.key());
                            System.out.println("Value: " + record.value());
                            System.out.println("OffSet: " + record.offset());
                            System.out.println("Partition: " + record.partition());
                        });
                    }
                } catch (Exception e) {
                    System.out.println("Error while consuming records: " + e.getMessage());
                    break;
                }
            }
        }
    }

    public static Properties properties() {
        var properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, LogService.class.getSimpleName());
        return properties;
    }
}
