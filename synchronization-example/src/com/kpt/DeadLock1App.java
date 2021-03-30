package com.kpt;

public class DeadLock1App {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) throws InterruptedException {

        final var t1 = new Thread(DeadLock1App::taskA, "RED");
        final var t2 = new Thread(DeadLock1App::taskB, "GREEN");
        final var t3 = new Thread(DeadLock1App::taskC, "YELLOW");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t2.join();
        
    }

    private static void taskA() {
        synchronized (lock1) {
            System.out.println(Thread.currentThread().getName()+ " is holding lock1");
            taskB();
        }
    }

    private static void taskB() {
        synchronized (lock2) {
            System.out.println(Thread.currentThread().getName()+ " is holding lock2");
            taskC();
        }
    }

    private static void taskC() {
        synchronized (lock1) {
            System.out.println(Thread.currentThread().getName()+ " is holding lock1");
        }
    }
}
