package org.example;

import org.example.job.MapperImpl;
import org.example.job.ReducerImpl;
import org.example.node.Coordinator;
import org.example.node.Worker;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<String> files = Arrays.asList("file1.txt", "file2.txt");
        int numberOfWorkers = 4;
        int reduceCount = 2;

        Coordinator coordinator = new Coordinator(files, reduceCount);
        FileManager fileManager = new FileManager();
        MapperImpl mapper = new MapperImpl();
        ReducerImpl reducer = new ReducerImpl();

        CountDownLatch mapLatch = new CountDownLatch(files.size());
        CountDownLatch reduceLatch = new CountDownLatch(reduceCount);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfWorkers);

        for (int i = 0; i < numberOfWorkers; i++) {
            executorService.execute(new Worker(coordinator, mapper, reducer, fileManager, mapLatch, reduceLatch));
        }

        mapLatch.await();
        reduceLatch.await();

        executorService.shutdown();
        fileManager.deleteTempFiles();
    }
}