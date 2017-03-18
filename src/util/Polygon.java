package util;


import java.util.ArrayList;


public class Polygon {

    public int vertsNum;

    public double[] vertsX, vertsY;

    public Vector2D[] edges;

    private int clockwise;

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

    private void calcEdges() {
        for (int i = 0; i < vertsNum; i++) {
            int t = (i + 1) % vertsNum;
            edges[i] = new Vector2D(vertsX[t] - vertsX[i], vertsY[t] - vertsY[i]);
        }
    }

    public Vector2D[] getNormals() {
        Vector2D[] normals = new Vector2D[vertsNum];
        for (int i = 0; i < vertsNum; i++) {
            normals[i] = edges[i].getPerp();
        }
        return normals;
    }

    private void calcClockwise() {

        int lowest = 0;
        for (int i = 0; i < vertsNum; i++) {
            if (vertsY[i] > vertsY[lowest])
                lowest = i;
            else if (vertsY[i] == vertsY[lowest])
                lowest = (vertsX[lowest] > vertsX[i] ? lowest : i);
        }

        double z = edges[lowest].cross(edges[(lowest + 1) % vertsNum]);

        clockwise = (z > 0 ? 1 : -1);
    }

    public boolean isConvex() {
        return (getConcaveVertices().size() == 0);
    }

    public ArrayList<Integer> getConcaveVertices() {

        ArrayList<Integer> concave = new ArrayList<>();

        for (int i = 1; i < vertsNum; i++) {
            double z = edges[i].cross(edges[(i + 1) % vertsNum]);
            z *= clockwise;

            if (z < 0)
                concave.add((i + 1) % vertsNum);
        }
        return concave;
    }
}
