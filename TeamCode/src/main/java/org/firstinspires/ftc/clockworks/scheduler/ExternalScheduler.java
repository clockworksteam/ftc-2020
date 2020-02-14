package org.firstinspires.ftc.clockworks.scheduler;

public interface ExternalScheduler {
    void register(Fiber fiber);
    void startFibers();
    void stopFibers();
}
