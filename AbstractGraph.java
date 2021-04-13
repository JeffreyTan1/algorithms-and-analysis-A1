import java.io.PrintWriter;
import java.util.HashMap;

import javax.swing.CellEditor;

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
    // 500 + 20% = 600
    public final int MAX_DIMEN = 600;
    public int numVertex = 0;
    public HashMap<String, Integer> labelIndexMap;
    public HashMap<Integer, String> indexLabelMap;
    public HashMap<String, SIRState> labelStateMap;
    public String[] allEdges;

    public AbstractGraph() {
        labelIndexMap = new HashMap<String, Integer>();
        indexLabelMap = new HashMap<Integer, String>();
        labelStateMap = new HashMap<String, SIRState>();

    }

    public boolean isInArray(String checkStr, String[] array) {
        for (String s : array) {
            if (s != null) {
                if (s.equals(checkStr)) {
                    return true;
                }
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

    public String[] resizeArrayByLen(String[] array, int len) {
        String[] newArray = new String[len];
        for (int i = 0; i < len; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public int getNumVertex() {
        return numVertex;
    }

    public int getMAX_DIMEN() {
        return this.MAX_DIMEN;
    }

    public HashMap<String, Integer> getLabelIndexMap() {
        return this.labelIndexMap;
    }

    public HashMap<Integer, String> getIndexLabelMap() {
        return this.indexLabelMap;
    }

    public HashMap<String, SIRState> getLabelStateMap() {
        return this.labelStateMap;
    }

} // end of abstract class AbstractGraph
