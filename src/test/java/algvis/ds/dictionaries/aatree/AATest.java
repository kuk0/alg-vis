package algvis.ds.dictionaries.aatree;

import static algvis.helper.ReflectionHelper.getFieldValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import algvis.internationalization.ICheckBox;
import algvis.ui.BaseIntegrationTest;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

public class AATest extends BaseIntegrationTest {
    AA aa;
    AANode rootNode, leftNode, rightNode;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        selectDsMenuByName(AA.dsName);
        aa = (AA) getActiveDataStructure();
        turnOnMode23();
        clearActivePanel();
        keys = new int[] {200, 100, 300, 150, 125, 50, 75};
    }

    @Test
    public void testInsertRoot() throws Exception {
        insertArray(0, 0);
        updateRootNodes();
        assertNotNull(rootNode);
        assertTrue(rootNode.isLeaf());
        assertEquals(keys[0], rootNode.getKey());
        assertEquals(1, rootNode.getLevel());
    }

    @Test
    public void testInsertRootRight() throws Exception {
        // right rotate after inserting keys[1]
        insertArray(0, 1);
        updateRootNodes();
        assertFalse(rootNode.isLeaf());
        assertEquals(keys[1], rootNode.getKey());
        assertEquals(1, rootNode.getLevel());

        assertNull(leftNode);

        assertNotNull(rightNode);
        assertTrue(rightNode.isLeaf());
        assertEquals(keys[0], rightNode.getKey());
        assertEquals(1, rightNode.getLevel());
    }

    @Test
    public void testInsertRootLeftRight() throws Exception {
        // left rotate after inserting keys[2]
        insertArray(0, 2);
        updateRootNodes();
        assertEquals(keys[0], rootNode.getKey());
        assertEquals(2, rootNode.getLevel());

        assertNotNull(leftNode);
        assertTrue(leftNode.isLeaf());
        assertEquals(keys[1], leftNode.getKey());
        assertEquals(1, leftNode.getLevel());

        assertNotNull(rightNode);
        assertTrue(rightNode.isLeaf());
        assertEquals(keys[2], rightNode.getKey());
        assertEquals(1, rightNode.getLevel());
    }

    @Test
    public void testDelete() throws Exception {
        insertArray(0, keys.length - 1);

        // right rotate at leftNode after deleting
        delete(keys[6]);
        updateRootNodes();
        assertEquals(keys[5], leftNode.getKey());
        assertEquals(1, leftNode.getLevel());
        assertNull(leftNode.getLeft());
        AANode lrNode = leftNode.getRight();
        assertEquals(keys[1], lrNode.getKey());
        assertEquals(1, lrNode.getLevel());

        // right rotate at rightNode after deleting
        delete(keys[2]);
        updateRootNodes();
        assertEquals(keys[3], rightNode.getKey());

        // delete root
        delete(keys[4]);
        updateRootNodes();
        assertEquals(keys[3], rootNode.getKey());

        delete(keys[5]);
        delete(keys[1]);
        updateRootNodes();
        assertEquals(keys[3], rootNode.getKey());
        assertEquals(1, rootNode.getLevel());
        assertNull(rootNode.getLeft());

        delete(keys[3]);
        updateRootNodes();
        assertTrue(rootNode.isLeaf());

        // delete all
        delete(keys[0]);
        updateRootNodes();
        assertNull(rootNode);

        // delete nothing
        delete(keys[0]);
        updateRootNodes();
        assertNull(rootNode);
    }

    @Test
    public void testDeleteRightNodeWithOnlyOneChild() throws Exception {
        keys = new int[] {50, 100, 200, 300};
        insertArray(0, keys.length - 1);
        updateRootNodes();
        assertFalse(rightNode.isLeaf());

        delete(keys[2]);
        updateRootNodes();
        assertTrue(rightNode.isLeaf());
        assertEquals(keys[3], rightNode.getKey());
    }

    @Test
    public void testDeleteNodeWithTwoChildrenMinorCase01() throws Exception {
        // the node to be deleted has 2 children
        // its right child has a left child
        keys = new int[] {200, 50, 250, 300, 225};
        insertArray(0, keys.length - 1);
        updateRootNodes();
        assertEquals(keys[0], rootNode.getKey());
        assertNotNull(rightNode.getLeft());
        assertNotNull(rightNode.getRight());

        delete(keys[0]);
        updateRootNodes();
        assertEquals(keys[4], rootNode.getKey());
        assertNull(rightNode.getLeft());
    }

    @Test
    public void testDeleteNodeWithTwoChildrenMinorCase02() throws Exception {
        // the node to be deleted has 2 children
        // and it is a right child of its parent
        keys = new int[] {200, 50, 250, 300, 225};
        insertArray(0, keys.length - 1);
        updateRootNodes();
        assertEquals(keys[0], rootNode.getKey());
        assertNotNull(rightNode.getLeft());
        assertNotNull(rightNode.getRight());

        // the right child of the node replaces it
        // rotate at this position after that 
        // and the value will be the left child's value
        delete(keys[2]);
        updateRootNodes();
        assertEquals(keys[4], rightNode.getKey());
        assertNull(rightNode.getLeft());
    }

    private void updateRootNodes() {
        if (aa != null) {
            rootNode = (AANode) aa.getRoot();
            if (rootNode != null) {
                leftNode = rootNode.getLeft();
                rightNode = rootNode.getRight();
            }
        }
    }

    private void turnOnMode23() {
        boolean b23Clicked = false;

        VisPanel activeVisPanel = getActiveVisPanel(algVis);
        if (activeVisPanel != null) {
            Buttons buttons = activeVisPanel.buttons;
            ICheckBox b23 = (ICheckBox) getFieldValue(buttons, "B23");
            if (b23 != null) {
                b23.doClick();
                b23Clicked = true;
            }
        }

        if (!b23Clicked) {
            aa.setMode23(true);
        }
    }
}
