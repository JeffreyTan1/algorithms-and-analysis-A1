import java.io.*;
import java.util.*;

public class Scenario2Delete {

    public static void main(String[] args) throws FileNotFoundException {

        System.out.println("Scenario 2 Delete program beginning");
        PrintStream stdOut = System.out;

        String outFile = "C:\\Users\\tanma\\Google Drive\\Synced Desktop\\Y2S1\\AA Files\\Assignments\\1\\algorithms-and-analysis-A1\\Graphs & Results\\Scenario2Del.txt";
        PrintStream stream = new PrintStream(outFile);
        System.setOut(stream);

        String[] fileChar1 = { "H", "M", "L" };
        String[] fileChar2 = { "H", "M", "L" };
        String[] fileChar3 = { "1", "2", "3" };

        for (int char1 = 0; char1 < 3; char1++) {
            for (int char2 = 0; char2 < 3; char2++) {
                for (int char3 = 0; char3 < 3; char3++) {
                    System.out.println("=============Printing deletion results for file: " + fileChar1[char1]
                            + fileChar2[char2] + fileChar3[char3] + "=====================");

                    AdjacencyList adjacencyList = new AdjacencyList();
                    AdjacencyMatrix adjacencyMatrix = new AdjacencyMatrix();
                    IncidenceMatrix incidenceMatrix = new IncidenceMatrix();

                    ArrayList<String> edgesExist = new ArrayList<String>();
                    ArrayList<String> vertsExist = new ArrayList<String>();

                    try {
                        readAndCreateGraphs(adjacencyList, adjacencyMatrix, incidenceMatrix, edgesExist, vertsExist,
                                fileChar1[char1], fileChar2[char2], fileChar3[char3]);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    int numEdge = incidenceMatrix.getNumEdge();

                    ArrayList<String> delEdgList = createDelEdges(numEdge, edgesExist, vertsExist);

                    runDeletions(delEdgList, adjacencyMatrix);
                    runDeletions(delEdgList, adjacencyList);
                    runDeletions(delEdgList, incidenceMatrix);

                }
            }
        }

        System.setOut(stdOut);
        System.out.println("Scenario 2 program finished");

    }

    private static void runDeletions(ArrayList<String> delEdgList, AbstractGraph abstractGraph) {

        long totalTime = 0;

        for (int i = 0; i < delEdgList.size(); i++) {
            String edge = delEdgList.get(i);
            int vIndex = 0;
            for (int j = 1; j < edge.length(); j++) {
                if (edge.charAt(j) == 'v') {
                    vIndex = j;
                }

            }
            String src = edge.substring(0, vIndex);
            String tar = edge.substring(vIndex);

            long startTime = System.nanoTime();
            abstractGraph.deleteEdge(src, tar);
            totalTime = totalTime + (System.nanoTime() - startTime);
            // System.out.println("a deletion was done");
        }

        double averageTime = totalTime / ((double) (delEdgList.size()));
        // double millis = averageTime / 1000000.0;

        System.out.println(
                "Average time for deletions " + abstractGraph.getClass() + ": " + averageTime + "nano seconds");
    }

    private static ArrayList<String> createDelEdges(int numEdge, ArrayList<String> edgesExist,
            ArrayList<String> vertsExist) {
        Random rand = new Random();

        int edgeAmount = (int) (numEdge * 0.2);

        ArrayList<String> delEdges = new ArrayList<String>();

        int count = 0;

        while (count < edgeAmount) {
            int randNum1 = rand.nextInt(vertsExist.size());
            int randNum2 = rand.nextInt(vertsExist.size());

            String randEdge = vertsExist.get(randNum1) + vertsExist.get(randNum2);
            String randEdgeReverse = vertsExist.get(randNum2) + vertsExist.get(randNum1);

            if (edgesExist.contains(randEdge) && !delEdges.contains(randEdge) && !delEdges.contains(randEdgeReverse)
                    && randNum1 != randNum2) {
                delEdges.add(randEdge);
                count++;
            }

        }

        return delEdges;

    }

    private static void readAndCreateGraphs(AdjacencyList adjacencyList, AdjacencyMatrix adjacencyMatrix,
            IncidenceMatrix incidenceMatrix, ArrayList<String> edgesExist, ArrayList<String> vertsExist,
            String fileChar1, String fileChar2, String fileChar3) throws FileNotFoundException {

        String startingDir = "C:\\Users\\tanma\\Google Drive\\Synced Desktop\\Y2S1\\AA Files\\Assignments\\1\\algorithms-and-analysis-A1\\Graphs & Results\\";
        String graphTypeDir = "Erdos-Renyi\\";
        // String graphTypeDir = "Scale-free\\";

        String graphSize = null;

        if (fileChar1.equals("H")) {
            graphSize = "High\\";
        } else if (fileChar1.equals("M")) {
            graphSize = "Medium\\";
        } else if (fileChar1.equals("L")) {
            graphSize = "Low\\";
        }

        String fileInName = fileChar1 + fileChar2 + fileChar3;
        String netExt = ".net";
        // String txtExt = ".txt";

        System.out.println("Graph number : " + fileInName);
        File pajekFile = new File(startingDir + graphTypeDir + graphSize + fileInName + netExt);

        Scanner vertReader = new Scanner(pajekFile);

        while (vertReader.hasNext()) {
            String data = vertReader.next();
            if (data.startsWith("\"") && data.endsWith("\"")) {
                String vert = data.substring(1, data.length() - 1);
                adjacencyList.addVertex(vert);
                adjacencyMatrix.addVertex(vert);
                incidenceMatrix.addVertex(vert);
                vertsExist.add(vert);
            }
        }

        vertReader.close();

        Scanner edgeReader = new Scanner(pajekFile);

        int starCounter = 0;

        while (edgeReader.hasNext()) {
            String data = edgeReader.next();
            if (data.startsWith("*")) {
                data = edgeReader.next();

                // System.out.println(data);
                starCounter++;
            }

            if (starCounter == 2) {
                String edgeFrom = data;
                edgeFrom = "v" + edgeFrom;
                String edgeTo = edgeReader.next();
                edgeTo = "v" + edgeTo;

                adjacencyList.addEdge(edgeFrom, edgeTo);
                adjacencyMatrix.addEdge(edgeFrom, edgeTo);
                incidenceMatrix.addEdge(edgeFrom, edgeTo);

                edgesExist.add(edgeFrom + edgeTo);
                edgesExist.add(edgeTo + edgeFrom);

                edgeReader.next();
            }

        }
        edgeReader.close();
    }

}
