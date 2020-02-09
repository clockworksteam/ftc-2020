package org.firstinspires.ftc.clockworks.scheduler;

import java.util.ArrayList;

public class OnDemandScheduler extends Thread implements InternalScheduler, ExternalScheduler {
    private ArrayList<Fiber> fibers = new ArrayList<>();
    private ArrayList<Fiber> awaitingInit = new ArrayList<>();
    private ArrayList<Fiber> awaitingDeinit = new ArrayList<>();
    private volatile boolean running = false;

    private OnDemandScheduler() { }

    @Override
    public void register(Fiber fiber) {
        awaitingInit.add(fiber);
    }

    @Override
    public void startFibers() {
        running = true;
        this.start();
    }

    @Override
    public void stopFibers() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            for (Fiber fiber : awaitingInit) {
                fiber.init(this);
                fibers.add(fiber);
            }
            for (Fiber fiber : fibers) {
                fiber.tick();
            }
            for (Fiber fiber : awaitingDeinit) {
                fibers.remove(fiber);
                fiber.deinit();
            }
        }
    }

    @Override
    public void unregister(Fiber fiber) {
        awaitingDeinit.add(fiber);
    }

    public static ExternalScheduler create() {
        return new OnDemandScheduler();
    }
}
