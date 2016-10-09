package org.schmeduler.immutable;

import org.schmeduler.common.AbstractScheduler;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class Scheduler<TIME,DURATION,JOB> extends AbstractScheduler<TIME,DURATION,JOB> {
    private final List<SingleJobScheduler<TIME,JOB>> jobSchedulers;

    public Scheduler(TimeAdapter<TIME, DURATION> adapter,
                     BiConsumer<DURATION, Runnable> schedulerExecutor,
                     List<SingleJobScheduler<TIME,JOB>> jobSchedulers) {
        super(adapter, schedulerExecutor);
        this.jobSchedulers = Collections.unmodifiableList(jobSchedulers);
    }

    @Override
    public List<SingleJobScheduler<TIME, JOB>> getJobSchedulers() {
        return this.jobSchedulers;
    }

    @Override
    protected void forceRestart() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Runnable executionEventForTime(TIME time) {
        return () -> this.executeJobsAt(time);
    }
}
