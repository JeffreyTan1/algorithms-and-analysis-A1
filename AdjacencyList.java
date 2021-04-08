import java.io.PrintWriter;
// import java.util.*;
import java.util.HashMap;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjacencyList extends AbstractGraph {
    private Node[] adjacencyList;

    /**
     * Contructs empty graph.
     */
    public AdjacencyList() {
        adjacencyList = new Node[MAX_DIMEN];
    } // end of AdjacencyList()

    public void addVertex(String vertLabel) {
        Node newList = new Node();
        adjacencyList[numVertex] = newList;

        labelIndexMap.put(vertLabel, numVertex);
        indexLabelMap.put(numVertex, vertLabel);
        labelStateMap.put(vertLabel, SIRState.S);
        numVertex++;
    } // end of addVertex()

    public void addEdge(String srcLabel, String tarLabel) {
        int srcIndex = (int) labelIndexMap.get(srcLabel);
        int tarIndex = (int) labelIndexMap.get(tarLabel);

        edgeFromTo(srcIndex, tarLabel);
        edgeFromTo(tarIndex, srcLabel);

    } // end of addEdge()

    private void edgeFromTo(int fromIndex, String toLabel) {
        if (adjacencyList[fromIndex].getData() == null) {
            adjacencyList[fromIndex].setData(toLabel);
        } else {
            Node currentNode = adjacencyList[fromIndex];
            while (currentNode.getNextNode() != null) {
                currentNode = currentNode.getNextNode();
            }
            Node newNode = new Node(toLabel);
            currentNode.setNextNode(newNode);
        }
    }

    public void deleteEdge(String srcLabel, String tarLabel) {
        int srcIndex = (int) labelIndexMap.get(srcLabel);
        int tarIndex = (int) labelIndexMap.get(tarLabel);

        deleteFromTo(srcIndex, tarLabel);
        deleteFromTo(tarIndex, srcLabel);
    } // end of deleteEdge()

    private void deleteFromTo(int fromIndex, String toLabel) {
        Node currentNode = adjacencyList[fromIndex];
        Node prevNode = null;

        while (true) {
            if (currentNode.getData() == toLabel) {
                // when its the first node and there is a second node
                if (prevNode == null && currentNode.getNextNode() != null) {
                    adjacencyList[fromIndex] = currentNode.getNextNode();
                }
                // when its the first node and there is no second node
                else if (prevNode == null && currentNode.getNextNode() == null) {
                    currentNode.setData(null);
                }
                // when its not the first node and there is no node after
                else if (prevNode != null && currentNode.getNextNode() != null) {
                    prevNode.setNextNode(null);
                }
                // when its not the first node and there is a node after
                else if (prevNode != null && currentNode.getNextNode() == null) {
                    prevNode.setNextNode(currentNode.getNextNode());
                }
                break;
            }

            prevNode = currentNode;
            // will be null ptr if no next, but in that case
            // the program should've run the if statement above
            currentNode = currentNode.getNextNode();
        }

    }

    public void deleteVertex(String vertLabel) {
        int vertIndex = (int) labelIndexMap.get(vertLabel);

        adjacencyList[vertIndex] = null;
        labelIndexMap.remove(vertLabel);
        indexLabelMap.remove(vertIndex);
        // shift backwards
        for (int i = 0; i < numVertex - vertIndex - 1; i++) {
            adjacencyList[vertIndex + i] = adjacencyList[vertIndex + i + 1];

            labelIndexMap.put(indexLabelMap.get(vertIndex + i + 1), vertIndex + i);
            indexLabelMap.put(vertIndex + i, indexLabelMap.get(vertIndex + i + 1));
            indexLabelMap.remove(vertIndex + i + 1);
        }

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
            for (int j = 0; j < currentNeighboursLen; j++) {
                String vertexInList = adjacencyList[i].getData();
                int index = labelIndexMap.get(vertexInList);

                if (!isInArray((String) indexLabelMap.get(index), kHopNeighboursArray)) {
                    // Update the nextNeighbours list which is a list of neighbours, that we may
                    // Need to get neighbours of in the next hop
                    nextNeighbours[nextNeighboursLen] = index;
                    nextNeighboursLen++;
                    // Add the neighbours to the return list
                    kHopNeighboursArray[kHopNeighboursArrayLen] = (String) indexLabelMap.get(index);
                    kHopNeighboursArrayLen++;
                }
            }
        }

        return kHopNeighboursArray;
    } // end of kHopNeighbours()

    public void printEdges(PrintWriter os) {
        for (int i = 0; i < numVertex; i++) {
            if (adjacencyList[i].getData() == null) {
                os.println("");
            } else {
                Node currentNode = adjacencyList[i];
                while (currentNode.getNextNode() != null) {
                    os.println(indexLabelMap.get(i) + " " + currentNode.getData());
                    currentNode = currentNode.getNextNode();
                }
            }
        }
    } // end of printEdges()

    // Node implementation inner class
    public class Node {
        Node nextNode;
        String data;

        public Node() {
        }

        public Node(String data) {
            this.data = data;
        }

        public void setNextNode(Node nextNode) {
            this.nextNode = nextNode;
        }

        public Node getNextNode() {
            return nextNode;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

    }
} // end of class AdjacencyList
