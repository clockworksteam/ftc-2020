package org.firstinspires.ftc.clockworks.algorithm.motion;

public class BezierTrace implements Trace {
	private final static double SEGMENT_SIZE = 100; // 10 cm

	private final double exitRange;
	private final boolean exitEarly;

	public BezierTrace(double[] x, double[] y, Double[] heading, double exitRange, boolean exitEarly) {
		this.exitRange = exitRange;
		this.exitEarly = exitEarly;
		double estLen = estimateLength(x, y);

	}

	@Override
	public double positionX(int alpha) {
		return 0;
	}

	@Override
	public double positionY(int alpha) {
		return 0;
	}

	@Override
	public double getExitRange() {
		return exitRange;
	}

	@Override
	public boolean exitEarly() {
		return exitEarly;
	}

	private static double estimateLength(double[] x, double[] y) {
		double[] srcX = new double[x.length];
		double[] srcY = new double[y.length];
		double[] midX = new double[x.length];
		double[] midY = new double[y.length];
		double length = 0;
		double lastX = x[0], lastY = y[0];
		for (double alpha = 0; alpha <=  1; alpha += 1.0/300) {
			System.arraycopy(x, 0, srcX, 0, x.length);
			System.arraycopy(y, 0, srcY, 0, y.length);
			for (int size = x.length - 1; size > 0; size--) {
				for (int i = 0; i < size; i++) {
					midX[i] = srcX[i] + (srcX[i + 1] - srcX[i]) * alpha;
					midY[i] = srcY[i] + (srcY[i + 1] - srcY[i]) * alpha;
				}
				double tmp[];
				tmp = srcX;
				srcX = midX;
				midX = tmp;
				tmp = srcY;
				srcY = midY;
				midY = tmp;
			}
			length += Math.sqrt((lastX - srcX[0]) * (lastX - srcX[0]) + (lastY - srcY[0]) + (lastY - srcY[0]));
			lastX = srcX[0];
			lastY = srcY[0];
		}
		return length;
	}

}
