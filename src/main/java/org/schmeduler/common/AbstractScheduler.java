package org.schmeduler.common;

import org.schmeduler.Schmeduler;
import org.schmeduler.immutable.SingleJobScheduler;
import org.schmeduler.immutable.TimeAdapter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public abstract class AbstractScheduler<TIME, DURATION, JOB> implements Schmeduler {
    private final TimeAdapter<TIME, DURATION> adapter;
    private final AtomicReference<TIME> startedAt = new AtomicReference<>();
    private final BiConsumer<DURATION, Runnable> scheduledExecutor;
    protected abstract List<SingleJobScheduler<TIME,JOB>> getJobSchedulers();
    protected abstract Runnable executionEventForTime(TIME time);
    protected void forceRestart() {
        TIME now = adapter.now();
        findNextFireEvent(now);
    }
    public AbstractScheduler(TimeAdapter<TIME, DURATION> adapter,
                             BiConsumer<DURATION, Runnable> schedulerExecutor) {
        this.adapter = adapter;
        this.scheduledExecutor = schedulerExecutor;
    }

    public TimeAdapter<TIME, DURATION> getAdapter() {
        return adapter;
    }

    protected void executeJobsAt(TIME time) {
        if(isRunning()) {
            getJobSchedulers().stream().filter(t ->
                            t.getNextFireTime(time)
                                    .filter(t0 -> t0.equals(time))
                                    .isPresent()).forEach(
                    job -> job.fire(time));
            findNextFireEventAfter(time);
        } else {
            // stopped
        }
    }

    private boolean isRunning() {
        return startedAt.get() != null;
    }

    private Stream<TIME> flatten(Stream<Optional<TIME>> s) {
        return s.flatMap(o -> o.isPresent() ? Stream.of(o.get()) : Stream.empty());
    }
    private Stream<TIME> fireTimesAfter(TIME after) {
        return flatten(getJobSchedulers().stream().map(job -> job.getFireTimeAfter(after)));
    }
    private Stream<TIME> fireTimesStartingAt(TIME startingAt) {
        return flatten(getJobSchedulers().stream().map(job -> job.getNextFireTime(startingAt)));
    }
    private void fireAtNextEvent(Stream<TIME> events) {
        if(isRunning()) {
            events.sorted((t1, t2) -> {
                if (t1.equals(t2)) {
                    return 0;
                } else if (adapter.isBefore(t1, t2)) {
                    return -1;
                } else {
                    return 1;
                }
            }).findFirst().ifPresent(t -> scheduledExecutor.accept(adapter.duration(t), executionEventForTime(t)));
        }
    }

    private void findNextFireEvent(TIME startingAt) {
        fireAtNextEvent(fireTimesStartingAt(startingAt));
    }
    private void findNextFireEventAfter(TIME after) {
        fireAtNextEvent(fireTimesAfter(after));
    }
    public Schmeduler start() {
        startIfNotAlreadyStarted(adapter.now());
        return this;
    }
    private void startEarliestAtPresent(TIME startingAt, TIME now) {
        if(adapter.isBefore(startingAt, now)) {
            startIfNotAlreadyStarted(now);
        } else {
            startIfNotAlreadyStarted(startingAt);
        }
    }
    private void startIfNotAlreadyStarted(TIME startingAt) {
        boolean notAlreadyStarted = startedAt.compareAndSet(null, startingAt);
        if(notAlreadyStarted) {
            findNextFireEvent(startingAt);
        }
    }
    public void startAt(TIME startingAt) {
        startEarliestAtPresent(startingAt, adapter.now());
    }
    public Schmeduler stop() {
        startedAt.set(null);
        return this;
    }

}
