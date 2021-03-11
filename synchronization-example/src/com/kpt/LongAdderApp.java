package com.kpt;

import java.util.concurrent.atomic.LongAdder;

public class LongAdderApp {

    private static final LongAdder balance = new LongAdder();

    public static void main(String[] args) {
        final var t1 = new Thread(LongAdderApp::deposit);
        final var t2 = new Thread(LongAdderApp::withdraw);
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("BALANCE => : " + balance.sum());
    }

    private static void deposit() {
        for (int i = 0; i < 20_000; i++) {
            balance.increment();
        }
    }

    private static void withdraw() {
        for (int i = 0; i < 20_000; i++) {
            balance.decrement();
        }
    }
}
