package algvis.ds.dictionaries.scapegoattree;

import static algvis.helper.ReflectionHelper.getFieldValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import javax.swing.JSpinner;

import org.junit.Before;
import org.junit.Test;

import algvis.ui.BaseIntegrationTest;
import algvis.ui.VisPanel;

public class GBTreeTest extends BaseIntegrationTest {
    GBTree tree;
    GBNode rootNode, leftNode, rightNode;

    @Before
    public void setUp() throws Exception {
        selectDsMenuByName(GBTree.dsName);
        tree = (GBTree) getActiveDataStructure();
        clearActivePanel();
        keys = new int[] {200, 100, 300, 50, 75};
    }

    @Test
    public void testInsert() throws Exception {
        insertArray(0, 2);
        delete(keys[1]);
        insert(keys[3]);
        insert(keys[1]);
        delete(keys[1]);
        insert(keys[4]);
        updateRootNodes();
        assertEquals(keys[0], rootNode.getKey());
        assertEquals(keys[4], leftNode.getKey());
        assertEquals(keys[3], leftNode.getLeft().getKey());
        assertEquals(keys[2], rightNode.getKey());

        insert(keys[0]);
        updateRootNodes();
        assertEquals(keys[0], rootNode.getKey());
        assertEquals(keys[4], leftNode.getKey());
        assertEquals(keys[2], rightNode.getKey());
    }

    @Test
    public void testInsertWithMinAlpha() throws Exception {
        Double minAlpha = 1.01;
        setAlpha(minAlpha);
        insertArray(0, 2);
        updateRootNodes();

        assertEquals(keys[0], rootNode.getKey());
        assertEquals(keys[1], leftNode.getKey());
        assertEquals(keys[2], rightNode.getKey());
    }

    @Test
    public void testFind() throws Exception {
        // search on empty tree
        find(keys[0]);
        HashMap<String, Object> result = ((GBFind) tree.A).getResult();
        assertNull(result.get("node"));

        insertArray(0, keys.length - 1);

        // search node
        find(keys[1]);
        result = ((GBFind) tree.A).getResult();
        GBNode foundNode = (GBNode) result.get("node");
        assertNotNull(foundNode);
        assertEquals(keys[1], foundNode.getKey());

        // search deleted node
        delete(keys[1]);
        find(keys[1]);
        result = ((GBFind) tree.A).getResult();
        foundNode = (GBNode) result.get("node");
        assertNull(foundNode);
    }

    @Test
    public void testFindNotFound() throws Exception {
        // search on empty tree
        find(keys[0]);
        HashMap<String, Object> result = ((GBFind) tree.A).getResult();
        assertNull(result.get("node"));

        insertArray(0, keys.length - 1);

        // search deleted node
        delete(keys[1]);
        find(keys[1]);
        result = ((GBFind) tree.A).getResult();
        GBNode foundNode = (GBNode) result.get("node");
        assertNull(foundNode);

        // search nonexistent node case 1
        find(150);
        result = ((GBFind) tree.A).getResult();
        foundNode = (GBNode) result.get("node");
        assertNull(foundNode);

        // search nonexistent node case 2
        find(15);
        result = ((GBFind) tree.A).getResult();
        foundNode = (GBNode) result.get("node");
        assertNull(foundNode);
    }

    @Test
    public void testDeleteRebuild() throws Exception {
        keys = new int[] {100, 200, 300};
        insertArray(0, keys.length - 1);
        delete(keys[2]);
        delete(keys[0]);
        updateRootNodes();
        assertNotNull(rootNode);
        assertNull(leftNode);
        assertNull(rightNode);
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        // TODO delete on empty tree
        delete(keys[0]);

        insertArray(0, keys.length - 1);

        // delete deleted node
        delete(keys[1]);
        delete(keys[1]);
        updateRootNodes();
        assertTrue(leftNode.getRight().isDeleted());

        // delete nonexistent node case 1
        delete(150);
        updateRootNodes();
        assertEquals(5, rootNode.size);

        // delete nonexistent node case 2
        delete(15);
        assertEquals(5, rootNode.size);
    }

    private void updateRootNodes() {
        if (tree != null) {
            rootNode = (GBNode) tree.getRoot();
            if (rootNode != null) {
                leftNode = rootNode.getLeft();
                rightNode = rootNode.getRight();
            }
        }
    }

    private void setAlpha(Double alpha) throws Exception {
        boolean alphaSet = false;
        VisPanel activeVisPanel = getActiveVisPanel(algVis);
        if (activeVisPanel != null && 
                activeVisPanel.buttons != null && activeVisPanel.buttons instanceof GBButtons) {
            GBButtons buttons = (GBButtons) activeVisPanel.buttons;
            JSpinner AS = (JSpinner) getFieldValue(buttons, "AS");
            if (AS != null) {
                AS.setValue(alpha);
                alphaSet = true;
            }
        }
        if (!alphaSet) {
            tree.alpha = alpha;
        }
    }
}
