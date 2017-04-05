package collision;

import util.Polygon;
import util.Vector2D;

import java.util.ArrayList;

public class Partitioner {

    private Polygon poly;

    public Vector2D[] partitionEdges;
    public Vector2D[] partitionNormals;

    public int[] concaveVerts;
    public int[] partitionMap;

    public Partitioner(Polygon polygon) {
        this.poly = polygon;
    }

    public void partitionPolygon() {
        this.calcConcaveVertices();
        this.calcPartitions();
        this.calcPartitionNormals();
    }

    private void calcConcaveVertices() {
        ArrayList<Integer> conc = new ArrayList<>();
        for (int i = 1; i < poly.vertsNum; i++) {
            double z = poly.edges[i].cross(poly.edges[(i + 1) % poly.vertsNum]);
            z *= poly.clockwise;
            if (z < 0)
                conc.add((i + 1) % poly.vertsNum);
        }
        concaveVerts = new int[conc.size()];
        for (int i = 0; i < conc.size(); i++) {
            concaveVerts[i] = conc.get(i);
        }
    }

    private void calcPartitions() {
        ArrayList<Vector2D> partEdges = new ArrayList<>();
        ArrayList<Integer> edgeMap = new ArrayList<>();

        // TODO: No idea if this algorithm works for all cases lmao don't think it does...
        for (int vert : this.concaveVerts) {
            // cause java doesn't like modulo lmao
            Vector2D startEdge = poly.edges[(((vert - 1) % poly.vertsNum) + poly.vertsNum) % poly.vertsNum];
            Vector2D partEdge = poly.edges[vert];
            int i = vert;
            while (poly.clockwise * startEdge.cross(partEdge) < 0) {
                i = (i + 1) % poly.vertsNum;
                partEdge.add(poly.edges[i]);
            }
            edgeMap.add(i);
            partEdges.add(partEdge);
        }

        partitionEdges = new Vector2D[partEdges.size()];
        partitionMap = new int[partitionEdges.length];
        for (int i = 0; i < partEdges.size(); i++) {
            partitionEdges[i] = partEdges.get(i);
            partitionMap[i] = edgeMap.get(i);
        }
    }

    private void calcPartitionNormals() {
        partitionNormals = new Vector2D[partitionEdges.length];
        for (int i = 0; i < partitionEdges.length; i++) {
            partitionNormals[i] = partitionEdges[i].getPerp();
        }
    }
}
