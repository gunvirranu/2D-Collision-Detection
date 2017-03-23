package util;


public class ConvexPoly {

    public int vertsNum;
    public double[] vertsX, vertsY;

    public Vector2D[] edges;
    public Vector2D[] normals;

    public int clockwise;
    public double minX, minY, maxX, maxY;
    public double avgX, avgY;

    public ConvexPoly(double[] vertsX, double[] vertsY, int vertsNum) {
        this.vertsX = vertsX;
        this.vertsY = vertsY;
        this.vertsNum = vertsNum;
        this.edges = new Vector2D[vertsNum];
        this.normals = new Vector2D[vertsNum];

        calcBoundingBox();
        calcCenter();
        calcEdges();
        calcClockwise();
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

    public void rotate(double angle, double x, double y) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        for (int i = 0; i < vertsNum; i++) {
            double newX = c * (vertsX[i] - x) - s * (vertsY[i] - y) + x;
            vertsY[i] = s * (vertsX[i] - x) + c * (vertsY[i] - y) + y;
            vertsX[i] = newX;
        }

        calcBoundingBox();
        calcEdges();
    }

    private void calcBoundingBox() {
        if (vertsNum > 0) {
            minX = maxX = vertsX[0];
            minY = maxY = vertsY[0];
        }
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

    private void calcEdges() {
        for (int i = 0; i < vertsNum; i++) {
            int t = (i + 1) % vertsNum;
            edges[i] = new Vector2D(vertsX[t] - vertsX[i], vertsY[t] - vertsY[i]);
        }
        calcNormals();
    }

    private void calcNormals() {
        for (int i = 0; i < vertsNum; i++) {
            normals[i] = edges[i].getPerp();
        }
    }

    private void calcClockwise() {
        int lowest = 0;
        for (int i = 0; i < vertsNum; i++) {
            if (vertsY[i] > vertsY[lowest])
                lowest = i;
            else if (vertsY[i] == vertsY[lowest])
                lowest = (vertsX[lowest] > vertsX[i] ? lowest : i);
        }
        if (vertsNum > 0) {
            double z = edges[(((lowest - 1) % vertsNum) + vertsNum) % vertsNum].cross(edges[lowest]);
            clockwise = (z > 0 ? 1 : -1);
        }
    }
}
