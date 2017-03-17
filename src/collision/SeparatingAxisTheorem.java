package collision;

import util.Polygon;
import util.Vector2D;


public class SeparatingAxisTheorem {

    public static boolean isCollide(Polygon polyA, Polygon polyB) {

        // Broad Phase Check
        if (!isAABBOverlap(polyA, polyB)) {
            return false;
        }

        // Narrow Phase Check

        Polygon poly;

        Vector2D p1 = new Vector2D();
        Vector2D p2 = new Vector2D();

        Vector2D[] axes;
        Vector2D[] axesA = new Vector2D[polyA.vertsNum];
        Vector2D[] axesB = new Vector2D[polyB.vertsNum];

        for (int k = 0; k < 2; k++) {
            if (k == 0) poly = polyA;
            else poly = polyB;

            for (int i = 0; i < poly.vertsNum; i++) {

                p1.set(poly.vertsX[i], poly.vertsY[i]);

                int t = (i + 1 == poly.vertsNum ? 0 : i + 1);
                p2.set(poly.vertsX[t], poly.vertsY[t]);

                Vector2D edge = p1.getSubtracted(p2);
                Vector2D normal = edge.getPerp();

                (k == 0 ? axesA : axesB)[i] = normal;
            }
        }

        for (int k = 0; k < 2; k++) {
            if (k == 0) axes = axesA;
            else axes = axesB;

            for (int i = 0; i < axes.length; i++) {

                double minA = axes[i].dot(polyA.vertsX[0], polyA.vertsY[0]);
                double maxA = minA;

                for (int j = 1; j < polyA.vertsNum; j++) {

                    double p = axes[i].dot(polyA.vertsX[j], polyA.vertsY[j]);

                    if (p < minA) minA = p;
                    else if (p > maxA) maxA = p;
                }

                double minB = axes[i].dot(polyB.vertsX[0], polyB.vertsY[0]);
                double maxB = minB;

                for (int j = 1; j < polyB.vertsNum; j++) {

                    double p = axes[i].dot(polyB.vertsX[j], polyB.vertsY[j]);

                    if (p < minB) minB = p;
                    else if (p > maxB) maxB = p;
                }

                if (!is1DOverlap(minA, maxA, minB, maxB)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isAABBOverlap(Polygon polyA, Polygon polyB) {

        return !(polyB.minX > polyA.maxX
                || polyB.maxX < polyA.minX
                || polyB.minY > polyA.maxY
                || polyB.maxY < polyA.minY);
    }

    public static boolean is1DOverlap(double leftX, double rightX, double leftY, double rightY) {
        return leftX <= rightY && rightX >= leftY;
    }
}
