package org.firstinspires.ftc.clockworks.algorithm.motion;

public interface Trace {
	double positionX(int alpha);
	double positionY(int alpha);
	double alphaTangentFrom(int x, int y);
	double getExitRange();
	boolean exitEarly();
}
