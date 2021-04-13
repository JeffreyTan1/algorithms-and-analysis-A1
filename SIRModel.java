import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Random;
//import java.util.zip.Inflater;

/**
 * SIR model.
 *
 * @author Jeffrey Chan, 2021.
 */
public class SIRModel {

    /**
     * Default constructor, modify as needed.
     */

    AbstractGraph mGraph;
    String[] infectedVertices;
    int infLen;
    String[] susceptibleVertices;
    int suscLen;
    String[] recoveredVertices;
    int recLen;
    String[] newlyInfected;
    String[] newlyRecovered;

    HashMap<String, Integer> labelIndexMap;
    HashMap<Integer, String> indexLabelMap;
    HashMap<String, SIRState> labelStateMap;

    Random rand = new Random();

    int numVertex;

    public SIRModel() {

    } // end of SIRModel()

    /**
     * Run the SIR epidemic model to completion, i.e., until no more changes to the
     * states of the vertices for a whole iteration.
     *
     * @param graph             Input contracts graph.
     * @param seedVertices      Set of seed, infected vertices.
     * @param infectionProb     Probability of infection.
     * @param recoverProb       Probability that a vertex can become recovered.
     * @param sirModelOutWriter PrintWriter to output the necessary information per
     *                          iteration (see specs for details).
     */
    public void runSimulation(ContactsGraph graph, String[] seedVertices, float infectionProb, float recoverProb,
            PrintWriter sirModelOutWriter) {
        rand.setSeed(1234);

        mGraph = ((AbstractGraph) graph);
        numVertex = mGraph.getNumVertex();

        labelIndexMap = mGraph.getLabelIndexMap();
        indexLabelMap = mGraph.getIndexLabelMap();
        labelStateMap = mGraph.getLabelStateMap();

        infectedVertices = new String[numVertex];
        infLen = 0;
        susceptibleVertices = new String[numVertex];
        suscLen = 0;
        recoveredVertices = new String[numVertex];
        recLen = 0;

        // Set the seed vertices to infected
        for (int i = 0; i < seedVertices.length; i++) {
            String seedLabel = seedVertices[i];
            labelStateMap.put(seedLabel, SIRState.I);
        }

        // Setup the lists of inf, sus, and rec
        for (int i = 0; i < numVertex; i++) {
            String currentLabel = indexLabelMap.get(i);
            switch (labelStateMap.get(currentLabel)) {
            case S:
                susceptibleVertices[suscLen] = currentLabel;
                suscLen++;
                break;
            case I:
                infectedVertices[infLen] = currentLabel;
                infLen++;
                break;
            case R:
                recoveredVertices[recLen] = currentLabel;
                break;
            }
        }

        // System.out.println("Sus vertices: " + "\n" +
        // arrayToString(susceptibleVertices));
        // System.out.println("Inf vertices: " + "\n" +
        // arrayToString(infectedVertices));
        // System.out.println("Rec vertices: " + "\n" +
        // arrayToString(recoveredVertices));

        boolean continueModel = true;
        int iter = 1;

        int prevInf = -1;
        int prevRec = -1;
        int sameInfRecCounter = 0;

        while (continueModel) {
            // System.out.println("iter: " + iter);

            newlyInfected = updateInfected(mGraph, infectionProb);
            newlyRecovered = updateRecovered(mGraph, recoverProb);

            // System.out.println("infvert " + arrayToString(infectedVertices));
            // System.out.println("newlyinf " + arrayToString(newlyInfected));
            // System.out.println("newlyrec " + arrayToString(newlyRecovered));

            // System.out.println("unison array " +
            // arrayToString(unisonOfArrays(infectedVertices, newlyInfected)));

            infectedVertices = minusArrays(unisonOfArrays(infectedVertices, newlyInfected), newlyRecovered);
            infLen = getArrayLengthNonNull(infectedVertices);

            recoveredVertices = unisonOfArrays(recoveredVertices, newlyRecovered);
            recLen = getArrayLengthNonNull(recoveredVertices);

            continueModel = updateStop(prevInf, prevRec);

            String line = iter + ":" + "[" + arrayToString(newlyInfected) + "]" + "[" + arrayToString(newlyRecovered)
                    + "]";

            sirModelOutWriter.println(line);

            int infectedVerticesLen = getArrayLengthNonNull(infectedVertices);
            int recoveredVerticesLen = getArrayLengthNonNull(recoveredVertices);

            if (prevInf == infectedVerticesLen || prevRec == recoveredVerticesLen) {
                sameInfRecCounter++;
            } else {
                sameInfRecCounter = 0;
            }

            if (sameInfRecCounter == 10) {
                continueModel = true;
            }

            prevInf = infectedVerticesLen;
            prevRec = recoveredVerticesLen;

            iter++;
        }

    } // end of runSimulation()

    private String[] updateInfected(AbstractGraph graph, float infectProb) {
        String[] newInf = new String[numVertex];
        int newInfLen = 0;

        for (int i = 0; i < infLen; i++) {
            String[] neighbours = graph.kHopNeighbours(1, infectedVertices[i]);
            // System.out.println("SIR: neighbours for current infected: " +
            // arrayToString(neighbours));

            for (int j = 0; j < getArrayLengthNonNull(neighbours); j++) {
                // passes rng, not already infected, or recovered
                if (randomResultGenerator(infectProb, rand) && !isInArray(neighbours[j], infectedVertices)
                        && !isInArray(neighbours[j], recoveredVertices) && !isInArray(neighbours[j], newInf)) {
                    // System.out.println("adding to newinf: " + neighbours[j]);
                    newInf[newInfLen] = neighbours[j];
                    newInfLen++;

                }
            }

        }
        // System.out.println("returning uypdateinf");

        return newInf;
    }

    private String[] updateRecovered(AbstractGraph graph, float recoverProb) {
        String[] newRec = new String[numVertex];
        int newRecLen = 0;

        for (int i = 0; i < infLen; i++) {
            if (randomResultGenerator(recoverProb, rand)) {
                newRec[newRecLen] = infectedVertices[i];
                newRecLen++;
            }
        }

        return newRec;
    }

    private String[] unisonOfArrays(String[] a, String[] b) {
        int aLength = getArrayLengthNonNull(a);
        int bLength = getArrayLengthNonNull(b);

        for (int i = 0; i < bLength; i++) {
            if (!isInArray(b[i], a)) {
                a[aLength] = b[i];
                aLength++;
            }
        }

        return a;
    }

    private String[] minusArrays(String[] a, String[] b) {
        int bLength = getArrayLengthNonNull(b);

        for (int i = 0; i < bLength; i++) {
            if (isInArray(b[i], a)) {
                removeElement(b[i], a);
            }
        }

        return a;
    }

    private boolean updateStop(int prevInf, int prevRec) {
        int infVertLen = getArrayLengthNonNull(infectedVertices);
        int recVertLen = getArrayLengthNonNull(recoveredVertices);
        if (infVertLen == 0 && (infVertLen == prevInf || recVertLen == prevRec)) {
            return false;
        }
        return true;

    }

    private int getArrayLengthNonNull(String[] array) {
        int length = 0;

        for (String s : array) {
            if (s != null) {
                length++;
            } else {

                return length;
            }
        }

        return length;
    }

    private boolean randomResultGenerator(float probability, Random rand) {
        float randVal = rand.nextFloat();
        if (randVal < probability) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isInArray(String checkStr, String[] array) {
        for (String s : array) {
            if (s != null) {
                if (s.equals(checkStr)) {
                    return true;
                }
            }
        }
        return false;

    }

    // private int getIndexInArray(String checkStr, String[] array) {
    // for (int i = 0; i < getArrayLengthNonNull(array); i++) {
    // if (array[i].equals(checkStr)) {
    // return i;
    // }
    // }
    // return -1;

    // }

    private void removeElement(String removeString, String[] array) {
        int arrayLen = getArrayLengthNonNull(array);
        for (int i = 0; i < arrayLen; i++) {
            if (array[i] == removeString) {
                for (int j = 0; j < arrayLen - i; j++) {
                    array[i + j] = array[i + j + 1];
                }
                array[arrayLen] = null;
            }
        }
    }

    private String arrayToString(String[] array) {
        String arrayString = "";
        for (int i = 0; i < getArrayLengthNonNull(array); i++) {
            arrayString = arrayString + array[i] + " ";
        }
        return arrayString.trim();
    }

} // end of class SIRModel
