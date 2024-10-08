package org.example.node;

import org.example.CustomSet;
import org.example.FileManager;
import org.example.job.MapperImpl;
import org.example.job.ReducerImpl;
import org.example.task.MapTask;
import org.example.task.ReduceTask;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {
    private final Coordinator coordinator;
    private final FileManager fileManager;
    private final MapperImpl mapper;
    private final ReducerImpl reducer;
    private final CountDownLatch mapLatch;
    private final CountDownLatch reduceLatch;

    public Worker(Coordinator coordinator, MapperImpl mapper, ReducerImpl reducer, FileManager fileManager, CountDownLatch mapLatch, CountDownLatch reduceLatch) {
        this.coordinator = coordinator;
        this.mapper = mapper;
        this.reducer = reducer;
        this.fileManager = fileManager;
        this.mapLatch = mapLatch;
        this.reduceLatch = reduceLatch;
    }

    @Override
    public void run() {
        try {
            while (true) {
                MapTask mapTask = coordinator.getMapTask();
                if (mapTask != null) {
                    doMapTask(mapTask);
                    mapLatch.countDown();
                    continue;
                }

                if (coordinator.allMapTasksCompleted()) {
                    ReduceTask reduceTask = coordinator.getReduceTask();
                    if (reduceTask != null) {
                        doReduceTask(reduceTask);
                        reduceLatch.countDown();
                    }
                }

                if (coordinator.allReduceTasksCompleted()) {
                    break;
                }

                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void doMapTask(MapTask mapTask) throws IOException, InterruptedException {
        String content = fileManager.readFile(mapTask.filename());

        List<CustomSet> mappedCustomSet = mapper.map(mapTask.filename(), content);

        fileManager.writeTempFile(mapTask.id(), mappedCustomSet, mapTask.reduceCount());

        for (int i = 0; i < mapTask.reduceCount(); i++) {
            String fileName = String.format("mr-%d-%d", mapTask.id(), i);
            coordinator.addTempFile(i, fileName);
        }

        coordinator.markMapTaskCompleted();
    }

    private void doReduceTask(ReduceTask reduceTask) throws IOException {
        Map<String, List<String>> reduceCustomSet = fileManager.readTempFile(reduceTask.tempFiles());
        for (Map.Entry<String, List<String>> entry : reduceCustomSet.entrySet()) {
            String result = reducer.reduce(entry.getKey(), entry.getValue());
            fileManager.writeFinalFile(entry.getKey(), result);
        }
    }
}
