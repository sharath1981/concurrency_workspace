package com.kpt;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreOddEvenPrinter {
    private static final Semaphore odd = new Semaphore(0);
    private static final Semaphore even = new Semaphore(1);
    public static void main(String[] args) {
        CompletableFuture.allOf(
                CompletableFuture.runAsync(SemaphoreOddEvenPrinter::oddPrinter),
                CompletableFuture.runAsync(SemaphoreOddEvenPrinter::evenPrinter)).join();
    }

    private static void oddPrinter() {
        for (int i = 1; i < 100; i+=2) {
            try {
                odd.acquire();
                System.out.println("odd -> "+i);
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                even.release();
            }
        }
    }
    private static void evenPrinter() {
        for (int i = 0; i < 100; i+=2) {
            try {
                even.acquire();
                System.out.println("even -> "+i);
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                odd.release();
            }
        }
    }
}
