package com.ryana;

public class Resource1 {
    private Resource1() {
    }

    private static class Holder {
        private static final Resource1 INSTANCE = new Resource1();
    }

    // Lazily loaded when this method called
    public static Resource1 getInstance() {
        return Holder.INSTANCE;
    }
}
