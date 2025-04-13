package br.com.ecommerce;

import br.com.ecommerce.consumer.ConsumerService;
import br.com.ecommerce.consumer.KafkaService;
import br.com.ecommerce.consumer.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ReadingReportService implements ConsumerService<User> {

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    public static void main(String[] args) {
        new ServiceRunner<>(ReadingReportService::new).start(5);

    }

    public void parse(ConsumerRecord<String, Message<User>> record) throws IOException {
        System.out.println("------------------------------------------");
        System.out.println("Processing report for " + record.value());
        var user = record.value().payload();
        var target = new File(user.getReportPath());

        IO.copyTo(SOURCE, target);
        IO.append(target, "\nCreated for br.com.ecommerce.User: " + user.uuid());

        System.out.println("File created " + target.getAbsolutePath());
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_USER_GENERATE_READING_REPORT";
    }

    @Override
    public String getConsumerGroup() {
        return ReadingReportService.class.getSimpleName();
    }
}
