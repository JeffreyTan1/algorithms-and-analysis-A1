import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Abstract class to allow you to implement common functionality or definitions.
 * All three graph implement classes extend this.
 *
 * Note, you should make sure to test after changes. Note it is optional to use
 * this file.
 *
 * @author Jeffrey Chan, 2021.
 */
public abstract class AbstractGraph implements ContactsGraph {
    public final int MAX_DIMEN = 15;
    public int numVertex = 0;
    public HashMap<String, Integer> labelIndexMap;
    public HashMap<Integer, String> indexLabelMap;
    public HashMap<String, SIRState> labelStateMap;

    public boolean isInArray(String checkStr, String[] array) {
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

} // end of abstract class AbstractGraph
