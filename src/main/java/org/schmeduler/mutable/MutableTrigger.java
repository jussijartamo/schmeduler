package org.schmeduler.mutable;

import org.schmeduler.Trigger;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class MutableTrigger<TIME> implements Trigger<TIME>, TriggerSwitch<TIME> {
    private final AtomicReference<Trigger<TIME>> composite;
    private final MutableSchmeduler parent;

    public MutableTrigger(MutableSchmeduler parent) {
        this(parent, null);
    }
    public MutableTrigger(MutableSchmeduler parent, Trigger<TIME> trigger) {
        this.composite = new AtomicReference<>(trigger);
        this.parent = parent;
    }
    @Override
    public Optional<TIME> getFireTimeAfter(TIME after) {
        Optional<Trigger<TIME>> trigger = Optional.ofNullable(composite.get());
        return trigger.flatMap(t -> t.getFireTimeAfter(after));
    }

    @Override
    public Optional<TIME> getNextFireTime(TIME startingAt) {
        Optional<Trigger<TIME>> trigger = Optional.ofNullable(composite.get());
        return trigger.flatMap(t -> t.getNextFireTime(startingAt));
    }

    @Override
    public MutableTrigger<TIME> setTrigger(Trigger<TIME> newTrigger) {
        composite.set(newTrigger);
        parent.updated();
        return this;
    }
}
