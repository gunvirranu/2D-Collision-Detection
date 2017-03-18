package util;


public class Polygon {

    public double[] vertsX, vertsY;

    public Vector2D[] edges;

    public int vertsNum;

    public double minX, minY, maxX, maxY;
    public double avgX, avgY;

    public Polygon(double[] vertsX, double[] vertsY, int vertsNum) {
        this.vertsX = vertsX;
        this.vertsY = vertsY;
        this.vertsNum = vertsNum;
        this.edges = new Vector2D[vertsNum];

        calcBoundingBox();
        calcCenter();
        calcEdges();
    }

    public void move(double dx, double dy) {

        for (int i = 0; i < vertsNum; i++) {
            vertsX[i] += dx;
            vertsY[i] += dy;
        }
        minX += dx; minY += dy;
        maxX += dx; maxY += dy;
        avgX += dx; avgY += dy;
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
        calcEdges();
    }

    private void calcBoundingBox() {
        minX = maxX = vertsX[0];
        minY = maxY = vertsY[0];
        for (int i = 0; i < vertsNum; i++) {
            if (vertsX[i] < minX) minX = vertsX[i];
            if (vertsX[i] > maxX) maxX = vertsX[i];
            if (vertsY[i] < minY) minY = vertsY[i];
            if (vertsY[i] > maxY) maxY = vertsY[i];
        }
    }

    private void calcCenter() {
        double sumX = 0;
        double sumY = 0;
        for (int i = 0; i < vertsNum; i++) {
            sumX += vertsX[i];
            sumY += vertsY[i];
        }
        avgX = sumX / vertsNum;
        avgY = sumY / vertsNum;
    }

    public void calcEdges() {

        for (int i = 0; i < vertsNum; i++) {
            int t = (i + 1 == vertsNum ? 0 : i + 1);

            Vector2D edge = new Vector2D(vertsX[t] - vertsX[i], vertsY[t] - vertsY[i]);
            edges[i] = edge;
        }
    }

    public Vector2D[] getNormals() {

        Vector2D[] normals = new Vector2D[vertsNum];

        for (int i = 0; i < vertsNum; i++) {
            normals[i] = edges[i].getPerp();
        }

        return normals;
    }

    public boolean isConvex() {

        double z = edges[0].cross(edges[1]);
        int clockwise = (z >= 0 ? 1 : -1);

        for (int i = 1; i < vertsNum; i++) {
            int t = (i + 1 == vertsNum ? 0 : i + 1);

            z = edges[i].cross(edges[t]) * clockwise;

            if (z < 0) return false;
        }

        return true;
    }
}
