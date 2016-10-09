package org.schmeduler.immutable;

import org.schmeduler.Trigger;

public interface TriggerBuilder<TIME, DURATION> extends TimeAdapter<TIME, DURATION> {

    Trigger<TIME> every(DURATION duration);
}
