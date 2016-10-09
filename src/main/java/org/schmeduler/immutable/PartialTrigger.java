package org.schmeduler.immutable;

import org.schmeduler.common.AbstractPartialTrigger;

import java.util.Optional;

import static java.util.Optional.*;

public class PartialTrigger<T, D> extends AbstractPartialTrigger<T, D, SimpleTrigger<T,D>>{

    public PartialTrigger(D duration, TimeAdapter<T, D> adapter) {
        super(duration, adapter);
    }

    @Override
    protected SimpleTrigger<T, D> newTrigger(D newDuration, TimeAdapter<T, D> adapter) {
        return new SimpleTrigger<>(newDuration, adapter.now(), empty(), adapter);
    }

}
