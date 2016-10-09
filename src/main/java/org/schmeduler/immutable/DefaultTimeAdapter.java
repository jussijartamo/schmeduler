package org.schmeduler.immutable;

import java.util.Calendar;
import java.util.Date;

public class DefaultTimeAdapter implements TimeAdapter<Date, Long> {

    @Override
    public Date now() {
        return new Date();
    }

    @Override
    public Date untilNextEvent(Long step, Date startingAt, Date currentlyAt) {
        long between = currentlyAt.getTime() - startingAt.getTime();
        Long untilNextEvent = step - (between % step);
        if(step.equals(untilNextEvent)) {
            return currentlyAt;
        } else {
            return new Date(currentlyAt.getTime() + untilNextEvent);
        }
    }


    //@Override
    public int getDayOfTheWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public Long duration(Date first, Date second) {
        return second.getTime() - first.getTime();
    }

    //@Override
    public Long modulo(Long dividend, Long divisor) {
        return dividend % divisor;
    }

    @Override
    public Date plus(Date date, Long aLong) {
        return new Date(date.getTime() + aLong);
    }

    @Override
    public boolean isBefore(Date first, Date second) {
        return first.before(second);
    }

    @Override
    public Long asSeconds(Long milliseconds) {
        return milliseconds*1000L;
    }

    @Override
    public Long asMinutes(Long milliseconds) {
        return asSeconds(milliseconds) * 60;
    }

    @Override
    public Long asHours(Long milliseconds) {
        return asMinutes(asSeconds(milliseconds)) * 60;
    }

    @Override
    public Long asDays(Long milliseconds) {
        return asHours(asMinutes(asSeconds(milliseconds))) * 24;
    }
}
