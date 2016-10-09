package org.schmeduler;


import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Trigger<TIME> {

    Optional<TIME> getFireTimeAfter(TIME after);

    Optional<TIME> getNextFireTime(TIME startingAt);

    default Stream<TIME> fireTimesAsStream(TIME startingAt) {
        Iterator<TIME> iterator = new Iterator<TIME>() {
            private Optional<TIME> now = getNextFireTime(startingAt);
            @Override
            public boolean hasNext() {
                return now.isPresent();
            }

            @Override
            public TIME next() {
                if(!now.isPresent()) throw new NoSuchElementException();
                TIME next = now.get();
                now = getFireTimeAfter(next);
                return next;
            }
        };
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iterator,
                Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL), false);
    }
}
