package com.kpt;

import java.util.concurrent.atomic.AtomicInteger;

public class SequentialPrinter {
    private static final AtomicInteger counter = new AtomicInteger();
    public static void main(String[] args) {

        for (int i = 1; i < 11; i++) {
            final var thread = new Thread(SequentialPrinter::print, "Thread-"+i);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void print() {
        System.out.format("%s => %d %n", Thread.currentThread().getName(), counter.incrementAndGet());
    }
}
