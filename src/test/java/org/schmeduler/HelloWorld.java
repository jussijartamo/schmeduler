package org.schmeduler;

import static org.schmeduler.immutable.SchmedulerBuilder.*;

public class HelloWorld {

    public static void main(String[] args) {
        final Runnable job = () -> System.out.println("Hello World!");

        newBuilder().addJob(every(1).seconds(), job).build();
    }
}
