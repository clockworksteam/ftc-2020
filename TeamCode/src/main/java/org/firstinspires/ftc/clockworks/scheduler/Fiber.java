package org.firstinspires.ftc.clockworks.scheduler;

public interface Fiber {

    /**
     * Initializes the Fiber object
     * @param scheduler the scheduler API
     */
    void init(InternalScheduler scheduler);

    /**
     * Executes an iteration of the Fiber object
     */
    void tick();

    /**
     * Deinitializes a Fiber object
     */
    void deinit();
}
