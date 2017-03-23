package collision;

import util.ConvexPoly;
import util.Polygon;
import util.Vector2D;

import java.util.ArrayList;

public class Partitioner {

    private Polygon polygon;
    // TODO: hacky non-convex ConvexPoly object for calculations
    private ArrayList<ConvexPoly> subPolys = new ArrayList<>();
    private ConvexPoly nonConvexConvex;

    private Vector2D[] partitionEdges;

    public Partitioner(Polygon polygon) {
        this.polygon = polygon;
        this.nonConvexConvex = new ConvexPoly(polygon.vertsX, polygon.vertsY, polygon.vertsNum);
    }

    public ArrayList<ConvexPoly> partitionPolygon() {
        subPolys.add(new ConvexPoly(polygon.vertsX, polygon.vertsY, polygon.vertsNum));
        return subPolys;
    }

    private ArrayList<Integer> getConcaveVertices() {
        ArrayList<Integer> concave = new ArrayList<>();
        for (int i = 1; i < polygon.vertsNum; i++) {
            double z = nonConvexConvex.edges[i].cross(nonConvexConvex.edges[(i + 1) % nonConvexConvex.vertsNum]);
            z *= nonConvexConvex.clockwise;
            if (z < 0)
                concave.add((i + 1) % nonConvexConvex.vertsNum);
        }
        return concave;
    }


    private void calcPartitionEdges() {
        ArrayList<Vector2D> partEdges = new ArrayList<>();
        for (int vert : getConcaveVertices()) {
            // cause java doesn't like modulo lmao
            Vector2D startEdge = nonConvexConvex.edges[(((vert - 1) % nonConvexConvex.vertsNum) + nonConvexConvex.vertsNum) % nonConvexConvex.vertsNum];
            Vector2D partEdge = nonConvexConvex.edges[vert];
            int i = vert;
            while (nonConvexConvex.clockwise * startEdge.cross(partEdge) < 0) {
                i = (i + 1) % nonConvexConvex.vertsNum;
                partEdge.add(nonConvexConvex.edges[i]);
            }
            partEdges.add(partEdge);
        }
        partitionEdges = partEdges.toArray(new Vector2D[0]);
    }

    public Vector2D[] getPartitionNormals() {
        Vector2D[] partNormals = new Vector2D[partitionEdges.length];
        for (int i = 0; i < partitionEdges.length; i++) {
            partNormals[i] = partitionEdges[i].getPerp();
        }
        return partNormals;
    }
}
