package org.schmeduler.immutable;

import java.util.Date;

public interface SchmedulerBuilder {

    static PartialTrigger<Date, Long> every(long duration) {
        return new PartialTrigger<>(duration, new DefaultTimeAdapter());
    }

    static DefaultSchedulerBuilder newBuilder() {
        return new DefaultSchedulerBuilder();
    }
}
