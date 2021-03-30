package com.kpt;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OddEvenPrinter {

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[42m";
    private static final String RED = "\u001B[41m";
    private static final Lock lock = new ReentrantLock(true);

    public static void main(String[] args) throws InterruptedException {
        final var odd = new Thread(OddEvenPrinter::oddPrinter, RED);
        final var even = new Thread(OddEvenPrinter::evenPrinter, GREEN);
        odd.start();
        even.start();
        odd.join();
        even.join();
    }

    private static void oddPrinter() {
        for (int i = 1; i < 100; i = i + 2) {
            printer(i);
        }
    }

    private static void evenPrinter() {
        for (int i = 2; i < 100; i = i + 2) {
            printer(i);
        }
    }

    private static void printer(final int i) {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + i + " " + RESET);
        } finally {
            lock.unlock();
        }
    }
}
