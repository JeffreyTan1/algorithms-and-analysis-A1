import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Scenario1 {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Scenario 1 program beginning");
        PrintStream stdOut = System.out;

        String outFile = "C:\\Users\\tanma\\Google Drive\\Synced Desktop\\Y2S1\\AA Files\\Assignments\\1\\algorithms-and-analysis-A1\\Graphs & Results\\Scenario1.txt";
        PrintStream stream = new PrintStream(outFile);

        System.setOut(stream);
        String[] fileChar2 = { "H", "M", "L" };
        String[] fileChar3 = { "1", "2", "3" };

        for (int char2 = 0; char2 < 3; char2++) {
            for (int char3 = 0; char3 < 3; char3++) {
                System.out.println("=============Printing results for file: " + "H" + fileChar2[char2]
                        + fileChar3[char3] + "=====================");
                int[] k = { 10, 20, 30 };

                AdjacencyList adjacencyList = new AdjacencyList();
                AdjacencyMatrix adjacencyMatrix = new AdjacencyMatrix();
                IncidenceMatrix incidenceMatrix = new IncidenceMatrix();

                try {
                    readAndCreateGraphs(adjacencyList, adjacencyMatrix, incidenceMatrix, fileChar2[char2],
                            fileChar3[char3]);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                ArrayList<String> kHopSeeds = createKHopSeeds(adjacencyList);

                for (int i = 0; i < 3; i++) {
                    if (i == 0) {
                        System.out.println("k = 10");
                    }
                    if (i == 1) {
                        System.out.println("k = 20");
                    }
                    if (i == 2) {
                        System.out.println("k = 30");
                    }
                    runKHopOnSeeds(kHopSeeds, adjacencyMatrix, k[i]);

                    runKHopOnSeeds(kHopSeeds, adjacencyList, k[i]);

                    runKHopOnSeeds(kHopSeeds, incidenceMatrix, k[i]);
                }
            }
        }

        System.setOut(stdOut);
        System.out.println("Scenario 1 program finished");

    }

    private static void runKHopOnSeeds(ArrayList<String> seeds, AbstractGraph abstractGraph, int k) {

        long totalTime = 0;

        for (int i = 0; i < seeds.size(); i++) {
            long startTime = System.nanoTime();
            abstractGraph.kHopNeighbours(k, seeds.get(i));
            totalTime = totalTime + (System.nanoTime() - startTime);
        }

        double averageTime = totalTime / ((double) (seeds.size()));
        // double millis = averageTime / 1000000.0;

        System.out.println("Average kHop time for " + abstractGraph.getClass() + ": " + averageTime + "nano seconds");
    }

    private static ArrayList<String> createKHopSeeds(AbstractGraph abstractGraph) {
        Random rand = new Random();

        int graphVert = abstractGraph.getNumVertex();

        int seedAmount = (int) (graphVert * 0.2);

        ArrayList<String> seedVerts = new ArrayList<String>();

        HashMap indexLabelMap = abstractGraph.getIndexLabelMap();

        int count = 0;

        while (count < seedAmount) {
            int randNum = rand.nextInt(graphVert);
            if (!seedVerts.contains(randNum)) {
                seedVerts.add((String) indexLabelMap.get(randNum));
                count++;
            }
        }

        return seedVerts;

    }

    private static void readAndCreateGraphs(AdjacencyList adjacencyList, AdjacencyMatrix adjacencyMatrix,
            IncidenceMatrix incidenceMatrix, String fileChar2, String fileChar3) throws FileNotFoundException {

        String startingDir = "C:\\Users\\tanma\\Google Drive\\Synced Desktop\\Y2S1\\AA Files\\Assignments\\1\\algorithms-and-analysis-A1\\Graphs & Results\\";
        String graphTypeDir = "Erdos-Renyi\\";
        // String graphTypeDir = "Scale-free\\";
        String graphSize = "High\\";
        String fileInName = "H" + fileChar2 + fileChar3;
        String netExt = ".net";
        // String txtExt = ".txt";

        System.out.println("Graph name : " + fileInName);
        File pajekFile = new File(startingDir + graphTypeDir + graphSize + fileInName + netExt);

        Scanner vertReader = new Scanner(pajekFile);

        while (vertReader.hasNext()) {
            String data = vertReader.next();
            if (data.startsWith("\"") && data.endsWith("\"")) {
                String vert = data.substring(1, data.length() - 1);
                adjacencyList.addVertex(vert);
                adjacencyMatrix.addVertex(vert);
                incidenceMatrix.addVertex(vert);
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
