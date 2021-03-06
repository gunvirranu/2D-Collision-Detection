package collision;

import util.ConvexPoly;
import util.Polygon;
import util.Vector2D;


public class SeparatingAxisTheorem {

    public static boolean isCollide(Polygon polyA, Polygon polyB) {

        for (ConvexPoly subPolyA : polyA.subPolys) {
            for (ConvexPoly subPolyB : polyB.subPolys) {
                if (isConvexCollide(subPolyA, subPolyB))
                    return true;
            }
        }
        return false;
    }

    private static boolean isConvexCollide(ConvexPoly polyA, ConvexPoly polyB) {

        // Broad Phase Check
        if (!isAABBOverlap(polyA, polyB))
            return false;

        // Narrow Phase Check
        Vector2D[] axesA = polyA.normals;
        Vector2D[] axesB = polyB.normals;

        return (isOverlapSingleAxis(polyA, polyB, axesA) &&
                isOverlapSingleAxis(polyA, polyB, axesB));
    }

    private static boolean isOverlapSingleAxis(ConvexPoly polyA, ConvexPoly polyB, Vector2D[] axes) {

        for (Vector2D axis : axes) {

            double[] tmp = getAxisPolyRange(polyA, axis);
            double minA = tmp[0];
            double maxA = tmp[1];

            tmp = getAxisPolyRange(polyB, axis);
            double minB = tmp[0];
            double maxB = tmp[1];

            if (!is1DOverlap(minA, maxA, minB, maxB))
                return false;
        }

        return true;
    }

    private static double[] getAxisPolyRange(ConvexPoly poly, Vector2D axis) {

        double min = axis.dot(poly.vertsX[0], poly.vertsY[0]);
        double max = min;

        for (int j = 1; j < poly.vertsNum; j++) {

            double p = axis.dot(poly.vertsX[j], poly.vertsY[j]);
            if (p < min)
                min = p;
            else if (p > max)
                max = p;
        }

        return new double[]{min, max};
    }

    private static boolean isAABBOverlap(ConvexPoly polyA, ConvexPoly polyB) {
        return !(polyB.minX > polyA.maxX
                || polyB.maxX < polyA.minX
                || polyB.minY > polyA.maxY
                || polyB.maxY < polyA.minY);
    }

    private static boolean is1DOverlap(double leftX, double rightX, double leftY, double rightY) {
        return leftX <= rightY && rightX >= leftY;
    }
}
