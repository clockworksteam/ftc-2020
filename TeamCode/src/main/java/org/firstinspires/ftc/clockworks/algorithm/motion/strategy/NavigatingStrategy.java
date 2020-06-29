package org.firstinspires.ftc.clockworks.algorithm.motion.strategy;

import org.firstinspires.ftc.clockworks.algorithm.PID;
import org.firstinspires.ftc.clockworks.algorithm.motion.Point;
import org.firstinspires.ftc.clockworks.control.PositionController;

public class NavigatingStrategy implements MotionStrategy {
    private Point lastTarget;
    private Point activeTarget;

    private PID compensator = new PID(1, 0, 0);

    @Override
    public void begin(Point lastTarget, Point activeTarget) {
        this.lastTarget = lastTarget;
        this.activeTarget = activeTarget;
        compensator.reset(System.currentTimeMillis() / 1000.0);
    }

    @Override
    public void run(Point lastKnownPosition, PositionController ctrl) {
        double lineLen = Math.sqrt(Point.distanceSquared(lastTarget, activeTarget));
        double fact = (activeTarget.getY() - lastTarget.getY()) * lastKnownPosition.getX() -
                      (activeTarget.getX() - lastTarget.getX()) * lastKnownPosition.getY() +
                      activeTarget.getX() * lastTarget.getY() - activeTarget.getY() * lastTarget.getX();
        double distance = Math.abs(fact) / lineLen;
        double correction = compensator.feed(distance, System.currentTimeMillis() / 1000.0);
        correction =  Math.min(1, Math.abs(correction));

        double tangentAplifier = Math.sqrt(1 - correction * correction);

        double intersectX = lastTarget.getX() + (activeTarget.getX() - lastTarget.getX()) * (distance / lineLen);
        double intersectY = lastTarget.getY() + (activeTarget.getY() - lastTarget.getY()) * (distance / lineLen);

        double tangentVecX = activeTarget.getX() - lastTarget.getX();
        double tangentVecY = activeTarget.getY() - lastTarget.getY();
        double normalVecX = intersectX - lastKnownPosition.getX();
        double normalVecY = intersectY - lastKnownPosition.getY();

        double tangentLen = Math.sqrt(tangentVecX * tangentVecX + tangentVecY * tangentVecY);
        double normalLen = Math.sqrt(normalVecX * normalVecX + normalVecY * normalVecY);
        tangentVecX /= tangentLen / tangentAplifier;
        tangentVecY /= tangentLen / tangentAplifier;
        normalVecX /= normalLen;
        normalVecY /= normalLen;

        double angle = Math.atan2(tangentVecY + normalVecY, tangentVecX + normalVecX);

        ctrl.setDirection(angle, 1);

    }
}
