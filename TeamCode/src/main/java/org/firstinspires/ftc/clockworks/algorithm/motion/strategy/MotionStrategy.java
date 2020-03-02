package org.firstinspires.ftc.clockworks.algorithm.motion.strategy;

import org.firstinspires.ftc.clockworks.algorithm.motion.Point;
import org.firstinspires.ftc.clockworks.control.PositionController;

public interface MotionStrategy {
    void begin(Point lastTarget, Point activeTarget);
    void run(Point lastKnownPosition, PositionController ctrl);
}
