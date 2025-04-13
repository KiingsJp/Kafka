package br.com.ecommerce;

public record Message<T>(CorrelationId id, T payload) {
}
