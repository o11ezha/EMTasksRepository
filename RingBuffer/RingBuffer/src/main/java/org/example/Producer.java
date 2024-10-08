package org.example;

public class Producer implements Runnable {
    private final RingBuffer<Integer> ringBuffer;
    private final int itemCount;

    public Producer(RingBuffer<Integer> ringBuffer, int itemCount) {
        this.ringBuffer = ringBuffer;
        this.itemCount = itemCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < itemCount; i++) {
            try {
                System.out.println("Putting: " + i + " " + Thread.currentThread().getName());
                ringBuffer.put(i);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
