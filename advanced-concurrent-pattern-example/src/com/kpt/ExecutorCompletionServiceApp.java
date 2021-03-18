package com.kpt;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ExecutorCompletionServiceApp {

    private static final Random rand = new Random();

    public static void main(String[] args) {

        //executorService();
        // difference between ExecutorService and ExecutorCompletionService
        executorCompletionService();

    }

    private static void executorService() {
        final var service = Executors.newFixedThreadPool(10);
        final var futures = Stream.generate(() -> service.submit(ExecutorCompletionServiceApp::runner))
                                             .limit(10).collect(Collectors.toSet());

        for (Future<String> future : futures) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private static void executorCompletionService() {
        final var service = Executors.newFixedThreadPool(10);
        final var completionService = new ExecutorCompletionService<>(service);

        IntStream.rangeClosed(1, 10).forEach(i -> {
            completionService.submit(ExecutorCompletionServiceApp::runner);
        });

        IntStream.rangeClosed(1, 10).forEach(i -> {
            try {
                System.out.println(completionService.take().get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    private static String runner() throws InterruptedException {
        final var sleep = rand.nextInt(20);
        TimeUnit.SECONDS.sleep(sleep);
        return String.format("%s finished in %d seconds...", Thread.currentThread().getName(), sleep);
    }

}
