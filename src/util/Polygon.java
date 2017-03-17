package util;

import java.util.Arrays;


public class Polygon {

    public double[] vertsX, vertsY;

    public int vertsNum;

    public double minX, minY, maxX, maxY;
    public double avgX, avgY;

    public Polygon(double[] vertsX, double[] vertsY, int vertsNum) {
        this.vertsX = vertsX;
        this.vertsY = vertsY;
        this.vertsNum = vertsNum;

        calcBoundingBox();
        calcCenter();
    }

    public void move(double dx, double dy) {

        for (int i = 0; i < vertsNum; i++) {
            vertsX[i] += dx;
            vertsY[i] += dy;
        }
        minX += dx;
        minY += dy;
        maxX += dx;
        maxY += dy;
        avgX += dx;
        avgY += dy;
    }

    public void rotate(double angle) {

        double s = Math.sin(angle);
        double c = Math.cos(angle);

        for (int i = 0; i < vertsNum; i++) {
            double newX = c * (vertsX[i] - avgX) - s * (vertsY[i] - avgY) + avgX;
            vertsY[i] = s * (vertsX[i] - avgX) + c * (vertsY[i] - avgY) + avgY;
            vertsX[i] = newX;
        }

        calcBoundingBox();
    }

    private void calcBoundingBox() {
        minX = Arrays.stream(vertsX).min().getAsDouble();
        maxX = Arrays.stream(vertsX).max().getAsDouble();
        minY = Arrays.stream(vertsY).min().getAsDouble();
        maxY = Arrays.stream(vertsY).max().getAsDouble();
    }

    private void calcCenter() {
        avgX = Arrays.stream(vertsX).average().getAsDouble();
        avgY = Arrays.stream(vertsY).average().getAsDouble();
    }
}
