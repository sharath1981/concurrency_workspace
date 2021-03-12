package com.kpt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CustomBQWaitNotifySolution {
    private static final CountDownLatch latch = new CountDownLatch(8);
    private static final BlockingQueueWaitNotify<Integer> queue = new BlockingQueueWaitNotify<>(5);

    public static void main(String[] args) throws Exception {
        final var service = Executors.newFixedThreadPool(8);
        try {
            service.submit(CustomBQWaitNotifySolution::producer);
            service.submit(CustomBQWaitNotifySolution::consumer);
            service.submit(CustomBQWaitNotifySolution::producer);
            service.submit(CustomBQWaitNotifySolution::consumer);
            service.submit(CustomBQWaitNotifySolution::producer);
            service.submit(CustomBQWaitNotifySolution::consumer);
            service.submit(CustomBQWaitNotifySolution::producer);
            service.submit(CustomBQWaitNotifySolution::consumer);
            System.out.println("Before await...");
            latch.await();
            System.out.println("Completed...");
        } finally {
            service.shutdown();
        }
    }

    private static void producer() {
        for (int i = 0; i < 100; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(50);
                queue.put(i);
                System.out.println("Produced => " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        latch.countDown();
    }

    private static void consumer() {
        for (int i = 0; i < 100; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println("Consumed => " + queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        latch.countDown();
    }
}

class BlockingQueueWaitNotify<E> {

    private final Object lock = new Object();

    private final List<E> list;
    private final int capacity;

    public BlockingQueueWaitNotify(int capacity) {
        this.list = new ArrayList<>(capacity);
        this.capacity = capacity;
    }

    public void put(final E element) throws InterruptedException {
        synchronized (lock) {
            while (capacity == list.size()) {
                lock.wait();
            }
            list.add(element);
            lock.notify();
        }
    }

    public E take() throws InterruptedException {
        synchronized (lock) {
            while (list.isEmpty()) {
                lock.wait();
            }
            final var element = list.remove(0);
            lock.notify();
            return element;
        }
    }
}
