package br.com.ecommerce;

import br.com.ecommerce.consumer.KafkaService;
import br.com.ecommerce.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BatchSendMessageService {

    private final Connection connection;

    private final KafkaDispatcher<User> userDispatcher = new KafkaDispatcher<>();

    BatchSendMessageService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        String createQuery = "create table Users(uuid varchar(200) primary key, email varchar(200))";

        this.connection = DriverManager.getConnection(url);
        try {
            this.connection.createStatement().execute(createQuery);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) throws SQLException, ExecutionException, InterruptedException {
        var batchSendMessageService = new BatchSendMessageService();
        try (
                var service = new KafkaService<>(
                        BatchSendMessageService.class.getSimpleName(),
                        "ECOMMERCE_SEND_MESSAGE_TO_ALL_USERS",
                        batchSendMessageService::parse,
                        Map.of()
                )
        ) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Message<String>> record) {
        var message = record.value();
        System.out.println("------------------------------------------");
        System.out.println("Processing new batch");
        System.out.println("Topic: " + message.payload());
//        if(true) throw new RuntimeException("meu erro aquii");
        try {
            for (User user : getAllUser()){
                var correlationId = record.value().id().continueWith(BatchSendMessageService.class.getSimpleName());
                userDispatcher.sendAsync(message.payload(), user.uuid(), user, correlationId);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private ArrayList<User> getAllUser() throws SQLException {
        var results = connection.prepareStatement("select uuid from Users").executeQuery();
        var users = new ArrayList<User>();
        while (results.next()) {
            var user = new User(results.getString(1));
            users.add(user);
        }
        return users;
    }
}
