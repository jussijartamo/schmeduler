package org.schmeduler.immutable;

import org.schmeduler.common.AbstractScheduler;
import org.schmeduler.Trigger;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static java.util.Collections.*;

public class SchedulerBuilder<TIME, DURATION, JOB> {
    private final TimeAdapter<TIME, DURATION> adapter;
    private final BiConsumer<DURATION, Runnable> schedulerExecutor;
    private final BiConsumer<TIME, JOB> executor;
    private final Collection<PartialSingleJobScheduler<TIME,JOB>> jobSchedulers;
    private final boolean startOnBuild;

    public SchedulerBuilder(TimeAdapter<TIME, DURATION> adapter,
                             BiConsumer<DURATION, Runnable> schedulerExecutor,
                             BiConsumer<TIME, JOB> executor) {
        this(adapter,schedulerExecutor,executor,Collections.emptyList(), true);
    }
    private SchedulerBuilder(TimeAdapter<TIME, DURATION> adapter,
                             BiConsumer<DURATION, Runnable> schedulerExecutor,
                             BiConsumer<TIME, JOB> executor,
                             Collection<PartialSingleJobScheduler<TIME,JOB>> jobSchedulers,
                             boolean startOnBuild) {
        this.adapter = adapter;
        this.schedulerExecutor = schedulerExecutor;
        this.executor = executor;
        this.jobSchedulers = jobSchedulers;
        this.startOnBuild = startOnBuild;
    }

    public AbstractScheduler<TIME,DURATION,JOB> build() {
        if (adapter == null||schedulerExecutor == null||executor == null||(jobSchedulers == null || jobSchedulers.isEmpty())) {
            throw new IllegalStateException("Unable to build incomplete scheduler!");
        }
        List<SingleJobScheduler<TIME,JOB>> jobs = jobSchedulers.stream()
                .map(j -> new SingleJobScheduler<>(j.trigger, j.job, executor))
                .collect(Collectors.toList());
        Scheduler<TIME, DURATION, JOB> scheduler = new Scheduler<>(adapter, schedulerExecutor, jobs);
        if(startOnBuild) {
            scheduler.start();
        }
        return scheduler;
    }

    private Collection<PartialSingleJobScheduler<TIME,JOB>> appendJobs(
            Collection<PartialSingleJobScheduler<TIME,JOB>> jobSchedulers,
            PartialSingleJobScheduler<TIME,JOB> jobScheduler) {
        ArrayList<PartialSingleJobScheduler<TIME, JOB>> p = new ArrayList<>(jobSchedulers);
        p.add(jobScheduler);
        return unmodifiableList(p);
    }
    public SchedulerBuilder addJobs(Trigger<TIME> trigger, Collection<JOB> jobs) {
        Collection<PartialSingleJobScheduler<TIME,JOB>> partialJobs = jobSchedulers;
        for(JOB job : jobs) {
            PartialSingleJobScheduler partialJobScheduler = new PartialSingleJobScheduler(trigger, job);
            partialJobs = appendJobs(partialJobs, partialJobScheduler);
        }
        return new SchedulerBuilder(adapter, schedulerExecutor, executor, partialJobs, startOnBuild);
    }
    public SchedulerBuilder addJob(Trigger<TIME> trigger, JOB job) {
        PartialSingleJobScheduler partialJobScheduler = new PartialSingleJobScheduler(trigger, job);
        return new SchedulerBuilder(adapter, schedulerExecutor, executor,
                appendJobs(jobSchedulers, partialJobScheduler), startOnBuild);
    }
    public SchedulerBuilder setStartOnBuild(boolean startOnBuild) {
        return new SchedulerBuilder(adapter, schedulerExecutor, executor, jobSchedulers, startOnBuild);
    }

    private class PartialSingleJobScheduler<TIME,JOB> {
        private final Trigger<TIME> trigger;
        private final JOB job;

        PartialSingleJobScheduler(Trigger<TIME> trigger, JOB job) {
            this.trigger = trigger;
            this.job = job;
        }
    }
}
