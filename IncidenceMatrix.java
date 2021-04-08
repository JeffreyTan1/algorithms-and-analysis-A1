import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Incidence matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class IncidenceMatrix extends AbstractGraph {
    private int[][] incidenceMatrix;
    private int numEdge;
    private HashMap<String, Integer> rLabelIndexMap;
    private HashMap<Integer, String> rIndexLabelMap;
    private HashMap<String, Integer> cLabelIndexMap;
    // We use string instead of char because of the more powerful string library in
    // java
    private HashMap<Integer, String> cIndexLabelMapChar1;
    private HashMap<Integer, String> cIndexLabelMapChar2;

    /**
     * Contructs empty graph.
     */
    public IncidenceMatrix() {
        incidenceMatrix = new int[MAX_DIMEN][((int) Math.pow(MAX_DIMEN, 2)) - MAX_DIMEN];
        numEdge = 0;
        rLabelIndexMap = new HashMap<String, Integer>();
        rIndexLabelMap = new HashMap<Integer, String>();
        cLabelIndexMap = new HashMap<String, Integer>();
        cIndexLabelMapChar1 = new HashMap<Integer, String>();
        cIndexLabelMapChar2 = new HashMap<Integer, String>();
    } // end of IncidenceMatrix()

    public void addVertex(String vertLabel) {
        // set row for the vertex to be 0 because it has no edges
        for (int i = 0; i < numEdge; i++) {
            incidenceMatrix[numVertex][i] = 0;
        }
        rLabelIndexMap.put(vertLabel, numVertex);
        rIndexLabelMap.put(numVertex, vertLabel);
        labelStateMap.put(vertLabel, SIRState.S);
        numVertex++;
    } // end of addVertex()

    public void addEdge(String srcLabel, String tarLabel) {
        int srcIndex = (int) rLabelIndexMap.get(srcLabel);
        int tarIndex = (int) rLabelIndexMap.get(tarLabel);
        edgeFromTo(srcIndex, srcLabel, tarIndex, tarLabel);
    } // end of addEdge()

    private void edgeFromTo(int fromIndex, String fromLabel, int toIndex, String toLabel) {
        incidenceMatrix[fromIndex][numEdge] = 1;
        incidenceMatrix[toIndex][numEdge] = 1;
        for (int i = 0; i < numVertex; i++) {
            if (!(i == fromIndex || i == toIndex)) {
                incidenceMatrix[i][numEdge] = 0;
            }
        }
        cIndexLabelMapChar1.put(numEdge, fromLabel);
        cIndexLabelMapChar2.put(numEdge, toLabel);
        cLabelIndexMap.put(fromLabel + toLabel, numEdge);
        numEdge++;
    }

    public void deleteEdge(String srcLabel, String tarLabel) {
        for (int i = 0; i < numEdge; i++) {
            if ((String) cIndexLabelMapChar1.get(i) == srcLabel && (String) cIndexLabelMapChar2.get(i) == tarLabel) {
                deleteCol(i);
            }
            if ((String) cIndexLabelMapChar1.get(i) == tarLabel && (String) cIndexLabelMapChar2.get(i) == srcLabel) {
                deleteCol(i);
            }

        }
    } // end of deleteEdge()

    private void deleteCol(int colIndex) {
        for (int i = 0; i < numVertex - colIndex - 1; i++) {
            incidenceMatrix[colIndex + i] = incidenceMatrix[colIndex + i + 1];

            cIndexLabelMapChar1.put(colIndex + i, cIndexLabelMapChar1.get(colIndex + i + 1));
            cIndexLabelMapChar2.put(colIndex + i, cIndexLabelMapChar2.get(colIndex + i + 1));
            cIndexLabelMapChar1.remove(colIndex + i + 1);
            cIndexLabelMapChar2.remove(colIndex + i + 1);
        }
        --numEdge;
    }

    public void deleteVertex(String vertLabel) {
        int vertIndex = (int) rLabelIndexMap.get(vertLabel);
        deleteVertexRow(vertLabel, vertIndex);

    } // end of deleteVertex()

    private void deleteVertexRow(String vertLabel, int vertIndex) {
        // shift backwards rows
        rIndexLabelMap.remove(vertIndex);
        rLabelIndexMap.remove(vertLabel);
        for (int i = 0; i < numVertex - vertIndex - 1; i++) {
            incidenceMatrix[vertIndex + i] = incidenceMatrix[vertIndex + i + 1];

            rLabelIndexMap.put(rIndexLabelMap.get(vertIndex + i + 1), vertIndex + i);
            rIndexLabelMap.put(vertIndex + i, rIndexLabelMap.get(vertIndex + i + 1));
            rIndexLabelMap.remove(vertIndex + i + 1);
        }
    }

    private void deleteVertexEdges(String vertLabel) {
        for (int i = 0; i < numEdge; i++) {
            if ((String) cIndexLabelMapChar1.get(i) == vertLabel || (String) cIndexLabelMapChar2.get(i) == vertLabel)) {
                deleteCol(i);
            }
        }
    }

    public String[] kHopNeighbours(int k, String vertLabel) {
        // Implement me!

        // please update!
        return null;
    } // end of kHopNeighbours()

    public void printEdges(PrintWriter os) {
        // Implement me!
    } // end of printEdges()

} // end of class IncidenceMatrix
