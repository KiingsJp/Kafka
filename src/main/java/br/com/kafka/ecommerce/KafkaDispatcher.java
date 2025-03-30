package br.com.kafka.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;

public class KafkaDispatcher implements Closeable {

    private final KafkaProducer<String, String> producer;

    KafkaDispatcher() {
        this.producer = new KafkaProducer<>(properties());
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    void send(String topic, String key, String value) throws Exception {
        Callback callback = (data, exception) -> {
            if(exception != null) {
                System.out.println("Erro: " + exception.getMessage());
                return;
            }
            System.out.println("sucesso enviado - topico: " + data.topic() + " offset: " + data.offset() + " partition: " + data.partition() + " timestamp: " + data.timestamp() );
        };
        var record = new ProducerRecord<>(topic, key, value);
        producer.send(record, callback).get();
    }

    @Override
    public void close() {
        producer.close();
    }
}
