package com.kpt;

import java.util.concurrent.TimeUnit;

public class LiveLockApp {

    private static final String GREEN = "\u001B[42m";
    private static final String RED = "\u001B[41m";

    public static void main(String[] args) {

        final var customer = new Customer();
        final var shop = new Shop();
        final var t1 = new Thread(() -> customer.payMoney(shop), GREEN + "CUSTOMER");
        final var t2 = new Thread(() -> shop.shipOrder(customer), RED + "SHOP");
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Customer {
    private static final String RESET = "\u001B[0m";
    private boolean paid;

    public void payMoney(Shop shop) {
        while (!shop.isOrderShipped()) {
            System.out.println(Thread.currentThread().getName() + "is waiting for order" + RESET);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        paid = true;
    }

    public boolean isPaid() {
        return paid;
    }
}

class Shop {
    private static final String RESET = "\u001B[0m";
    private boolean orderShipped;

    public void shipOrder(Customer customer) {
        while (!customer.isPaid()) {
            System.out.println(Thread.currentThread().getName() + " is waiting for money " + RESET);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        orderShipped = true;
    }

    public boolean isOrderShipped() {
        return orderShipped;
    }
}