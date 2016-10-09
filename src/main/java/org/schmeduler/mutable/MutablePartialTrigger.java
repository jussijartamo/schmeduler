package org.schmeduler.mutable;

import org.schmeduler.Trigger;
import org.schmeduler.common.AbstractPartialTrigger;
import org.schmeduler.immutable.SimpleTrigger;
import org.schmeduler.immutable.TimeAdapter;

import static java.util.Optional.empty;

public class MutablePartialTrigger<T, D> extends AbstractPartialTrigger<T, D, MutableTrigger<T>> {
    private final MutableSchmeduler parent;

    public MutablePartialTrigger(D duration, TimeAdapter<T, D> adapter, MutableSchmeduler parent) {
        super(duration, adapter);
            this.parent = parent;
    }

    @Override
    protected MutableTrigger<T> newTrigger(D newDuration, TimeAdapter<T, D> adapter) {
        Trigger<T> trigger = new SimpleTrigger<>(newDuration, adapter.now(), empty(), adapter);
        return new MutableTrigger<T>(parent, trigger);
    }

}
