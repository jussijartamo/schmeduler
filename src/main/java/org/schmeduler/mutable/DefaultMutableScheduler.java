package org.schmeduler.mutable;

import org.schmeduler.immutable.*;

import java.util.Date;
import java.util.function.BiConsumer;

public class DefaultMutableScheduler extends MutableScheduler<Date, Long, Runnable> implements MutableSchmeduler<Date, Long, Runnable> {

    private DefaultMutableScheduler(BiConsumer<Date, Runnable> executor) {
        super(new DefaultTimeAdapter(),new DefaultSchedulerExecutor(), executor);
    }

    public static DefaultMutableScheduler newScheduler() {
        return new DefaultMutableScheduler(new DefaultJobExecutor());
    }

}
