package org.schmeduler.immutable;

public interface TimeAdapter<TIME, DURATION> {
    TIME now();
    default DURATION duration(TIME until) {
        return duration(now(), until);
    }
    TIME untilNextEvent(DURATION step, TIME startingAt, TIME currentlyAt);
    TIME plus(TIME time, DURATION duration);
    DURATION duration(TIME first, TIME second);

    boolean isBefore(TIME first, TIME second);

    DURATION asSeconds(DURATION duration);
    DURATION asMinutes(DURATION duration);
    DURATION asHours(DURATION duration);
    DURATION asDays(DURATION duration);

}
