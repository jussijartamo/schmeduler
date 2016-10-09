package org.schmeduler.immutable;

import org.schmeduler.Trigger;

import java.util.Optional;
import java.util.function.BiConsumer;

public class SingleJobScheduler<TIME, JOB> {
    private final Trigger<TIME> trigger;
    private final JOB job;
    private final BiConsumer<TIME, JOB> executor;

    public SingleJobScheduler(Trigger<TIME> trigger,
                              JOB job,
                              BiConsumer<TIME, JOB> executor) {
        this.trigger = trigger;
        this.job = job;
        this.executor = executor;
    }

    public Optional<TIME> getFireTimeAfter(TIME after) {
        return trigger.getFireTimeAfter(after);
    }

    public Optional<TIME> getNextFireTime(TIME startingAt) {
        return trigger.getNextFireTime(startingAt);
    }

    public void fire(TIME fireTime) {
        executor.accept(fireTime, job);
    }

}
