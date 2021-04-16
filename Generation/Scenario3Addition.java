import java.io.*;
import java.util.*;

public class Scenario3Addition {

    public static void main(String[] args) throws FileNotFoundException {

        System.out.println("Scenario 3 Addition program beginning");
        PrintStream stdOut = System.out;

        String outFile = "C:\\Users\\tanma\\Google Drive\\Synced Desktop\\Y2S1\\AA Files\\Assignments\\1\\algorithms-and-analysis-A1\\Graphs & Results\\Scenario3Add.txt";
        PrintStream stream = new PrintStream(outFile);
        System.setOut(stream);

        String[] fileChar1 = { "H", "M", "L" };
        String[] fileChar2 = { "H", "M", "L" };
        String[] fileChar3 = { "1", "2", "3" };

        for (int char1 = 0; char1 < 3; char1++) {
            for (int char2 = 0; char2 < 3; char2++) {
                for (int char3 = 0; char3 < 3; char3++) {
                    System.out.println("=============Printing vert addition results for file: " + fileChar1[char1]
                            + fileChar2[char2] + fileChar3[char3] + "=====================");

                    AdjacencyList adjacencyList = new AdjacencyList();
                    AdjacencyMatrix adjacencyMatrix = new AdjacencyMatrix();
                    IncidenceMatrix incidenceMatrix = new IncidenceMatrix();

                    ArrayList<String> vertsExist = new ArrayList<String>();

                    try {
                        readAndCreateGraphs(adjacencyList, adjacencyMatrix, incidenceMatrix, vertsExist,
                                fileChar1[char1], fileChar2[char2], fileChar3[char3]);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    int numVert = incidenceMatrix.getNumVertex();

                    ArrayList<String> addVertList = createaddVertes(numVert, vertsExist);

                    runAdditions(addVertList, adjacencyMatrix);
                    runAdditions(addVertList, adjacencyList);
                    runAdditions(addVertList, incidenceMatrix);

                }
            }
        }

        System.setOut(stdOut);
        System.out.println("Scenario 3 addition program finished");

    }

    private static void runAdditions(ArrayList<String> addVertList, AbstractGraph abstractGraph) {

        long totalTime = 0;

        for (int i = 0; i < addVertList.size(); i++) {
            String vert = addVertList.get(i);

            long startTime = System.nanoTime();
            abstractGraph.addVertex(vert);
            totalTime = totalTime + (System.nanoTime() - startTime);

        }

        double averageTime = totalTime / ((double) (addVertList.size()));
        // double millis = averageTime / 1000000.0;

        System.out.println(
                "Average time for additions " + abstractGraph.getClass() + ": " + averageTime + "nano seconds");
    }

    private static ArrayList<String> createaddVertes(int numVert, ArrayList<String> vertsExist) {
        int vertAmount = (int) (numVert * 0.2);

        ArrayList<String> addVerts = new ArrayList<String>();

        for (int i = 0; i < vertAmount; i++) {
            String newVert = "v" + (vertAmount + 1);
            addVerts.add(newVert);
        }

        return addVerts;

    }

    private static void readAndCreateGraphs(AdjacencyList adjacencyList, AdjacencyMatrix adjacencyMatrix,
            IncidenceMatrix incidenceMatrix, ArrayList<String> vertsExist, String fileChar1, String fileChar2,
            String fileChar3) throws FileNotFoundException {

        String startingDir = "C:\\Users\\tanma\\Google Drive\\Synced Desktop\\Y2S1\\AA Files\\Assignments\\1\\algorithms-and-analysis-A1\\Graphs & Results\\";
        // String graphTypeDir = "Erdos-Renyi\\";
        String graphTypeDir = "Scale-Free\\";

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

                edgeReader.next();
            }

        }
        edgeReader.close();
    }

}
