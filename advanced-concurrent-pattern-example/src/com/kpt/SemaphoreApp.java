package com.kpt;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SemaphoreApp {

    private static final Semaphore semaphore = new Semaphore(3);
    private static final List<AtmMachine> atmMachines = new CopyOnWriteArrayList<>();

    static {
        atmMachines.add(new AtmMachine("ATM-1"));
        atmMachines.add(new AtmMachine("ATM-2"));
        atmMachines.add(new AtmMachine("ATM-3"));
    }

    public static void main(String[] args) {
        final var service = Executors.newFixedThreadPool(9);
        IntStream.range(0, 9).forEach(i -> service.submit(SemaphoreApp::enterAtmRoom));
        service.shutdown();
    }

    private static void enterAtmRoom() {
        try {
            semaphore.acquire();
            withdrawMoneyFromAtmMachine();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    private static void withdrawMoneyFromAtmMachine() {
        try {
            final var atmMachine = atmMachines.remove(0);
            atmMachine.addVisitor(Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(2);
            atmMachines.add(atmMachine);
            System.out.println("Money withdrawn from the " + atmMachine);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class AtmMachine {
    private final String id;
    private final List<String> visitors;

    public AtmMachine(String id) {
        this.id = id;
        visitors = new CopyOnWriteArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<String> getVisitors() {
        return visitors;
    }

    public void addVisitor(final String visitor) {
        visitors.add(visitor);
    }

    @Override
    public String toString() {
        return "AtmMachine [id=" + id + ", visitors=" + visitors + "]";
    }

}
