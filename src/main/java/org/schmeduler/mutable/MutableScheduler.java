package org.schmeduler.mutable;

import org.schmeduler.common.AbstractScheduler;
import org.schmeduler.Trigger;
import org.schmeduler.immutable.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

/**
 * Created by jussi on 9.10.2016.
 */
public class MutableScheduler<TIME, DURATION, JOB> extends AbstractScheduler<TIME, DURATION, JOB> implements MutableSchmeduler<TIME, DURATION, JOB> {
    private final AtomicLong turnNumber = new AtomicLong(0);
    private final CopyOnWriteArrayList<SingleJobScheduler<TIME,JOB>> mutableJobList;
    private final BiConsumer<TIME, JOB> executor;

    public MutableScheduler(TimeAdapter<TIME, DURATION> adapter,
                            BiConsumer<DURATION, Runnable> schedulerExecutor,
                            BiConsumer<TIME, JOB> executor) {
        super(adapter,schedulerExecutor);
        this.mutableJobList = new CopyOnWriteArrayList<>();
        this.executor = executor;
    }

    @Override
    public void updated() {
        forceRestart();
    }

    @Override
    public MutableSchmeduler newJob(Trigger<TIME> trigger, JOB runnable) {
        mutableJobList.add(new SingleJobScheduler(trigger, runnable, executor));
        updated();
        return this;
    }

    @Override
    public List<SingleJobScheduler<TIME, JOB>> getJobSchedulers() {
        return this.mutableJobList;
    }

    @Override
    protected Runnable executionEventForTime(TIME date) {
        final Long atomicTurnNumber = turnNumber.incrementAndGet();
        return () -> {
            if(atomicTurnNumber.equals(turnNumber.get())) {
                this.executeJobsAt(date);
            }
        };
    }
}

