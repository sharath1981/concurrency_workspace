package com.kpt;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BlockingQueueSolution {

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[42m";
    private static final String RED = "\u001B[41m";
    private static final Random rand = new Random();
    private static final CountDownLatch latch = new CountDownLatch(8);
    private static final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

    public static void main(String[] args) throws Exception {
        final var service = Executors.newFixedThreadPool(8);
        try {
            service.submit(BlockingQueueSolution::producer);
            service.submit(BlockingQueueSolution::consumer);
            service.submit(BlockingQueueSolution::producer);
            service.submit(BlockingQueueSolution::consumer);
            service.submit(BlockingQueueSolution::producer);
            service.submit(BlockingQueueSolution::consumer);
            service.submit(BlockingQueueSolution::producer);
            service.submit(BlockingQueueSolution::consumer);
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
                TimeUnit.MILLISECONDS.sleep(rand.nextInt(1000));
                queue.put(i);
                System.out.println(GREEN + "Produced => " + i + RESET);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        latch.countDown();
    }

    private static void consumer() {
        for (int i = 0; i < 100; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(rand.nextInt(1000));
                System.out.println(RED + queue.take() + " => Consumed" + RESET);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        latch.countDown();
    }
}
