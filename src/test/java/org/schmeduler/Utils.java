package org.schmeduler;

import org.schmeduler.immutable.SingleJobScheduler;
import org.schmeduler.immutable.TimeAdapter;

import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class Utils {

    public static class Job implements Runnable {
        private final AtomicInteger done = new AtomicInteger();

        @Override
        public void run() {
            done.incrementAndGet();
        }

        public boolean isActivated() {
            return done.get() > 0;
        }

        public int getNumberOfTimesExecuted() {
            return done.get();
        }
    }

    public static class JobExecutor implements BiConsumer<Integer, Runnable> {

        @Override
        public void accept(Integer integer, Runnable executeJob) {
            executeJob.run();
        }
    }

    public static class IntegerSchedulerExecutor implements BiConsumer<Integer, Runnable> {
        private final TreeMap<Integer, Runnable> jobs = new TreeMap<>();

        @Override
        public void accept(Integer key, Runnable sideEffect) {
            if(jobs.containsKey(key)) {
                throw new IllegalStateException("Scheduler shouldn't schedule next event twice!");
            }
            jobs.put(key, sideEffect);
        }
        public int nextAt() {
            return jobs.navigableKeySet().first();
        }
        public int count() {
            return jobs.size();
        }

        public void activateNext() {
            if(!jobs.isEmpty()) {
                Integer first = jobs.navigableKeySet().first();
                jobs.remove(first).run();
            }
        }
    }

    public static class IntegerTimeAdapter implements TimeAdapter<Integer,Integer> {
        private Integer now = 0;

        public void setNow(Integer now) {
            this.now = now;
        }

        @Override
        public Integer now() {
            return now;
        }

        @Override
        public Integer untilNextEvent(Integer step, Integer startingAt, Integer currentlyAt) {
            int between = currentlyAt - startingAt;
            int untilNextEvent = step - (between % step);
            return currentlyAt + untilNextEvent;
        }

        @Override
        public Integer plus(Integer integer, Integer integer2) {
            return integer + integer2;
        }

        @Override
        public Integer duration(Integer first, Integer second) {
            return second - first;
        }

        @Override
        public boolean isBefore(Integer first, Integer second) {
            return first < second;
        }

        @Override
        public Integer asSeconds(Integer integer) {
            return integer * 1000;
        }

        @Override
        public Integer asMinutes(Integer integer) {
            return integer * 1000 * 60;
        }

        @Override
        public Integer asHours(Integer integer) {
            return integer * 1000 * 60 * 60;
        }

        @Override
        public Integer asDays(Integer integer) {
            return integer * 1000 * 60 * 60 * 24;
        }
    }
}
