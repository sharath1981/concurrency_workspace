package com.kpt;

import java.util.concurrent.TimeUnit;

public class StoppingThread {

    public static void main(String[] args) {
        
        final var thread = new Thread(StoppingThread::process);
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread.interrupt();
        
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void process() {
        while(!Thread.currentThread().isInterrupted()) {
            System.out.println(Thread.currentThread().getName()+" PROCESSING....");
        }
        System.out.println(Thread.currentThread().getName()+" STOPPED!");
    }

}
