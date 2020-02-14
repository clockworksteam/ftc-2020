package org.firstinspires.ftc.clockworks.scheduler;

public interface InternalScheduler {
    void unregister(Fiber fiber);
    void register(Fiber fiber);
}
