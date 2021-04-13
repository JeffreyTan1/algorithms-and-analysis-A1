import java.io.PrintWriter;

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
        Node newAdjList = new Node();
        adjacencyList[numVertex] = newAdjList;

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

        boolean cont = true;

        while (cont) {

            if (currentNode.getData().equals(toLabel)) {
                // when its the first node and there is a second node
                if (prevNode == null && currentNode.getNextNode() != null) {
                    adjacencyList[fromIndex] = currentNode.getNextNode();
                }
                // when its the first node and there is no second node
                else if (prevNode == null && currentNode.getNextNode() == null) {
                    currentNode.setData(null);
                }
                // when its not the first node and there is no node after
                else if (prevNode != null && currentNode.getNextNode() == null) {
                    prevNode.setNextNode(null);
                }
                // when its not the first node and there is a node after
                else if (prevNode != null && currentNode.getNextNode() != null) {
                    prevNode.setNextNode(currentNode.getNextNode());
                }
                cont = false;
            }

            prevNode = currentNode;
            // will be null ptr if no next, but in that case
            // the program should've run the if statement above

            currentNode = currentNode.getNextNode();

        }

    }

    public void deleteVertex(String vertLabel) {
        int vertIndex = (int) labelIndexMap.get(vertLabel);

        // delete all edges associated with the vertex
        for (int i = 0; i < numVertex; i++) {
            if (isEdgeOf(vertLabel, adjacencyList[i])) {
                deleteFromTo(i, vertLabel);
            }
        }

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
        numVertex--;

    } // end of deleteVertex()

    public String[] kHopNeighbours(int k, String vertLabel) {

        String[] kHopNeighboursArray = new String[numVertex];
        int kHopNeighboursArrayLen = 0;

        int vertIndex = (int) labelIndexMap.get(vertLabel);

        int[] currentNeighbours = new int[numVertex];
        int currentNeighboursLen = 0;

        int[] nextNeighbours = new int[numVertex];
        int nextNeighboursLen = 0;

        for (int i = 0; i < k; i++) {

            // if first iteration put the currentNeighbours in the currentNeighbours list
            if (i == 0) {
                Node currentNode = adjacencyList[vertIndex];

                // if vertex has no edges at all :: early termination
                if (currentNode.getData() == null) {
                    String[] kHopWithoutNull = resizeArrayByLen(kHopNeighboursArray, kHopNeighboursArrayLen);
                    return kHopWithoutNull;
                }
                currentNeighbours[currentNeighboursLen] = vertIndex;
                currentNeighboursLen++;
                // if not first iteration then we want our currentNeighbours to be the previous

            } else {
                currentNeighbours = nextNeighbours;
                currentNeighboursLen = nextNeighboursLen;
                // Now reset the nextNeighbors list to a fresh one
                nextNeighbours = new int[numVertex];
                nextNeighboursLen = 0;
            }

            // Iterate through all the neighbours in the currentNeighbours list
            for (int j = 0; j < currentNeighboursLen; j++) {
                int index = currentNeighbours[j];
                Node currentNode = adjacencyList[index];

                while (currentNode.getNextNode() != null) {
                    String data = currentNode.getData();

                    if (!isInArray(data, kHopNeighboursArray) && !data.equals(vertLabel)) {

                        // Update the nextNeighbours list which is a list of neighbours, that we may
                        // Need to get neighbours of in the next hop
                        nextNeighbours[nextNeighboursLen] = labelIndexMap.get(data);
                        nextNeighboursLen++;
                        // Add the neighbours to the return list
                        kHopNeighboursArray[kHopNeighboursArrayLen] = data;
                        kHopNeighboursArrayLen++;
                    }

                    currentNode = currentNode.getNextNode();
                }

                String data = currentNode.getData();
                if (!isInArray(data, kHopNeighboursArray) && !data.equals(vertLabel)) {

                    // Update the nextNeighbours list which is a list of neighbours, that we may
                    // Need to get neighbours of in the next hop
                    nextNeighbours[nextNeighboursLen] = labelIndexMap.get(data);
                    nextNeighboursLen++;
                    // Add the neighbours to the return list
                    kHopNeighboursArray[kHopNeighboursArrayLen] = data;
                    kHopNeighboursArrayLen++;
                }

            }
        }

        String[] kHopWithoutNull = resizeArrayByLen(kHopNeighboursArray, kHopNeighboursArrayLen);

        return kHopWithoutNull;
    } // end of kHopNeighbours()

    public void printEdges(PrintWriter os) {
        for (int i = 0; i < numVertex; i++) {
            if (adjacencyList[i].getData() != null) {
                Node currentNode = adjacencyList[i];
                if (currentNode.getNextNode() == null) {
                    os.println(indexLabelMap.get(i) + " " + currentNode.getData());
                } else {
                    while (currentNode.getNextNode() != null) {
                        os.println(indexLabelMap.get(i) + " " + currentNode.getData());
                        currentNode = currentNode.getNextNode();
                    }
                    os.println(indexLabelMap.get(i) + " " + currentNode.getData());
                }
            }
        }
    } // end of printEdges()

    private boolean isEdgeOf(String checkLabel, Node adjList) {
        if (adjList.getData() != null) {
            Node currentNode = adjList;
            // if the adjlist is size 1
            if (currentNode.getNextNode() == null) {
                if (currentNode.getData().equals(checkLabel)) {
                    return true;
                }
                return false;
            }
            // adjList size > 1
            while (currentNode.getNextNode() != null) {
                if (currentNode.getData().equals(checkLabel)) {
                    return true;
                }
                currentNode = currentNode.getNextNode();
            }
            if (currentNode.getData().equals(checkLabel)) {
                return true;
            }
        }
        // if the adjList is empty to begin with or not in the list
        return false;
    }

    // Node implementation inner class
    public class Node {
        Node nextNode;
        String data;

        public Node() {
            data = null;
            nextNode = null;
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
