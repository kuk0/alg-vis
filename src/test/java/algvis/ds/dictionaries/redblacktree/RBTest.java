package algvis.ds.dictionaries.redblacktree;

import static algvis.helper.ReflectionHelper.getFieldValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import algvis.ds.dictionaries.bst.BSTFind;
import algvis.internationalization.ICheckBox;
import algvis.ui.BaseIntegrationTest;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

public class RBTest extends BaseIntegrationTest {

    RB rb;
    RBNode rootNode, leftNode, rightNode;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        selectDsMenuByName(RB.dsName);
        rb = (RB) getActiveDataStructure();
        turnOnMode24();
        clearActivePanel();
        keys = new int[] {4, 7, 12, 15, 3, 5, 14, 18, 16, 17};
    }

    @Test
    public void testInsertRoot() throws Exception {
        insertArray(0, 0);
        updateRootNodes();

        assertNotNull(rootNode);
        assertFalse(rootNode.isRed());
        assertTrue(rootNode.isLeaf());
        assertEquals(rb.NULL, rootNode.getLeft2());
        assertEquals(rb.NULL, rootNode.getRight2());
        assertEquals(keys[0], rootNode.getKey());

        // nothing change when duplicatedly inserting
        insertArray(0, 0);
        updateRootNodes();
        assertTrue(rootNode.isLeaf());
    }

    @Test
    public void testInsertRootRight() throws Exception {
        insertArray(0, 1);
        updateRootNodes();

        assertFalse(rootNode.isRed());
        assertEquals(keys[0], rootNode.getKey());

        assertNotNull(rightNode);
        assertTrue(rightNode.isRed());
        assertTrue(rightNode.isLeaf());
        assertEquals(keys[1], rightNode.getKey());
    }

    @Test
    public void testInsertRootLeftRight() throws Exception {
        // left rotate at root after inserting keys[2]
        insertArray(0, 2);
        updateRootNodes();

        assertFalse(rootNode.isRed());
        assertEquals(keys[1], rootNode.getKey());
        assertNotEquals(rb.NULL, rootNode.getLeft2());
        assertNotEquals(rb.NULL, rootNode.getRight2());

        assertNotNull(leftNode);
        assertTrue(leftNode.isRed());
        assertTrue(leftNode.isLeaf());
        assertEquals(keys[0], leftNode.getKey());

        assertNotNull(rightNode);
        assertTrue(rightNode.isRed());
        assertTrue(rightNode.isLeaf());
        assertEquals(keys[2], rightNode.getKey());
    }

    @Test
    public void testRecolor() throws Exception {
        // recolor new node's parent after inserting keys[3]
        insertArray(0, 3);
        updateRootNodes();

        assertNotNull(rightNode);
        assertFalse(rightNode.isRed());
        assertFalse(rightNode.isLeaf());
        assertEquals(rb.NULL, rightNode.getLeft2());
        assertNotEquals(rb.NULL, rightNode.getRight2());

        assertNotNull(rightNode.getRight());
        RBNode rrNode = rightNode.getRight();
        assertTrue(rrNode.isRed());
        assertEquals(keys[3], rrNode.getKey());
    }

    @Test
    public void testDelete() throws Exception {
        insertArray(0, keys.length - 1);
        updateRootNodes();
        assertTrue(rootNode.testRedBlack());

        delete(keys[1]);
        updateRootNodes();
        assertTrue(leftNode.isRed());
        assertEquals(keys[0], leftNode.getKey());

        delete(keys[8]);
        updateRootNodes();
        assertTrue(rightNode.isRed());
        assertEquals(keys[9], rightNode.getKey());

        delete(keys[6]);
        updateRootNodes();
        assertFalse(rootNode.isRed());
        assertEquals(keys[3], rootNode.getKey());
        assertFalse(rightNode.isRed());
        assertNull(rightNode.getLeft());
        assertNotNull(rightNode.getRight());
        assertTrue(rightNode.getRight().isRed());

        delete(keys[7]);
        updateRootNodes();
        assertTrue(rightNode.isLeaf());

        delete(keys[9]);
        updateRootNodes();
        assertTrue(rightNode.isRed());
        assertEquals(keys[2], rightNode.getKey());

        delete(keys[5]);
        updateRootNodes();
        assertFalse(rightNode.isRed());

        delete(keys[2]);
        updateRootNodes();
        assertEquals(keys[3], rightNode.getKey());

        delete(keys[3]);
        updateRootNodes();
        assertTrue(leftNode.isRed());

        delete(keys[0]);
        updateRootNodes();
        assertEquals(keys[4], rootNode.getKey());
        assertTrue(rootNode.isLeaf());

        delete(keys[4]);
        updateRootNodes();
        assertNull(rootNode);

        // delete anything when being null
        delete(keys[0]);
    }

    @Test
    public void testDeleteALeftChildOnPath() throws Exception {
        // the node to be deleted is not a leaf, not root and is a left child
        keys = new int[] {20, 15, 25, 8};
        insertArray(0, keys.length - 1);
        delete(keys[1]);
        updateRootNodes();
        assertEquals(keys[3], leftNode.getKey());
    }

    @Test
    public void testDeleteMinorCase01() throws Exception {
        // delete at rootNode
        // rightNode is black
        // left most child of rightNode is black
        // right child of rightNode is red
        keys = new int[] {100, 80, 150, 70, 86, 125, 200, 60, 75, 85, 88, 175, 210};
        insertArray(0, keys.length - 1);

        // modify color but keep the tree a Red-Black tree
        boolean red = true;
        setRedByKey(keys[1], !red);
        setRedByKey(keys[3], red);
        setRedByKey(keys[4], red);
        setRedByKey(keys[7], !red);
        setRedByKey(keys[8], !red);
        setRedByKey(keys[9], !red);
        setRedByKey(keys[10], !red);
        setRedByKey(keys[11], !red);
        setRedByKey(keys[12], !red);
        updateRootNodes();
        assertTrue(rootNode.testStructure());

        // delete at rootNode and verify
        delete(keys[0]);
        updateRootNodes();
        assertEquals(keys[5], rootNode.getKey());
        assertEquals(keys[2], rightNode.getKey());
        assertNull(rightNode.getLeft());
    }

    @Test
    public void testDeleteMinorCase02() throws Exception {
        // delete at rootNode
        // rightNode is red
        // left most child of rightNode is black
        // right child of rightNode is black
        // right right child of rightNode is black or NULL
        keys = new int[] {100, 80, 150, 70, 86, 125, 200, 60, 75, 85, 88, 175, 210};
        insertArray(0, keys.length - 1);

        // modify color but keep the tree a Red-Black tree
        boolean red = true;
        setRedByKey(keys[1], !red);
        setRedByKey(keys[2], !red);
        setRedByKey(keys[3], red);
        setRedByKey(keys[4], red);
        setRedByKey(keys[6], red);
        setRedByKey(keys[7], !red);
        setRedByKey(keys[8], !red);
        setRedByKey(keys[9], !red);
        setRedByKey(keys[10], !red);
        setRedByKey(keys[11], !red);
        setRedByKey(keys[12], !red);
        updateRootNodes();
        assertTrue(rootNode.testStructure());

        // delete at rootNode and verify
        delete(keys[0]);
        updateRootNodes();
        assertEquals(keys[5], rootNode.getKey());
        assertEquals(keys[6], rightNode.getKey());
        assertEquals(keys[2], rightNode.getLeft().getKey());
    }

    @Test
    public void testDeleteMinorCase03() throws Exception {
        keys = new int[] {100, 80, 150, 76, 88, 140, 200, 75, 130, 145, 175, 202, 203};
        insertArray(0, keys.length - 1);

        // modify color but keep the tree a Red-Black tree
        boolean red = true;
        setRedByKey(keys[2], red);
        setRedByKey(keys[8], !red);
        setRedByKey(keys[9], !red);
        setRedByKey(keys[6], !red);

        updateRootNodes();
        assertTrue(rootNode.testStructure());

        delete(keys[2]);
        updateRootNodes();
        assertEquals(keys[10], rightNode.getKey());
        assertTrue(rightNode.isRed());
    }

    @Test
    public void testDeleteMinorCase04() throws Exception {
        keys = new int[] {100, 80, 150, 76, 88, 140, 200, 75, 130, 145, 175, 202, 201};
        insertArray(0, keys.length - 1);

        // modify color but keep the tree a Red-Black tree
        boolean red = true;
        setRedByKey(keys[2], red);
        setRedByKey(keys[8], !red);
        setRedByKey(keys[9], !red);
        setRedByKey(keys[6], !red);

        updateRootNodes();
        assertTrue(rootNode.testStructure());

        delete(keys[2]);
        updateRootNodes();
        assertEquals(keys[10], rightNode.getKey());
        assertTrue(rightNode.isRed());
    }

    @Test
    public void testDeleteMinorCase05() throws Exception {
        keys = new int[] {4, 7, 12, 15, 5, 14, 18, 16, 17};
        insertArray(0, keys.length - 1);
        delete(keys[1]);
        updateRootNodes();
        assertEquals(keys[5], rootNode.getKey());
        assertEquals(keys[4], leftNode.getKey());
        assertEquals(keys[7], rightNode.getKey());
    }

    @Test
    public void testColorCharater() throws Exception {
        insertArray(0, keys.length - 1);
        updateRootNodes();
        assertTrue(rootNode.testRedBlack());

        rootNode.setRed(true);
        assertFalse(rootNode.testRedBlack());

        rootNode.setRed(false);
        rightNode.getRight().setRed(true);
        assertFalse(rootNode.testRedBlack());
    }

    private void updateRootNodes() {
        if (rb != null) {
            rootNode = (RBNode) rb.getRoot();
            if (rootNode != null) {
                leftNode = rootNode.getLeft();
                rightNode = rootNode.getRight();
            }
        }
    }

    private void setRedByKey(int key, boolean red) {
        BSTFind find = new BSTFind(rb, key, rb.A);
        find.runAlgorithm();

        HashMap<String, Object> result = find.getResult();
        if (result != null && result.size() > 0) {
            RBNode rbNode = (RBNode) result.get("node");
            if (rbNode != null) {
                rbNode.setRed(red);
            }
        }
    }

    private void turnOnMode24() {
        boolean b24Clicked = false;

        VisPanel activeVisPanel = getActiveVisPanel(algVis);
        if (activeVisPanel != null) {
            Buttons buttons = activeVisPanel.buttons;
            ICheckBox b24 = (ICheckBox) getFieldValue(buttons, "B24");
            if (b24 != null) {
                b24.doClick();
                b24Clicked = true;
            }
        }

        if (!b24Clicked) {
            rb.mode24 = true;
        }
    }
}
