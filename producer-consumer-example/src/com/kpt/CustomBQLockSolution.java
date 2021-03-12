package com.kpt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomBQLockSolution {

    private static final CountDownLatch latch = new CountDownLatch(8);
    private static final BlockingQueueLock<Integer> queue = new BlockingQueueLock<>(5);

    public static void main(String[] args) throws Exception {
        final var service = Executors.newFixedThreadPool(8);
        try {
            service.submit(CustomBQLockSolution::producer);
            service.submit(CustomBQLockSolution::consumer);
            service.submit(CustomBQLockSolution::producer);
            service.submit(CustomBQLockSolution::consumer);
            service.submit(CustomBQLockSolution::producer);
            service.submit(CustomBQLockSolution::consumer);
            service.submit(CustomBQLockSolution::producer);
            service.submit(CustomBQLockSolution::consumer);
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

class BlockingQueueLock<E> {

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private final List<E> list;
    private final int capacity;

    public BlockingQueueLock(int capacity) {
        this.list = new ArrayList<>(capacity);
        this.capacity = capacity;
    }

    public void put(final E element) throws InterruptedException {
        lock.lock();
        try {
            while (capacity == list.size()) {
                notFull.await();
            }
            list.add(element);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        lock.lock();
        try {
            while (list.isEmpty()) {
                notEmpty.await();
            }
            final var element = list.remove(0);
            notFull.signal();
            return element;
        } finally {
            lock.unlock();
        }
    }
}
