package br.com.kafka.ecommerce;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class NewOrderMain {

    public static void main(String[] args) {
        try (var producer = new KafkaProducer<String, String>(properties())) {
            var value = "9123, 8123, 7123";
            var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", value, value);
            producer.send(record, (data, exception) -> {
                if(exception != null) {
                    System.out.println("Erro: " + exception.getMessage());
                    return;
                }
                System.out.println("sucesso enviado - topico: " + data.topic() + " offset: " + data.offset() + " partition: " + data.partition() + " timestamp: " + data.timestamp() );
            });
        }
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

}
