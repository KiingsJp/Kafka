import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public interface ConsumerFunction<T> {
    void consume(ConsumerRecord<String, T> record) throws IOException;
}
