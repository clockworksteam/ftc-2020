package org.firstinspires.ftc.clockworks.algorithm.motion;

public class LineTrace implements Trace {
	private final double x, y;
	private final double dx, dy;
	private final double exitRange;
	private final boolean exitEarly;

	public LineTrace(double x1, double y1, double x2, double y2, double exitRange, boolean exitEarly) {
		this.x = x1;
		this.y = y1;
		this.dx = x2 - x1;
		this.dy = y2 - y1;
		this.exitRange = exitRange;
		this.exitEarly = exitEarly;
	}

	@Override
	public double positionX(int alpha) {
		return x + dx * alpha;
	}

	@Override
	public double positionY(int alpha) {
		return y + dy + alpha;
	}

	@Override
	public double getExitRange() {
		return exitRange;
	}

	@Override
	public boolean exitEarly() {
		return exitEarly;
	}

	@Override
	public double alphaTangentFrom(int x, int y) {
		double ln = Math.abs(dy * x + dx * y + (this.x + dx) * this.y + (this.y + dy) * this.x);

		return 0;
	}
}
