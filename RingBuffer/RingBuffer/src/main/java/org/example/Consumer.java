package org.example;

public class Consumer implements Runnable{
    private final RingBuffer<Integer> ringBuffer;
    private final int itemCount;

    public Consumer(RingBuffer<Integer> ringBuffer, int itemCount) {
        this.ringBuffer = ringBuffer;
        this.itemCount = itemCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < itemCount; i++) {
            try {
                System.out.println("Getting: " + ringBuffer.get() + " " + Thread.currentThread().getName());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
