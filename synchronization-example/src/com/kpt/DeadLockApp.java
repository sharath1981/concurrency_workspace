package com.kpt;

import java.util.concurrent.TimeUnit;

public class DeadLockApp {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();
    public static void main(String[] args) throws InterruptedException {
        final var t1 = new Thread(DeadLockApp::runner1);
        final var t2 = new Thread(DeadLockApp::runner2);
        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    private static void runner1() {
        synchronized (lock1) {
            System.out.println(Thread.currentThread().getName()+" is holding lock1");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" is waiting for lock2");
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName()+" is holding lock2");
            }
        }
    }

    private static void runner2() {
        synchronized (lock2) {
            System.out.println(Thread.currentThread().getName()+" is holding lock2 ");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" is waiting for lock1");
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName()+" is holding lock1");
            }
        }
    }
}
