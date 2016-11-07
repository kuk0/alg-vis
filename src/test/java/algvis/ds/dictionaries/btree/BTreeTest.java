package algvis.ds.dictionaries.btree;

import static algvis.helper.ReflectionHelper.getFieldValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import javax.swing.JSpinner;

import org.junit.Before;
import org.junit.Test;

import algvis.ui.VisPanel;

public class BTreeTest extends BTreeBaseTest {

    @Before
    public void setUp() throws Exception {
        selectDsMenuByName(BTree.dsName);
        tree = (BTree) getActiveDataStructure();
        clearActivePanel();
        keys = new int[] {50, 30, 10, 70, 60, 25, 75, 80};
    }

    @Test
    public void testFind() throws Exception {
        // search on empty tree
        find(keys[0]);
        HashMap<String, Object> result = ((BFind) tree.A).getResult();
        assertNull(result.get("node"));

        setOrder(3);
        insertArray(0, keys.length - 1);

        // search root
        find(keys[4]);
        result = ((BFind) tree.A).getResult();
        BNode foundNode = (BNode) result.get("node");
        assertNotNull(foundNode);
        assertEquals(keys[4], foundNode.getKey());

        // search leaf
        find(keys[0]);
        result = ((BFind) tree.A).getResult();
        foundNode = (BNode) result.get("node");
        assertNotNull(foundNode);
        assertEquals(keys[0], foundNode.getKey());

        // search nonexistence node
        delete(keys[0]);
        find(keys[0]);
        result = ((BFind) tree.A).getResult();
        assertNull(result.get("node"));
    }

    private void setOrder(int order) throws Exception {
        boolean orderSet = false;
        VisPanel activeVisPanel = getActiveVisPanel(algVis);
        if (activeVisPanel != null && 
                activeVisPanel.buttons != null && activeVisPanel.buttons instanceof BTreeButtons) {
            BTreeButtons buttons = (BTreeButtons) activeVisPanel.buttons;
            JSpinner OS = (JSpinner) getFieldValue(buttons, "OS");
            if (OS != null) {
                    OS.setValue(3);
                    orderSet = true;
            }
        }
        if (!orderSet) {
            tree.setOrder(3);
        }
    }
}
