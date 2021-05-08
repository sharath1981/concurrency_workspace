package com.kpt;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class CallableFutureApp {
    public static void main(String[] args) {
        final var executorService = Executors.newFixedThreadPool(10);
        final var task = new Task();
        Stream.generate(()->executorService.submit(task))
              .limit(10)
              .peek(future-> System.out.println(future.isDone()))
              .map(future -> {
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                    return null;
                }
            }).forEach(System.out::println);
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        System.out.println("Done!!!");
    }

}

class Task implements Callable<String> {

    private static final AtomicInteger count = new AtomicInteger();

    @Override
    public String call() throws Exception {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return String.format("%s => %d", Thread.currentThread().getName(), count.incrementAndGet());
    }

}
