package com.kpt;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class CyclicBarrierApp {

    private static final CyclicBarrier barrier = new CyclicBarrier(4, CyclicBarrierApp::print);

    public static void main(String[] args) throws Exception {

        final var service = Executors.newFixedThreadPool(4);
        IntStream.rangeClosed(1, 4).forEach(i -> service.submit(() -> CyclicBarrierApp.printer(i)));
        service.shutdown();
    }

    private static void printer(final int sleep) {
        try {
            TimeUnit.SECONDS.sleep(sleep);
            System.out
                    .println(Thread.currentThread().getName() + " : waiting parties => " + barrier.getNumberWaiting());
            System.out.println(Thread.currentThread().getName() + " : Barrier Broken ? => " + barrier.isBroken());
            barrier.await();
            System.out.println(Thread.currentThread().getName() + " : proceeding after all parties reached at point ");
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private static void print() {
        System.out.println(Thread.currentThread().getName() + " : waiting parties => " + barrier.getNumberWaiting());
    }

}
