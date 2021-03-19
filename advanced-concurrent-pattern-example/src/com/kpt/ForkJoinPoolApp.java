package com.kpt;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class ForkJoinPoolApp {

    public static void main(String[] args) {

        //recursiveAction();
        recursiveTask();
    }

    private static void recursiveTask() {
        final var task = new FibonacciTask2(50);
        final var commonPool = ForkJoinPool.commonPool();
        System.out.println(commonPool.invoke(task));
    }

    private static void recursiveAction() {
        final var task = new FibonacciTask1(50);
        final var commonPool = ForkJoinPool.commonPool();
        commonPool.invoke(task);
        System.out.println(task.getNumber());
    }

}

class FibonacciTask1 extends RecursiveAction {

    private static final long threshold = 10;
    private volatile long number;

    public FibonacciTask1(long number) {
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    @Override
    protected void compute() {
        //System.out.println(Thread.currentThread().getName());
        final var n = number;
        if (n <= threshold) {
            number = fib(n);
        } else {
            final var f1 = new FibonacciTask1(n - 1);
            final var f2 = new FibonacciTask1(n - 2);

            ForkJoinTask.invokeAll(f1, f2);
            number = f1.number + f2.number;
        }
    }

    private static long fib(long n) {
        if (n <= 1)
            return n;
        else
            return fib(n - 1) + fib(n - 2);
    }

}

class FibonacciTask2 extends RecursiveTask<Long> {

    private static final long threshold = 10;
    private volatile long number;

    public FibonacciTask2(long number) {
        this.number = number;
    }

    @Override
    protected Long compute() {
        //System.out.println(Thread.currentThread().getName());
        final var n = number;
        if (n <= threshold) {
            return fib(n);
        } else {
            final var f1 = new FibonacciTask2(n - 1);
            final var f2 = new FibonacciTask2(n - 2);

            f1.fork();
            f2.fork();
            return f1.join() + f2.join();
        }
    }

    private static long fib(long n) {
        if (n <= 1)
            return n;
        else
            return fib(n - 1) + fib(n - 2);
    }
}
