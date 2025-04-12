import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

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

    public static void main(String[] args) throws SQLException {
        var batchSendMessageService = new BatchSendMessageService();
        try (
                var service = new KafkaService<>(
                        BatchSendMessageService.class.getSimpleName(),
                        "SEND_MESSAGE_TO_ALL_USERS",
                        batchSendMessageService::parse,
                        String.class,
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
        try {
            for (User user : getAllUser()){
                userDispatcher.send(message.payload(), user.uuid(), user);
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
