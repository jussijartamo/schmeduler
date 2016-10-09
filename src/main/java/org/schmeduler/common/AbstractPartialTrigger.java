package org.schmeduler.common;

import org.schmeduler.immutable.SimpleTrigger;
import org.schmeduler.immutable.TimeAdapter;

import static java.util.Optional.empty;

public abstract class AbstractPartialTrigger<TIME,DURATION,TRIGGER> {
    private final DURATION duration;
    private final TimeAdapter<TIME, DURATION> adapter;

    public AbstractPartialTrigger(DURATION duration, TimeAdapter<TIME, DURATION> adapter) {
        this.duration = duration;
        this.adapter = adapter;
    }

    protected abstract TRIGGER newTrigger(DURATION newDuration, TimeAdapter<TIME, DURATION> adapter);


    @Override
    public String toString() {
        return String.format("partial interval with duration of %s ", duration);
    }

    public TRIGGER seconds() {
        return newTrigger(adapter.asSeconds(duration), adapter);
    }
    public TRIGGER minutes() {
        return newTrigger(adapter.asMinutes(duration), adapter);
    }
    public TRIGGER hours() {
        return newTrigger(adapter.asHours(duration), adapter);
    }
    public TRIGGER days() {
        return newTrigger(adapter.asDays(duration), adapter);
    }
}
