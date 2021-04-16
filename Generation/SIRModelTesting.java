import java.io.*;
import java.util.*;

// For some reason this test takes REALLY LONG. But it completes in less than 10 minutes. Within our lifetimes at least.
public class SIRModelTesting {

    public static void main(String[] args) throws FileNotFoundException {

        System.out.println("SIRModelTest program beginning");
        PrintStream stdOut = System.out;

        String outFile = "C:\\Users\\tanma\\Google Drive\\Synced Desktop\\Y2S1\\AA Files\\Assignments\\1\\algorithms-and-analysis-A1\\Graphs & Results\\SIRModelTest.txt";
        PrintStream stream = new PrintStream(outFile);
        System.setOut(stream);

        float[] infSeedProportions = { 0.03f, 0.09f };
        float[] recProbabilities = { 0.2f, 0.8f };
        float[] infProbabilities = { 0.8f, 0.2f };

        System.out.println(
                "=============Printing deletion results for file: " + "H" + "M" + "1" + "=====================");
        AdjacencyList adjacencyList = new AdjacencyList();

        ArrayList<String> vertsExist = new ArrayList<String>();

        try {
            readAndCreateGraphs(adjacencyList, vertsExist, "1");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int numVertex = adjacencyList.getNumVertex();

        for (int prob = 0; prob < 2; prob++) {
            System.out.println("\nPrinting results when recProb: " + recProbabilities[prob] + " infProb: "
                    + infProbabilities[prob]);
            for (int prop = 0; prop < 2; prop++) {
                System.out.println("\nPrinting results when seed proportion: " + infSeedProportions[prop]);

                ArrayList<String> infSeedList = createInfSeeds(numVertex, vertsExist, infSeedProportions[prop]);

                runSIR(infSeedList, adjacencyList, recProbabilities[prob], infProbabilities[prob]);
            }
        }

        System.setOut(stdOut);
        System.out.println("SIRModelTest program finished");
    }

    private static void runSIR(ArrayList<String> infSeedList, AbstractGraph abstractGraph, float recProb,
            float infProb) {

        SIRModel sirModel = new SIRModel();

        String[] seedVertices = new String[infSeedList.size()];
        for (int i = 0; i < infSeedList.size(); i++) {
            seedVertices[i] = infSeedList.get(i);
        }

        PrintWriter outWriter = new PrintWriter(System.out, true);

        sirModel.runSimulation((ContactsGraph) abstractGraph, seedVertices, infProb, recProb, outWriter);

    }

    private static ArrayList<String> createInfSeeds(int numVert, ArrayList<String> vertsExist, float infSeedProp) {
        Random rand = new Random();

        int vertAmount = (int) (numVert * infSeedProp);

        ArrayList<String> seedVerts = new ArrayList<String>();

        int count = 0;

        while (count < vertAmount) {
            int randNum = rand.nextInt(vertsExist.size());

            String randVert = vertsExist.get(randNum);

            if (!seedVerts.contains(randVert)) {
                seedVerts.add(randVert);
                count++;
            }

        }

        return seedVerts;
    }

    private static void readAndCreateGraphs(AdjacencyList adjacencyList, ArrayList<String> vertsExist, String fileChar3)
            throws FileNotFoundException {

        String startingDir = "C:\\Users\\tanma\\Google Drive\\Synced Desktop\\Y2S1\\AA Files\\Assignments\\1\\algorithms-and-analysis-A1\\Graphs & Results\\";
        String graphTypeDir = "Erdos-Renyi\\";
        // String graphTypeDir = "Scale-Free\\";

        String graphSize = "High\\";

        String fileInName = "H" + "M" + fileChar3;
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

                edgeReader.next();
            }

        }
        edgeReader.close();
    }

}
