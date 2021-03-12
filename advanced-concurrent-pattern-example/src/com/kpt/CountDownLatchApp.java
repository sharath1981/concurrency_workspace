package com.kpt;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class CountDownLatchApp {

    private static final CountDownLatch latch = new CountDownLatch(4);

    public static void main(String[] args) throws Exception {

        final var service = Executors.newFixedThreadPool(4);
        IntStream.rangeClosed(1, 4)
                 .forEach(i -> service.submit(() -> CountDownLatchApp.printer(i)));
        latch.await();
        System.out.println(Thread.currentThread().getName() + " : latch count => " + latch.getCount());
        service.shutdown();
    }

    synchronized private static void printer(final int sleep) {
        try {
            TimeUnit.SECONDS.sleep(sleep);
            System.out.println(Thread.currentThread().getName() + " : latch count => " + latch.getCount());
            latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
