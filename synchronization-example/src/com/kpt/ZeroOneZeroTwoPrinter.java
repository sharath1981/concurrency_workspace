package com.kpt;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

public class ZeroOneZeroTwoPrinter {

    private static final Semaphore zSem = new Semaphore(1); 
    private static final Semaphore oSem = new Semaphore(0); 
    private static final Semaphore eSem = new Semaphore(0); 
    public static void main(String[] args) {
        CompletableFuture.allOf(
            CompletableFuture.runAsync(ZeroOneZeroTwoPrinter::zero),
            CompletableFuture.runAsync(ZeroOneZeroTwoPrinter::odd),
            CompletableFuture.runAsync(ZeroOneZeroTwoPrinter::even)).join();
    }

    private static void zero() {
        for (int i = 1; i < 10; i++) {
            try {
                zSem.acquire();
                System.out.print("0");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                (i%2==0 ? eSem : oSem).release();
            }
           
        }
    }

    private static void odd() {
        for (int i = 1; i < 10; i+=2) {
            try {
                oSem.acquire();
                System.out.print(""+i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                zSem.release();
            }
        }
    }

    private static void even() {
        for (int i = 2; i < 10; i+=2) {
            try {
                eSem.acquire();
                System.out.print(""+i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                zSem.release();
            }
        }
    }
}
