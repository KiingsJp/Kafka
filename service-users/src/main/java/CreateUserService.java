import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CreateUserService {

    private final Connection connection;

    CreateUserService() throws SQLException {
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
        var createUserService = new CreateUserService();
        try (
                var service = new KafkaService<>(
                        CreateUserService.class.getSimpleName(),
                        "ECOMMERCE_NEW_ORDER",
                        createUserService::parse,
                        Map.of()
                )
        ) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Message<Order>> record) {
        var message = record.value();
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking for new user");
        System.out.println(record.value());

        try {
//            deleteAllUsers();
            var order = message.payload();
            System.out.println(order.email());
            if(isNewUser(order.email())) {
                insertNewUser(order.email());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isNewUser(String email) throws SQLException {
        var exists = this.connection.prepareStatement("select * from Users where email = ?");
        exists.setString(1, email);
        var result = exists.executeQuery();
        System.out.println(!result.isBeforeFirst());
        return !result.isBeforeFirst();
    }

    private void insertNewUser(String email) throws SQLException {
        var insert = connection.prepareStatement("insert into Users(uuid, email) values (?, ?)");
        insert.setString(1, UUID.randomUUID().toString());
        insert.setString(2, email);
        insert.execute();
        System.out.println("Inserted new user: " + email);
    }

    private void deleteAllUsers() throws SQLException {
        var insert = connection.prepareStatement("delete from Users");
        insert.execute();
        System.out.println("Users deleted");
    }
}
