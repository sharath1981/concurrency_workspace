package com.kpt;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Exchanger;

public class ExchangerApp {
    private static final Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        CompletableFuture.allOf(CompletableFuture.runAsync(ExchangerApp::racer1),
                CompletableFuture.runAsync(ExchangerApp::racer2)).join();
    }

    private static void racer1() {
        try {
            final var threadName = Thread.currentThread().getName();
            final var message = String.format("SHARATH from %s", threadName);
            System.out.printf("%s => %s %n", threadName, exchanger.exchange(message));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void racer2() {
        try {
            final var threadName = Thread.currentThread().getName();
            final var message = String.format("KUMAR from %s", threadName);
            System.out.printf("%s => %s %n", Thread.currentThread().getName(), exchanger.exchange(message));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
