package util;


import java.util.ArrayList;


public class Polygon {

    public int vertsNum;
    public double[] vertsX, vertsY;

    public ArrayList<ConvexPoly> subPolys = new ArrayList<>();

    public double minX, minY, maxX, maxY;
    public double avgX, avgY;

    public Polygon(double[] vertsX, double[] vertsY, int vertsNum) {
        this.vertsX = vertsX;
        this.vertsY = vertsY;
        this.vertsNum = vertsNum;

        // workaround, only still works with convex polygons
        subPolys.add(new ConvexPoly(vertsX, vertsY, vertsNum));

        calcBoundingBox();
        calcCenter();
    }

    public void move(double dx, double dy) {
        for (ConvexPoly poly : subPolys) {
            poly.move(dx, dy);
        }
        minX += dx; minY += dy;
        maxX += dx; maxY += dy;
        avgX += dx; avgY += dy;
    }

    private void calcBoundingBox() {
        minX = maxX = subPolys.get(0).minX;
        minY = maxY = subPolys.get(0).minY;
        for (ConvexPoly poly : subPolys) {
            if (poly.minX < minX) minX = poly.minX;
            if (poly.maxX > maxX) maxX = poly.maxX;
            if (poly.minY < minY) minY = poly.minY;
            if (poly.maxY > maxY) maxY = poly.maxY;
        }

    }

    public void rotate(double angle) {
        for (ConvexPoly poly : subPolys) {
            poly.rotate(angle, avgX, avgY);
        }
        calcBoundingBox();
    }

    private void calcCenter() {
        double sumX = 0;
        double sumY = 0;
        for (ConvexPoly poly : subPolys) {
            sumX += poly.avgX;
            sumY += poly.avgY;
        }
        avgX = sumX / subPolys.size();
        avgY = sumY / subPolys.size();
    }
}
