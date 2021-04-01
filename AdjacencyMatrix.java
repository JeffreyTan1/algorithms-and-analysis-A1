import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Adjacency matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class AdjacencyMatrix extends AbstractGraph {

    private final int MAX_DIMEN = 15;
    private int[][] adjacencyMatrix;
    private int numVertex = 0;
    private HashMap<String, Integer> labelIndexMap;
    private HashMap<Integer, String> indexLabelMap;
    private HashMap<String, SIRState> labelStateMap;

    /**
     * Contructs empty graph.
     */
    public AdjacencyMatrix() {
        adjacencyMatrix = new int[MAX_DIMEN][MAX_DIMEN];
        labelIndexMap = new HashMap<String, Integer>();
        indexLabelMap = new HashMap<Integer, String>();
        labelStateMap = new HashMap<String, SIRState>();
    } // end of AdjacencyMatrix()

    public void addVertex(String vertLabel) {
        // set the rows and columns associated with new vertex to 0
        for (int i = 0; i < numVertex; i++) {
            adjacencyMatrix[numVertex][i] = 0;
            adjacencyMatrix[i][numVertex] = 0;
        }

        labelIndexMap.put(vertLabel, numVertex);
        indexLabelMap.put(numVertex, vertLabel);
        labelStateMap.put(vertLabel, SIRState.S);
        numVertex++;
    } // end of addVertex()

    public void addEdge(String srcLabel, String tarLabel) {
        int srcIndex = (int) labelIndexMap.get(srcLabel);
        int tarIndex = (int) labelIndexMap.get(tarLabel);
        // where src and tar meet, set to 1
        adjacencyMatrix[srcIndex][tarIndex] = 1;
        adjacencyMatrix[tarIndex][srcIndex] = 1;
    } // end of addEdge()

    public void toggleVertexState(String vertLabel) {
        switch ((SIRState) labelStateMap.get(vertLabel)) {
        case S:
            labelStateMap.put(vertLabel, SIRState.I);
            break;
        case I:
            labelStateMap.put(vertLabel, SIRState.R);
            break;
        case R:
            // do nothing
            break;
        }
    } // end of toggleVertexState()

    public void deleteEdge(String srcLabel, String tarLabel) {
        int srcIndex = (int) labelIndexMap.get(srcLabel);
        int tarIndex = (int) labelIndexMap.get(tarLabel);
        // where src and tar meet, set to 0
        adjacencyMatrix[srcIndex][tarIndex] = 0;
        adjacencyMatrix[tarIndex][srcIndex] = 0;
    } // end of deleteEdge()

    public void deleteVertex(String vertLabel) {
        int vertIndex = (int) labelIndexMap.get(vertLabel);

        // Clears any edges
        for (int i = 0; i < numVertex; i++) {
            adjacencyMatrix[vertIndex][i] = 0;
            adjacencyMatrix[i][vertIndex] = 0;
        }

        labelIndexMap.remove(vertLabel);
        indexLabelMap.remove(vertIndex);
        labelStateMap.remove(vertLabel);
        numVertex--;
    } // end of deleteVertex()

    public String[] kHopNeighbours(int k, String vertLabel) {
        String[] kHopNeighboursArray = new String[numVertex];
        int kHopNeighboursArrayLen = 0;
        int vertIndex = (int) labelIndexMap.get(vertLabel);
        int[] currentNeighbours;
        int currentNeighboursLen;
        int[] nextNeighbours = null;
        int nextNeighboursLen = 0;

        for (int i = 0; i < k; i++) {

            // if first iteration put the currentNeighbours in the currentNeighbours list
            if (i == 0) {
                currentNeighbours = new int[numVertex];
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
            for (int neighbourIndex = 0; neighbourIndex < currentNeighboursLen; neighbourIndex++) {
                // Check for edges to other vertices for current neighbour index
                for (int j = 0; j < numVertex; j++) {
                    if (adjacencyMatrix[neighbourIndex][j] == 1) {
                        // check if the array is already in kHopNeighboursArray
                        if (!isInArray((String) indexLabelMap.get(j), kHopNeighboursArray)) {
                            // Update the nextNeighbours list which is a list of neighbours, that we may
                            // Need to get neighbours of in the next hop
                            nextNeighbours[nextNeighboursLen] = j;
                            nextNeighboursLen++;
                            // Add the neighbours to the return list
                            kHopNeighboursArray[kHopNeighboursArrayLen] = (String) indexLabelMap.get(j);
                            kHopNeighboursArrayLen++;
                        }
                    }
                }
            }

        }

        return kHopNeighboursArray;

    } // end of kHopNeighbours()

    private boolean isInArray(String checkStr, String[] array) {
        for (String s : array) {
            if (s.equals(checkStr)) {
                return true;
            }
        }
        return false;
    }

    public void printVertices(PrintWriter os) {
        for (int i = 0; i < numVertex; i++) {
            String label = (String) indexLabelMap.get(i);
            os.print("(" + label + "," + labelStateMap.get(label) + ") ");
        }
    } // end of printVertices()

    public void printEdges(PrintWriter os) {
        for (int i = 0; i < numVertex; i++) {
            for (int j = 0; j < numVertex; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    os.println(indexLabelMap.get(i) + " " + indexLabelMap.get(j));
                }
            }
        }
    } // end of printEdges()

} // end of class AdjacencyMatrix
