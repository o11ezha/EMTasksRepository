package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int BUFFER_SIZE = 10;
    private static final int ITEM_COUNT = 20;

    public static void main(String[] args) {
        RingBuffer<Integer> ringBuffer = new RingBuffer<>(BUFFER_SIZE);

        Runnable producer = new Producer(ringBuffer, ITEM_COUNT);
        Runnable consumer = new Consumer(ringBuffer, ITEM_COUNT);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(producer);
        executorService.submit(consumer);

        executorService.shutdown();
    }
}