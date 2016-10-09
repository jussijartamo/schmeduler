package org.schmeduler;

import org.junit.Assert;
import org.junit.Test;
import org.schmeduler.mutable.MutableScheduler;
import org.schmeduler.mutable.MutableSchmeduler;
import org.schmeduler.mutable.MutableTrigger;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.schmeduler.Utils.*;

public class SchedulerTest {

    @Test
    public void shouldExecuteAllJobsInCorrectOrder() {
        IntegerSchedulerExecutor schedulerExecutor = new IntegerSchedulerExecutor();
        IntegerTimeAdapter adapter = new IntegerTimeAdapter();
        JobExecutor executor = new JobExecutor();

        List<Job> firstJobs = Arrays.asList(new Job(), new Job(), new Job());
        List<Job> secondJobs = Arrays.asList(new Job(), new Job());

        final MutableSchmeduler<Integer,Integer,Runnable> schmeduler =
                new MutableScheduler<>(adapter, schedulerExecutor, executor);

        MutableTrigger<Integer> firstTrigger = schmeduler.every(10).seconds();
        MutableTrigger<Integer> secondTrigger = schmeduler.every(15).seconds();

        schmeduler.newJobs(firstTrigger, firstJobs);
        schmeduler.newJobs(secondTrigger, secondJobs);

        assertTrue("Job activated although scheduler not running!",
                firstJobs.stream().noneMatch(Job::isActivated) &&
                        secondJobs.stream().noneMatch(Job::isActivated));

        assertTrue("Scheduler executor should be inactive at this point!",
                schedulerExecutor.count() == 0);

        schmeduler.start();

        assertTrue("Scheduler executor should be active at this point!",
                schedulerExecutor.count() == 1);

        assertTrue("First activation is right now!",
                schedulerExecutor.nextAt() == 0);

        schedulerExecutor.activateNext();

        assertTrue("All jobs should be activated!",
                firstJobs.stream().allMatch(Job::isActivated) &&
                        secondJobs.stream().allMatch(Job::isActivated));

        assertTrue("First activation is 10 units from now!",
                schedulerExecutor.nextAt() == adapter.asSeconds(10));

        adapter.setNow(adapter.asSeconds(10));

        schedulerExecutor.activateNext();

        assertTrue("First jobs should be activated twice at this point!",
                firstJobs.stream().allMatch(j -> j.getNumberOfTimesExecuted() == 2) &&
                        secondJobs.stream().allMatch(j -> j.getNumberOfTimesExecuted() == 1));

    }

}
