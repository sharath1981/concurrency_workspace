package com.kpt;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreSimpleApp {

    private static final Semaphore semaphore = new Semaphore(3);

    public static void main(String[] args) {
        final var service = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 30; i++) {
            service.submit(SemaphoreSimpleApp::runner);
        }
        service.shutdown();
    }

    private static void runner() {
        try {
            semaphore.acquire();
            TimeUnit.SECONDS.sleep(3);
            System.out.println(Thread.currentThread().getName());
        } catch (InterruptedException e) {
        } finally {
            semaphore.release();
        }
    }
}
