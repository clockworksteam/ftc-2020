package org.firstinspires.ftc.clockworks.algorithm.motion.strategy;

import org.firstinspires.ftc.clockworks.algorithm.PID;
import org.firstinspires.ftc.clockworks.algorithm.motion.Point;
import org.firstinspires.ftc.clockworks.algorithm.motion.strategy.MotionStrategy;
import org.firstinspires.ftc.clockworks.control.PositionController;

public class StoppingStrategy implements MotionStrategy {
    private Point activeTarget;
    
    private PID compensator = new PID(1, 0, 0);
    
    @Override
    public void begin(Point lastTarget, Point activeTarget) {
        this.activeTarget = activeTarget;
        compensator.reset(System.currentTimeMillis() / 1000.0);
    }

    @Override
    public void run(Point lastKnownPosition, PositionController ctrl) {
        double distance = Math.sqrt(Point.distanceSquared(lastKnownPosition, activeTarget));
        double angle = Math.atan2(activeTarget.getY() - lastKnownPosition.getY(), activeTarget.getX() - lastKnownPosition.getX());
        double correction = compensator.feed(distance, System.currentTimeMillis() / 1000.0);
        ctrl.setDirection(angle, correction);
    }
}
