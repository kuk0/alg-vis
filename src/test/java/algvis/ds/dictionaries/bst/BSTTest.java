package algvis.ds.dictionaries.bst;

import static algvis.helper.ReflectionHelper.getFieldValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import algvis.internationalization.ICheckBox;
import algvis.ui.BaseIntegrationTest;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

public class BSTTest extends BaseIntegrationTest {

    BST bst;
    int rootKeyValue, leftKeyValue, rightKeyValue;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        selectDsMenuByName(BST.dsName);
        bst = (BST) getActiveDataStructure();
        turnOnOrder();
        clearActivePanel();

        keys = new int[] {100, 25, 125, 200, 45, 115, 15};
        rootKeyValue = keys[0];
        leftKeyValue = keys[1];
        rightKeyValue = keys[2];
    }

    @Test
    public void testInsertRoot() throws Exception {
        insert(rootKeyValue);

        assertNotNull(bst);

        BSTNode rootNode = bst.getRoot();
        assertNotNull(rootNode);
        assertEquals(rootKeyValue, rootNode.getKey());

        assertTrue(rootNode.isLeaf());
        assertFalse(rootNode.isLeft());
    }

    @Test
    public void testInsertRootAndLeft() throws Exception {
        insert(rootKeyValue);
        insert(leftKeyValue);

        BSTNode rootNode = bst.getRoot();
        BSTNode leftNode = rootNode.getLeft();

        assertFalse(rootNode.isLeaf());

        assertNotNull(leftNode);
        assertEquals(leftKeyValue, leftNode.getKey());
        assertTrue(leftNode.isLeaf());
        assertTrue(leftNode.isLeft());

        assertNull(rootNode.getRight());
    }

    @Test
    public void testInsertRootAndRight() throws Exception {
        insert(rootKeyValue);
        insert(rightKeyValue);

        BSTNode rootNode = bst.getRoot();
        BSTNode rightNode = rootNode.getRight();

        assertFalse(rootNode.isLeaf());

        assertNotNull(rightNode);
        assertEquals(rightKeyValue, rightNode.getKey());
        assertTrue(rightNode.isLeaf());
        assertFalse(rightNode.isLeft());

        assertNull(rootNode.getLeft());
    }

    @Test
    public void testInsertRootLeftRight() throws Exception {
        insert(rootKeyValue);
        insert(leftKeyValue);
        insert(rightKeyValue);

        BSTNode rootNode = bst.getRoot();
        BSTNode leftNode = rootNode.getLeft();
        BSTNode rightNode = rootNode.getRight();

        assertFalse(rootNode.isLeaf());

        assertNotNull(leftNode);
        assertEquals(leftKeyValue, leftNode.getKey());
        assertTrue(leftNode.isLeaf());
        assertTrue(leftNode.isLeft());

        assertNotNull(rightNode);
        assertEquals(rightKeyValue, rightNode.getKey());
        assertTrue(rightNode.isLeaf());
        assertFalse(rightNode.isLeft());
    }

    @Test
    public void testStructureAndOrder() throws Exception {
        insert(rootKeyValue);
        insert(leftKeyValue);
        insert(rightKeyValue);

        BSTNode rootNode = bst.getRoot();
        BSTNode leftNode = rootNode.getLeft();
        BSTNode rightNode = rootNode.getRight();

        assertTrue(rootNode.testStructure());
        assertTrue(rootNode.testOrder());

        leftNode.setKey(leftKeyValue + rightKeyValue);
        assertFalse(rootNode.testOrder());

        leftNode.setKey(leftKeyValue);
        rightNode.setKey(leftKeyValue - 1);
        assertFalse(rootNode.testOrder());

        leftNode.setParent(null);
        assertFalse(rootNode.testStructure());

        leftNode.setParent(rootNode);
        rightNode.setParent(null);
        assertFalse(rootNode.testStructure());
    }

    @Test
    public void testDelete() throws Exception {
        for (int key : keys) {
            insert(key);
        }
        delete(rightKeyValue);

        BSTNode rootNode = bst.getRoot();
        BSTNode rightNode = rootNode.getRight();
        assertNotEquals(rightKeyValue, rightNode.getKey());
        assertEquals(200, rightNode.getKey());
        assertFalse(rightNode.isLeaf());

        delete(rightNode.getLeft().getKey());
        assertTrue(rightNode.isLeaf());

        delete(leftKeyValue);
        BSTNode leftNode = rootNode.getLeft();
        assertNotEquals(leftKeyValue, leftNode.getKey());
        assertEquals(45, leftNode.getKey());
        assertFalse(leftNode.isLeaf());

        delete(leftNode.getKey());
        leftNode = rootNode.getLeft();
        assertTrue(leftNode.isLeaf());

        delete(rootKeyValue);
        rootNode = bst.getRoot();
        assertNotEquals(rootKeyValue, rootNode.getKey());
        assertEquals(200, rootNode.getKey());

        rootKeyValue = rootNode.getKey();
        delete(rootKeyValue);
        rootNode = bst.getRoot();
        assertNotEquals(rootKeyValue, rootNode.getKey());
        assertEquals(15, rootNode.getKey());

        rootKeyValue = rootNode.getKey();
        delete(rootKeyValue);
        rootNode = bst.getRoot();
        assertNull(rootNode);

        // delete anything when being null
        delete(keys[0]);
    }

    @Test
    public void testDeleteRightNodeWithOnlyLeftChild() throws Exception {
        keys = new int[] {10, 5, 20, 15};
        insertArray(0, keys.length);
        delete(keys[2]);

        BSTNode rootNode = bst.getRoot();
        BSTNode rightNode = rootNode.getRight();
        assertTrue(rightNode.isLeaf());
        assertEquals(keys[3], rightNode.getKey());
    }

    @Test
    public void testDeleteRightNodeWithOnlyRightChild() throws Exception {
        keys = new int[] {10, 5, 15, 20};
        insertArray(0, keys.length);
        delete(keys[2]);

        BSTNode rootNode = bst.getRoot();
        BSTNode rightNode = rootNode.getRight();
        assertTrue(rightNode.isLeaf());
        assertEquals(keys[3], rightNode.getKey());
    }

    @Test
    public void testDeleteRightNodeWithBothChildrenCase01() throws Exception {
        // rightNode has both left and right children
        // right child of rightNode, rrNode, has a left child
        // rrNode's left child also has a right child
        keys = new int[] {10, 20, 15, 35, 25, 30};
        insertArray(0, keys.length - 1);
        delete(keys[1]);
        BSTNode rootNode = bst.getRoot();
        BSTNode rightNode = rootNode.getRight();
        BSTNode rrNode = rightNode.getRight();
        BSTNode rrLeftNode = rrNode.getLeft();
        assertEquals(keys[4], rightNode.getKey());
        assertEquals(keys[3], rrNode.getKey());
        assertEquals(keys[5], rrLeftNode.getKey());
    }

    @Test
    public void testDeleteRightNodeWithBothChildrenCase02() throws Exception {
        // rightNode has both left and right children
        // right child of rightNode, rrNode, has only a right child
        // rrNode's right child also has a right child
        keys = new int[] {10, 20, 15, 25, 30};
        insertArray(0, keys.length - 1);
        delete(keys[1]);
        BSTNode rootNode = bst.getRoot();
        BSTNode rightNode = rootNode.getRight();
        BSTNode rrNode = rightNode.getRight();
        assertEquals(keys[3], rightNode.getKey());
        assertEquals(keys[4], rrNode.getKey());
        assertTrue(rrNode.isLeaf());
    }

    @Test
    public void deleteNonExistentKey() throws Exception {
        keys = new int[] {10, 5, 8};
        insertArray(0, keys.length - 1);

        int nonExistentKey = 20;
        delete(nonExistentKey);
        BSTNode rootNode = bst.getRoot();
        assertEquals(keys[0], rootNode.getKey());
        assertNotNull(rootNode.getLeft());
        assertNull(rootNode.getRight());

        nonExistentKey = 3;
        delete(nonExistentKey);
        rootNode = bst.getRoot();
        BSTNode leftNode = rootNode.getLeft();
        assertNull(leftNode.getLeft());
        assertNotNull(leftNode.getRight());
    }

    @Test
    public void testMouseClicked() throws Exception {
        VisPanel activeVisPanel = getActiveVisPanel(algVis);
        Buttons buttons = activeVisPanel.buttons;
        insertArray(0, 2);
        BSTNode rootNode = bst.getRoot();
        BSTNode leftNode = rootNode.getLeft();
        BSTNode rightNode = rootNode.getRight();

        bst.mouseClicked(rootNode.x, rootNode.y);
        assertEquals(keys[0], Integer.parseInt(buttons.I.getText()));

        bst.mouseClicked(leftNode.x, leftNode.y);
        assertEquals(keys[1], Integer.parseInt(buttons.I.getText()));

        bst.mouseClicked(rightNode.x, rightNode.y);
        assertEquals(keys[2], Integer.parseInt(buttons.I.getText()));
    }

    @Test
    public void testPreOrder() throws Exception {
        insertArray(0, keys.length - 1);

        int[] preOrderKeysExpect = new int[] {100, 25, 15, 45, 125, 115, 200};
        Vector<BSTNode> preOrderNodes = bst.getRoot().preorder();

        assertEquals(preOrderKeysExpect.length, preOrderNodes.size());
        for (int i = 0; i < preOrderKeysExpect.length; i++) {
            assertEquals(preOrderKeysExpect[i], preOrderNodes.get(i).getKey());
        }
    }

    @Test
    public void testFind() throws Exception {
        insertArray(0, 0);
        find(keys[0]);
        HashMap<String, Object> result = ((BSTFind) bst.A).getResult();
        assertNotNull(result);

        BSTNode foundNode = (BSTNode) result.get("node");
        assertNotNull(foundNode);
        assertEquals(keys[0], foundNode.getKey());
    }

    private void turnOnOrder() {
        boolean orderClicked = false;

        VisPanel activeVisPanel = getActiveVisPanel(algVis);
        if (activeVisPanel != null) {
            Buttons buttons = activeVisPanel.buttons;
            ICheckBox order = (ICheckBox) getFieldValue(buttons, "order");
            if (order != null) {
                order.doClick();
                orderClicked = true;
            }
        }

        if (!orderClicked) {
            bst.order = true;
        }
    }
}
