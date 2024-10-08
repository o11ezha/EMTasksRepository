package org.example.node;

import org.example.task.MapTask;
import org.example.task.ReduceTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Coordinator {
    private final BlockingQueue<MapTask> mapQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<ReduceTask> reduceQueue = new LinkedBlockingQueue<>();
    private final Map<Integer, List<String>> tempFiles = new ConcurrentHashMap<>();
    private int completedMapTasks = 0;
    private final int totalMapTasks;
    private final int reduceCount;

    public Coordinator(List<String> inputFiles, int reduceCount) {
        this.reduceCount = reduceCount;
        this.totalMapTasks = inputFiles.size();
        for (int i = 0; i < totalMapTasks; i++) {
            mapQueue.add(new MapTask(i, inputFiles.get(i), reduceCount));
        }
    }

    public MapTask getMapTask(){
        return mapQueue.poll();
    }

    public ReduceTask getReduceTask(){
        return reduceQueue.poll();
    }

    public void addTempFile(int reduceTaskID, String filename) {
        tempFiles.computeIfAbsent(reduceTaskID, k -> new ArrayList<>()).add(filename);
    }

    public void addReduceTask() throws InterruptedException {
        for (int i = 0; i < reduceCount; i++) {
            reduceQueue.put(new ReduceTask(i, tempFiles.getOrDefault(i, Collections.emptyList())));
        }
    }

    public synchronized void markMapTaskCompleted() throws InterruptedException {
        completedMapTasks++;
        if (allMapTasksCompleted()) {
            addReduceTask();
        }
    }

    public synchronized boolean allMapTasksCompleted() {
        return mapQueue.isEmpty() && completedMapTasks == totalMapTasks;
    }

    public boolean allReduceTasksCompleted() {
        return reduceQueue.isEmpty();
    }
}
