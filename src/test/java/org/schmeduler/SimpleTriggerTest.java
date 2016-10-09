package org.schmeduler;

import org.junit.Assert;
import org.junit.Test;
import org.schmeduler.immutable.DefaultTimeAdapter;
import org.schmeduler.immutable.SimpleTrigger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.*;
import static org.schmeduler.Utils.*;

public class SimpleTriggerTest {

    @Test
    public void shouldHandleNowAsStartingTime() {
        DefaultTimeAdapter adapter = new DefaultTimeAdapter();
        Date now = adapter.now();
        SimpleTrigger<Date, Long> trigger = new SimpleTrigger<>(adapter.asSeconds(5L), now, empty(), adapter);

        Optional<Date> nextFireTime = trigger.getNextFireTime(now);
        Assert.assertEquals(Optional.of(now), nextFireTime);
    }
    @Test
    public void shouldHandleCaseWhenNowEqualsNextStep() {
        DefaultTimeAdapter adapter = new DefaultTimeAdapter();
        Date now = adapter.now();
        Date then = new Date(now.getTime() - adapter.asSeconds(5L));
        SimpleTrigger<Date, Long> trigger = new SimpleTrigger<>(adapter.asSeconds(5L), then, empty(), adapter);

        Optional<Date> nextFireTime = trigger.getNextFireTime(now);
        Assert.assertEquals(Optional.of(now), nextFireTime);
    }

    @Test
    public void shouldTriggerInMultiplesOfStartingTime() {
        IntegerTimeAdapter adapter = new IntegerTimeAdapter();
        adapter.setNow(113);
        Integer start = 100;
        List<Integer> nextFireTimes = new SimpleTrigger<>(5, start, empty(), adapter)
                .fireTimesAsStream(adapter.now()).limit(5).collect(Collectors.toList());
        Assert.assertEquals(
                Arrays.asList(115,120,125,130,135), nextFireTimes);
    }

    @Test
    public void shouldTriggerAtStartingFirstTime() {
        IntegerTimeAdapter adapter = new IntegerTimeAdapter();
        Integer now = adapter.now();
        Integer start = adapter.asSeconds(10);
        Integer every = adapter.asSeconds(5);
        List<Integer> times = new SimpleTrigger<>(every, start, empty(), adapter)
                .fireTimesAsStream(now)
                .limit(5).collect(Collectors.toList());
        Assert.assertArrayEquals(
                Arrays.asList(10000,15000,20000,25000,30000).toArray(),
                times.toArray());
    }
    @Test
    public void shouldTriggerEveryFiveSeconds() {
        IntegerTimeAdapter adapter = new IntegerTimeAdapter();
        Integer now = adapter.now();
        Integer every = adapter.asSeconds(5);
        List<Integer> times = new SimpleTrigger<>(every, now, empty(), adapter)
                .fireTimesAsStream(now)
                .limit(5).collect(Collectors.toList());
        Assert.assertArrayEquals(
                Arrays.asList(0,5000,10000,15000,20000).toArray(),
                times.toArray());
    }

    @Test
    public void shouldTriggerOnlyFiveTimes() {
        IntegerTimeAdapter adapter = new IntegerTimeAdapter();
        Integer now = adapter.now();
        Integer every = 5;
        Integer until = 25;
        List<Integer> times = new SimpleTrigger<>(every, now, of(until), adapter)
                .fireTimesAsStream(now).collect(Collectors.toList());
        Assert.assertArrayEquals(
                Arrays.asList(0,5,10,15,20).toArray(),
                times.toArray());

    }
    @Test
    public void shouldTriggerAtEndingWhenEndingIsInclusive() {
        IntegerTimeAdapter adapter = new IntegerTimeAdapter();
        Integer now = adapter.now();
        Integer every = 5;
        Integer until = 25;
        List<Integer> times = new SimpleTrigger<>(every, now, of(until), adapter, true)
                .fireTimesAsStream(now).collect(Collectors.toList());
        Assert.assertArrayEquals(
                Arrays.asList(0,5,10,15,20,25).toArray(),
                times.toArray());
    }

}
