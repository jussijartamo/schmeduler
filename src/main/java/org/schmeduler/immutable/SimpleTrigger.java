package org.schmeduler.immutable;

import org.schmeduler.Trigger;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Optional.*;

public final class SimpleTrigger<T, D> implements Trigger<T> {

    private final D duration;
    private final T startingAt;
    private final Optional<T> endingAt;
    private final TimeAdapter<T, D> adapter;
    private final Predicate<T> isBeforeEnding;

    public SimpleTrigger(D duration, T startingAt, Optional<T> endingAt, TimeAdapter<T, D> adapter) {
        this(duration, startingAt, endingAt, adapter, false);
    }
    public SimpleTrigger(D duration, T startingAt, Optional<T> endingAt, TimeAdapter<T, D> adapter, boolean endingInclusive) {
        this.duration = duration;
        this.startingAt = startingAt;
        this.endingAt = endingAt;
        this.adapter = adapter;
        Function<T, Predicate<T>> isBefore = ending -> time -> adapter.isBefore(time, ending) || endingInclusive && (time.equals(ending));
        this.isBeforeEnding = endingAt.map(isBefore).orElse(t -> true);
    }
    public SimpleTrigger<T,D> setEndingInclusive(boolean endingInclusive) {
        return new SimpleTrigger<>(duration, startingAt, endingAt, adapter, endingInclusive);
    }
    public SimpleTrigger<T,D> setEndingAt(T endingAt) {
        return new SimpleTrigger<>(duration, startingAt, ofNullable(endingAt), adapter);
    }
    public SimpleTrigger<T,D> setStartingAt(T startingAt) {
        return new SimpleTrigger<>(duration, startingAt, endingAt, adapter);
    }

    @Override
    public Optional<T> getFireTimeAfter(T after) {
        return getNextFireTime(after).map(t -> {
            if(t.equals(after)) {
                return adapter.plus(t, duration);
            } else {
                return t;
            }
        }).filter(isBeforeEnding);
    }

    @Override
    public Optional<T> getNextFireTime(T startingAt) {
        if(adapter.isBefore(startingAt, this.startingAt)
                || this.startingAt.equals(startingAt)) {
            return of(this.startingAt).filter(isBeforeEnding);
        } else {
            return of(adapter.untilNextEvent(duration, this.startingAt, startingAt)).filter(isBeforeEnding);
        }
    }

}
