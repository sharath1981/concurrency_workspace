package com.ryana;

import java.util.Objects;

public class Resource {

    // volatile To establish happens before link and to fix reordering of statements
    // resource=new Resource().
    private static volatile Resource resource;

    // To restrict instantiation from outside
    private Resource() {
    }

    // static factory method for easy access from outside
    public static Resource getInstance() {

        // this check is to improve performance hindered by synchronization
        if (Objects.isNull(resource)) {
            // Allow only one thread at a time during concurrency.
            synchronized (Resource.class) {
                // this check for lazy loading
                if (Objects.isNull(resource)) {
                    resource = new Resource();
                }
            }
        }
        return resource;
    }
}
