package com.kpt;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DataRaceApp {
    public static void main(String[] args) throws Exception {
        final var sharedResource = new SharedResource();
        final var service = Executors.newFixedThreadPool(12);
        try {
            service.submit(sharedResource::increment);
            service.submit(sharedResource::check);
            TimeUnit.SECONDS.sleep(30);

        } finally {
            service.shutdown();
        }
        System.out.println("Done...");
    }
}

class SharedResource {
    private int x = 0;
    private volatile int y = 0;

    public void increment() {
        while (true) {
            x++;
            y++;
        }
    }

    public void check() {
        while (true) {
            if (y > x) {
                System.out.println("Datarace detected...");
            }
        }
    }
}
