package org.schmeduler.immutable;

import java.util.Date;

public class DefaultSchedulerBuilder extends SchedulerBuilder<Date, Long, Runnable> {
    public DefaultSchedulerBuilder() {
        super(new DefaultTimeAdapter(), new DefaultSchedulerExecutor(), new DefaultJobExecutor());
    }
}
