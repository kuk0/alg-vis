package algvis.ds.dictionaries.avltree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import algvis.ui.BaseIntegrationTest;

public class AVLTest extends BaseIntegrationTest {

    AVL avl;
    AVLNode rootNode, leftNode, rightNode;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        selectDsMenuByName(AVL.dsName);
        avl = (AVL) getActiveDataStructure();
        clearActivePanel();
        keys = new int[] {4, 2, 1, 5, 6, 9, 14, 20, 25};
    }

    @Test
    public void testInsertRoot() throws Exception {
        insertArray(0, 0);
        updateRootNodes();
        assertNotNull(rootNode);
        assertTrue(rootNode.isLeaf());
        assertEquals(keys[0], rootNode.getKey());
        assertFalse(rootNode.isLeft());
    }

    @Test
    public void testInsertRootLeft() throws Exception {
        insertArray(0, 1);
        updateRootNodes();
        assertEquals(keys[0], rootNode.getKey());
        assertNotNull(leftNode);
        assertEquals(keys[1], leftNode.getKey());
        assertNull(rightNode);
    }

    @Test
    public void testInsertRootLeftRight() throws Exception {
        insertArray(0, 2);
        updateRootNodes();
        assertFalse(rootNode.isLeaf());
        assertEquals(keys[1], rootNode.getKey());
        assertNotNull(leftNode);
        assertEquals(keys[2], leftNode.getKey());
        assertNotNull(rightNode);
        assertEquals(keys[0], rightNode.getKey());
    }

    @Test
    public void testRotate() throws Exception {
        insertArray(0, 3);
        updateRootNodes();
        assertFalse(rightNode.isLeaf());
        assertNull(rightNode.getLeft());

        // left rotate at right node
        insert(keys[4]);
        updateRootNodes();
        assertEquals(keys[3], rightNode.getKey());

        // left rotate at root
        insert(keys[5]);
        updateRootNodes();
        assertEquals(keys[3], rootNode.getKey());
        assertNotNull(leftNode);
        assertEquals(keys[1], leftNode.getKey());
        assertNotNull(leftNode.getLeft());
        assertEquals(keys[2], leftNode.getLeft().getKey());
        assertNotNull(leftNode.getRight());
        assertEquals(keys[0], leftNode.getRight().getKey());

        // left rotate at right node
        insert(keys[6]);
        updateRootNodes();
        assertEquals(keys[5], rightNode.getKey());
        assertNotNull(rightNode.getLeft());
        assertEquals(keys[4], rightNode.getLeft().getKey());
        assertNotNull(leftNode.getRight());
        assertEquals(keys[6], rightNode.getRight().getKey());

        // right rotate at rightNode.getRight
        insert(keys[7]);
        insert(keys[8]);
        updateRootNodes();
        assertNotNull(rightNode.getRight());
        AVLNode rrNode = rightNode.getRight();
        assertEquals(keys[7], rrNode.getKey());
        assertNotNull(rrNode.getLeft());
        assertEquals(keys[6], rrNode.getLeft().getKey());
        assertNotNull(leftNode.getRight());
        assertEquals(keys[8], rrNode.getRight().getKey());
    }

    @Test
    public void testDelete() throws Exception {
        insertArray(0, keys.length - 1);

        delete(keys[4]);
        updateRootNodes();
        assertEquals(keys[3], rootNode.getKey());
        assertEquals(keys[1], leftNode.getKey());
        assertEquals(keys[7], rightNode.getKey());
        assertNotNull(rightNode.getRight());

        delete(keys[7]);
        updateRootNodes();
        assertEquals(keys[6], rightNode.getKey());

        delete(keys[8]);
        updateRootNodes();
        assertEquals(keys[3], rootNode.getKey());
        assertEquals(keys[1], leftNode.getKey());

        delete(keys[3]);
        updateRootNodes();
        assertEquals(keys[5], rootNode.getKey());
        assertEquals(keys[6], rightNode.getKey());

        delete(keys[5]);
        updateRootNodes();
        assertEquals(keys[1], rootNode.getKey());
        assertEquals(keys[2], leftNode.getKey());
        assertEquals(keys[6], rightNode.getKey());

        delete(keys[1]);
        updateRootNodes();
        assertEquals(keys[0], rootNode.getKey());

        delete(keys[0]);
        updateRootNodes();
        assertEquals(keys[6], rootNode.getKey());

        delete(keys[6]);
        delete(keys[2]);
        updateRootNodes();
        assertNull(rootNode);

        // delete anything when being null
        delete(keys[0]);
    }

    @Test
    public void testRLRotateAfterDeleting() throws Exception {
        keys = new int[] {10, 5, 20, 15};
        insertArray(0, keys.length - 1);
        delete(keys[1]);
        updateRootNodes();
        assertEquals(keys[3], rootNode.getKey());
        assertEquals(keys[0], leftNode.getKey());
        assertEquals(keys[2], rightNode.getKey());
    }

    private void updateRootNodes() {
        if (avl != null) {
            rootNode = (AVLNode) avl.getRoot();
            if (rootNode != null) {
                leftNode = rootNode.getLeft();
                rightNode = rootNode.getRight();
            }
        }
    }
}
