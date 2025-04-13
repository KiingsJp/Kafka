package br.com.ecommerce.consumer;

import java.util.concurrent.Executors;

public class ServiceRunner<T> {

    private final ServiceProvider<T> provider;

    public ServiceRunner(ServiceFactory<T> factory) {
        this.provider = new ServiceProvider<>(factory);
    }

    public void start(int countThreads) {
        var pool = Executors.newFixedThreadPool(countThreads);
        for (int i = 0; i < countThreads; i++) {
            pool.submit(provider);
        }
    }
}
