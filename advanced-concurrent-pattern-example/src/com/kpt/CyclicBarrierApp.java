package com.kpt;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class CyclicBarrierApp {

    private static final CyclicBarrier barrier = new CyclicBarrier(4, CyclicBarrierApp::processAtBarrier);
    private static final Random random = new Random(1); 

    public static void main(String[] args) throws Exception {

        final var service = Executors.newFixedThreadPool(4);
        IntStream.rangeClosed(1, 4).forEach(i -> service.submit(CyclicBarrierApp::printer));
        service.shutdown();
    }

    private static void printer() {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(10));
            System.out.println("     Number of parties reached Barrier(meeting point) => " + barrier.getNumberWaiting());
            System.out.println("|---------------------------------------------------------------------------------------------|");
            System.out.println("    "+Thread.currentThread().getName() + " : reached Barrier(meeting point)");
            barrier.await();
            System.out.println("    "+Thread.currentThread().getName() + " : proceeding after all parties reached Barrier(meeting point) ");
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private static void processAtBarrier() {
        System.out.println("     Number of parties reached Barrier(meeting point) => " + barrier.getNumberWaiting());
        System.out.println("     All parties reached Barrier(meeting point)");
        System.out.println("|---------------------------------------------------------------------------------------------|");
    }

}
