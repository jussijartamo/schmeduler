package org.schmeduler.mutable;

public interface SchmedulerBuilder {

    static DefaultMutableScheduler newScheduler() {
        return DefaultMutableScheduler.newScheduler();
    }

}
