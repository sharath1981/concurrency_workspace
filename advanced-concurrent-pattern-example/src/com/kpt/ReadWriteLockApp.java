package com.kpt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

public class ReadWriteLockApp {

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock readLock = lock.readLock();
    private static final Lock writeLock = lock.writeLock();
    private static final List<Integer> list = new ArrayList<>();
    private static final Random random = new Random();

    public static void main(String[] args) {

        final var service = Executors.newFixedThreadPool(10);
        IntStream.range(0, 10).forEach(i -> {
            service.submit(ReadWriteLockApp::write);
            service.submit(ReadWriteLockApp::read);
        });
        service.shutdown();

    }

    private static void read() {
        readLock.lock();
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println(list+" Read by : " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    private static void write() {
        writeLock.lock();
        try {
            TimeUnit.SECONDS.sleep(2);
            final var number = random.nextInt(10);
            list.add(number);
            System.out.println(number + " Written by : " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }
}
