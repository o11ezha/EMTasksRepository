package org.example;

public class Main {
    private static final Object lock = new Object();
    private static final int MAX_INPUT_COUNT = 20;
    private static int count = 1;

    public static void main(String[] args) {
        Thread evenThread = new Thread(() -> {
           while (count <= MAX_INPUT_COUNT) {
               synchronized (lock) {
                   if (count % 2 == 0){
                       System.out.println(Thread.currentThread().getName() + ": " + count++);
                       lock.notify();
                   } else {
                       try {
                           lock.wait();
                       } catch (InterruptedException e) {
                           throw new RuntimeException(e);
                       }
                   }
               }
           }
        });

        Thread oddThread = new Thread(() -> {
            while (count <= MAX_INPUT_COUNT) {
                synchronized (lock) {
                    if (count % 2 != 0) {
                        System.out.println(Thread.currentThread().getName() + ": " + count++);
                        lock.notify();
                    } else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        evenThread.setName("Even Thread");
        oddThread.setName("Odd Thread");

        evenThread.start();
        oddThread.start();
    }
}