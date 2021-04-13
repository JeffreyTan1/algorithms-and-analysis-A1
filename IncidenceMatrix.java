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
    private HashMap<String, Integer> colLabelIndexMap;
    // We use string instead of char because of the more powerful string library in
    // java
    private HashMap<Integer, String> colIndexLabelMapChar1;
    private HashMap<Integer, String> colIndexLabelMapChar2;

    /**
     * Contructs empty graph.
     */
    public IncidenceMatrix() {
        incidenceMatrix = new int[MAX_DIMEN][((int) Math.pow(MAX_DIMEN, 2)) - MAX_DIMEN];
        numEdge = 0;
        labelIndexMap = new HashMap<String, Integer>();
        indexLabelMap = new HashMap<Integer, String>();
        colLabelIndexMap = new HashMap<String, Integer>();
        colIndexLabelMapChar1 = new HashMap<Integer, String>();
        colIndexLabelMapChar2 = new HashMap<Integer, String>();
    } // end of IncidenceMatrix()

    public void addVertex(String vertLabel) {
        // set row for the vertex to be 0 because it has no edges
        for (int i = 0; i < numEdge; i++) {
            incidenceMatrix[numVertex][i] = 0;
        }
        labelIndexMap.put(vertLabel, numVertex);
        indexLabelMap.put(numVertex, vertLabel);
        labelStateMap.put(vertLabel, SIRState.S);
        numVertex++;
    } // end of addVertex()

    public void addEdge(String srcLabel, String tarLabel) {
        int srcIndex = (int) labelIndexMap.get(srcLabel);
        int tarIndex = (int) labelIndexMap.get(tarLabel);
        edgeFromTo(srcIndex, srcLabel, tarIndex, tarLabel);
        edgeFromTo(tarIndex, tarLabel, srcIndex, srcLabel);

    } // end of addEdge()

    private void edgeFromTo(int fromIndex, String fromLabel, int toIndex, String toLabel) {

        incidenceMatrix[fromIndex][numEdge] = 1;
        incidenceMatrix[toIndex][numEdge] = 1;
        // set the rest to 0 in the new edge column
        for (int i = 0; i < numVertex; i++) {
            if (!(i == fromIndex || i == toIndex)) {
                incidenceMatrix[i][numEdge] = 0;
            }
        }
        colIndexLabelMapChar1.put(numEdge, fromLabel);
        colIndexLabelMapChar2.put(numEdge, toLabel);
        colLabelIndexMap.put(fromLabel + toLabel, numEdge);
        numEdge++;
    }

    public void deleteEdge(String srcLabel, String tarLabel) {
        int orglNumEdge = numEdge;
        int numRemoved = 0;
        for (int i = 0; i < orglNumEdge; i++) {
            // delete if colIndex maps have src and tar in the same index
            if (colIndexLabelMapChar1.get(i - numRemoved).equals(srcLabel)
                    && colIndexLabelMapChar2.get(i - numRemoved).equals(tarLabel)) {

                deleteCol(i - numRemoved);
                numRemoved++;
            } else if (colIndexLabelMapChar1.get(i - numRemoved).equals(tarLabel)
                    && colIndexLabelMapChar2.get(i - numRemoved).equals(srcLabel)) {

                deleteCol(i - numRemoved);
                numRemoved++;
            }

        }
    } // end of deleteEdge()

    private void deleteCol(int colIndex) {

        for (int i = 0; i < numEdge - colIndex - 1; i++) {
            for (int j = 0; j < numVertex; j++) {
                incidenceMatrix[j][colIndex + i] = incidenceMatrix[j][colIndex + i + 1];
            }
            colIndexLabelMapChar1.put(colIndex + i, colIndexLabelMapChar1.get(colIndex + i + 1));
            colIndexLabelMapChar2.put(colIndex + i, colIndexLabelMapChar2.get(colIndex + i + 1));
            colIndexLabelMapChar1.remove(colIndex + i + 1);
            colIndexLabelMapChar2.remove(colIndex + i + 1);
        }
        --numEdge;
    }

    public void deleteVertex(String vertLabel) {

        int vertIndex = (int) labelIndexMap.get(vertLabel);
        deleteVertexEdges(vertLabel);

        deleteVertexRow(vertLabel, vertIndex);

    } // end of deleteVertex()

    private void deleteVertexRow(String vertLabel, int vertIndex) {
        // shift backwards rows

        for (int i = 0; i < numVertex - vertIndex - 1; i++) {
            for (int j = 0; j < numEdge; j++) {
                incidenceMatrix[vertIndex + i][j] = incidenceMatrix[vertIndex + i + 1][j];

                // System.out.println("Replacing " ;

            }
            labelIndexMap.put(indexLabelMap.get(vertIndex + i + 1), vertIndex + i);
            indexLabelMap.put(vertIndex + i, indexLabelMap.get(vertIndex + i + 1));
            indexLabelMap.remove(vertIndex + i + 1);
        }

        numVertex--;
    }

    private String graphToString() {
        String str = "";

        for (int i = 0; i < numEdge; i++) {
            str = str + " " + colIndexLabelMapChar1.get(i) + colIndexLabelMapChar2.get(i);
        }
        str = str + "\n";

        for (int i = 0; i < numVertex; i++) {
            str = str + indexLabelMap.get(i) + ": ";
            for (int j = 0; j < numEdge; j++) {
                int bin;

                if (incidenceMatrix[i][j] == 1) {
                    bin = 1;
                } else {
                    bin = 0;
                }

                str = str + bin + "  ";
            }
            str = str + "\n";
        }

        return str;
    }

    private void deleteVertexEdges(String vertLabel) {
        int orglNumEdge = numEdge;
        int numRemoved = 0;
        for (int i = 0; i < orglNumEdge; i++) {
            String char1 = colIndexLabelMapChar1.get(i - numRemoved);
            String char2 = colIndexLabelMapChar2.get(i - numRemoved);

            if (char1.equals(vertLabel)) {

                deleteCol(i - numRemoved);
                numRemoved++;
            }
            if (char2.equals(vertLabel)) {
                deleteCol(i - numRemoved);
                numRemoved++;
            }
        }
    }

    public String[] kHopNeighbours(int k, String vertLabel) {

        String[] kHopNeighboursArray = new String[numVertex];
        int kHopNeighboursArrayLen = 0;

        int vertIndex = (int) labelIndexMap.get(vertLabel);

        int[] currentNeighbours = new int[numVertex];
        int currentNeighboursLen;

        int[] nextNeighbours = new int[numVertex];
        int nextNeighboursLen = 0;

        for (int i = 0; i < k; i++) {

            // if first iteration put the currentNeighbours in the currentNeighbours list
            if (i == 0) {
                currentNeighbours[0] = vertIndex;
                currentNeighboursLen = 1;
                // if not first iteration then we want our currentNeighbours to be the previous
                // nextNeighbours
            } else {
                currentNeighbours = nextNeighbours;
                currentNeighboursLen = nextNeighboursLen;
                // Now reset the nextNeighbors list to a fresh one
                nextNeighbours = new int[numVertex];
                nextNeighboursLen = 0;
            }

            // Iterate through all the neighbours in the currentNeighbours list
            for (int j = 0; j < currentNeighboursLen; j++) {
                String rowLabel = indexLabelMap.get(currentNeighbours[j]);
                // Check for edges to other vertices for current neighbour index
                for (int m = 0; m < numEdge; m++) {
                    // System.out.println("iteration for edges: " + m);
                    String labelChar1 = colIndexLabelMapChar1.get(m);
                    String labelChar2 = colIndexLabelMapChar2.get(m);

                    if (incidenceMatrix[currentNeighbours[j]][m] == 1) {
                        // check if the array is already in kHopNeighboursArray

                        String labelToAdd;

                        if (rowLabel.equals(labelChar1)) {
                            labelToAdd = labelChar2;
                        } else {
                            labelToAdd = labelChar1;
                        }

                        if (!isInArray(labelToAdd, kHopNeighboursArray) && !labelToAdd.equals(vertLabel)) {

                            // Update the nextNeighbours list which is a list of neighbours, that we may
                            // Need to get neighbours of in the next hop

                            nextNeighbours[nextNeighboursLen] = labelIndexMap.get(labelToAdd);
                            nextNeighboursLen++;
                            // Add the neighbours to the return list
                            kHopNeighboursArray[kHopNeighboursArrayLen] = labelToAdd;
                            kHopNeighboursArrayLen++;
                        }
                    }

                }
            }

        }

        String[] kHopWithoutNull = resizeArrayByLen(kHopNeighboursArray, kHopNeighboursArrayLen);

        return kHopWithoutNull;
    } // end of kHopNeighbours()

    public void printEdges(PrintWriter os) {
        for (int i = 0; i < numEdge; i++) {
            os.println(colIndexLabelMapChar1.get(i) + " " + colIndexLabelMapChar2.get(i));
        }
    } // end of printEdges()

    public int getNumEdge() {
        return numEdge;
    }

} // end of class IncidenceMatrix
