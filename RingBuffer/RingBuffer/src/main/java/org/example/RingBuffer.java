package org.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RingBuffer<T> {
    private final T[] buffer;
    private int head, tail, count;
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    @SuppressWarnings("unchecked")
    public RingBuffer(int size) {
        buffer = (T[]) new Object[size];
        head = 0;
        tail = 0;
        count = 0;
    }

    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (count == buffer.length) {
                notFull.await();
            }
            buffer[tail] = item;
            tail = (tail + 1) % buffer.length;
            count++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T get() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }

            T item = buffer[head];
            head = (head + 1) % buffer.length;
            count--;
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
}
