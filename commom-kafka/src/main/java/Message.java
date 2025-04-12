public record Message<T>(CorrelationId id, T payload) {

}
