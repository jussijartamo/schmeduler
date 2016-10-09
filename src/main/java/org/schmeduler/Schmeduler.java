package org.schmeduler;

public interface Schmeduler {


    Schmeduler start();

    Schmeduler stop();

    /*
    public static PartialSimpleTrigger<Date, Long> every(long duration){
        return new PartialSimpleTrigger(duration, new DefaultTimeAdapter());
    }
    */
}
