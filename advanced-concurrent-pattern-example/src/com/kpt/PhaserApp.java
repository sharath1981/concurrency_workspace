package com.kpt;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class PhaserApp {

    private static final Phaser phaser = new Phaser();

    public static void main(String[] args) throws InterruptedException {
        final var service = Executors.newFixedThreadPool(3);
        TimeUnit.SECONDS.sleep(1);
        service.submit(PhaserApp::runner);
        TimeUnit.SECONDS.sleep(2);
        service.submit(PhaserApp::runner);
        TimeUnit.SECONDS.sleep(3);
        service.submit(PhaserApp::runner);

        TimeUnit.SECONDS.sleep(10);
        service.shutdown();

    }

    private static void runner() {
        phaser.register();
        print("after registering");
        for (int i = 1; i <= 2; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            print("before arrival " + i);
            phaser.arriveAndAwaitAdvance();
            print("after arrival " + i);
        }
    }

    private static void print(String msg) {
        System.out.printf("%-20s: %10s, t=%s, registered=%s, arrived=%s, unarrived=%s phase=%s%n", msg,
                Thread.currentThread().getName(), LocalTime.now(), phaser.getRegisteredParties(),
                phaser.getArrivedParties(), phaser.getUnarrivedParties(), phaser.getPhase());
    }

}
