import java.io.PrintWriter;

/**
 * Adjacency matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class AdjacencyMatrix extends AbstractGraph {

    private int[][] adjacencyMatrix;

    /**
     * Contructs empty graph.
     */
    public AdjacencyMatrix() {
        adjacencyMatrix = new int[MAX_DIMEN][MAX_DIMEN];
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

    public void deleteEdge(String srcLabel, String tarLabel) {
        int srcIndex = (int) labelIndexMap.get(srcLabel);
        int tarIndex = (int) labelIndexMap.get(tarLabel);
        // where src and tar meet, set to 0
        adjacencyMatrix[srcIndex][tarIndex] = 0;
        adjacencyMatrix[tarIndex][srcIndex] = 0;
    } // end of deleteEdge()

    public void deleteVertex(String vertLabel) {
        int vertIndex = (int) labelIndexMap.get(vertLabel);

        labelIndexMap.remove(vertLabel);
        indexLabelMap.remove(vertIndex);
        labelStateMap.remove(vertLabel);

        // shift columns

        for (int i = 0; i < numVertex - vertIndex - 1; i++) {
            for (int j = 0; j < numVertex; j++) {
                adjacencyMatrix[j][vertIndex + i] = adjacencyMatrix[j][vertIndex + i + 1];
            }

        }

        // shift rows
        for (int i = 0; i < numVertex - vertIndex - 1; i++) {
            for (int j = 0; j < numVertex; j++) {
                adjacencyMatrix[vertIndex + i][j] = adjacencyMatrix[vertIndex + i + 1][j];

            }
            labelIndexMap.put(indexLabelMap.get(vertIndex + i + 1), vertIndex + i);
            indexLabelMap.put(vertIndex + i, indexLabelMap.get(vertIndex + i + 1));
            indexLabelMap.remove(vertIndex + i + 1);
        }

        numVertex--;

    } // end of deleteVertex()

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
                // Check for edges to other vertices for current neighbour index
                for (int m = 0; m < numVertex; m++) {

                    if (adjacencyMatrix[currentNeighbours[j]][m] == 1) {
                        // check if the array is already in kHopNeighboursArray
                        if (!isInArray(indexLabelMap.get(m), kHopNeighboursArray) && m != vertIndex) {
                            // Update the nextNeighbours list which is a list of neighbours, that we may
                            // Need to get neighbours of in the next hop
                            nextNeighbours[nextNeighboursLen] = m;
                            nextNeighboursLen++;
                            // Add the neighbours to the return list
                            kHopNeighboursArray[kHopNeighboursArrayLen] = (String) indexLabelMap.get(m);
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
        for (int i = 0; i < numVertex; i++) {
            for (int j = 0; j < numVertex; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    os.println(indexLabelMap.get(i) + " " + indexLabelMap.get(j));
                }
            }
        }
    } // end of printEdges()

} // end of class AdjacencyMatrix
