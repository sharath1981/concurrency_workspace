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
            System.out.println(" racer1() : " + exchanger.exchange("SHARATH from racer1"));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void racer2() {
        try {
            System.out.println(" racer2() : " + exchanger.exchange("KUMAR from racer2"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
