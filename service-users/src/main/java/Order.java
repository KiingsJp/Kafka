import java.math.BigDecimal;

public record Order(String userId, String orderId, BigDecimal amount, String email) {

}
