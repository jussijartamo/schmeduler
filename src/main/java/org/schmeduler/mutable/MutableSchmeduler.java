package org.schmeduler.mutable;

import org.schmeduler.Schmeduler;
import org.schmeduler.Trigger;
import org.schmeduler.immutable.TimeAdapter;

import java.util.Collection;

public interface MutableSchmeduler<TIME,DURATION,JOB> extends Schmeduler {

    void updated();

    TimeAdapter<TIME, DURATION> getAdapter();

    MutableSchmeduler newJob(Trigger<TIME> trigger, JOB job);

    default MutableSchmeduler newJobs(Trigger<TIME> trigger, Collection<? extends JOB> jobs) {
        for(JOB job : jobs) {
            newJob(trigger, job);
        }
        return this;
    }

    default MutablePartialTrigger<TIME, DURATION> every(DURATION duration) {
        return new MutablePartialTrigger(duration, getAdapter(), this);
    }
}
