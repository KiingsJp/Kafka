package br.com.ecommerce.consumer;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ServiceProvider<T> implements Callable<Void> {

    ServiceFactory<T> factory;

    ServiceProvider(ServiceFactory<T> serviceFactory) {
        this.factory = serviceFactory;
    }

    public Void call() throws ExecutionException, InterruptedException {
        var myService = factory.crate();
        try (
                var service = new KafkaService<>(
                        myService.getConsumerGroup(),
                        myService.getTopic(),
                        myService::parse,
                        Map.of()
                )
        ) {
            service.run();
        }

        return null;
    }
}
