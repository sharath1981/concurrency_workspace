package com.kpt;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerApp {

    private static final AtomicInteger balance = new AtomicInteger();

    public static void main(String[] args) {
        final var t1 = new Thread(AtomicIntegerApp::deposit);
        final var t2 = new Thread(AtomicIntegerApp::withdraw);
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("BALANCE => : " + balance.get());
    }

    private static void deposit() {
        for (int i = 0; i < 20_000; i++) {
            balance.incrementAndGet();
        }
        /*
         * Stream.generate(balance::incrementAndGet) .limit(20_000) .count();
         */
    }

    private static void withdraw() {
        for (int i = 0; i < 20_000; i++) {
            balance.decrementAndGet();
        }
        /*
         * Stream.generate(balance::decrementAndGet) .limit(20_000) .count();
         */
    }
}
