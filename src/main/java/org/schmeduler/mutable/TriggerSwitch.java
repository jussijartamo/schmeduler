package org.schmeduler.mutable;

import org.schmeduler.Trigger;

public interface TriggerSwitch<TIME> {

    MutableTrigger<TIME> setTrigger(Trigger<TIME> newTrigger);
}
