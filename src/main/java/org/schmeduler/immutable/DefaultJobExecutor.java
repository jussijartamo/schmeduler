package org.schmeduler.immutable;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

public class DefaultJobExecutor implements BiConsumer<Date, Runnable> {
    private final ExecutorService executorService;
    public DefaultJobExecutor() {
        this(Executors.newSingleThreadExecutor());
    }
    public DefaultJobExecutor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void accept(Date date, Runnable runnable) {
        executorService.submit(runnable);
    }
}
