package com.kpt;

public class Synchronization {

    private static int balance;

    public static void main(String[] args) {
        final var t1 = new Thread(Synchronization::deposit);
        final var t2 = new Thread(Synchronization::withdraw);
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("BALANCE => : " + balance);
    }

    synchronized private static void deposit() {
        for (int i = 0; i < 20_000; i++) {
            balance++;
        }
    }

    synchronized private static void withdraw() {
        for (int i = 0; i < 20_000; i++) {
            balance--;
        }
    }
}
