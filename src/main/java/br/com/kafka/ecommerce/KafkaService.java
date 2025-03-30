package br.com.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class KafkaService implements Closeable {

    private final KafkaConsumer<String, String > consumer;
    private final ConsumerFunction parse;

    KafkaService(String topico, ConsumerFunction parse, String groupId) {
        this.consumer = new KafkaConsumer<>(properties(groupId));
        this.parse = parse;
        consumer.subscribe(Collections.singletonList(topico));
    }

    void run() {
        while (true) {
            try {
                var records = consumer.poll(Duration.ofMillis(100));
                if(!records.isEmpty()) {
                    System.out.println("Records found: " + records.count());
                    records.forEach(parse::consume);
                }
            } catch (Exception e) {
                System.out.println("Error while consuming records: " + e.getMessage());
                break;
            }
        }
    }

    private static Properties properties(String groupId) {
        var properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        return properties;
    }

    @Override
    public void close() {
        consumer.close();
    }
}
