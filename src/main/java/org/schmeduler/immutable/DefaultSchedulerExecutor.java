package org.schmeduler.immutable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class DefaultSchedulerExecutor implements BiConsumer<Long, Runnable> {
    private final ScheduledExecutorService scheduledExecutorService;
    public DefaultSchedulerExecutor() {
        this(Executors.newSingleThreadScheduledExecutor());
    }
    public DefaultSchedulerExecutor(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public void accept(Long milliseconds, Runnable runnable) {
        scheduledExecutorService.schedule(
                runnable,
                milliseconds,
                TimeUnit.MILLISECONDS);
    }
}
