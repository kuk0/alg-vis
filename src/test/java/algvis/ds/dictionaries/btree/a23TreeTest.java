package algvis.ds.dictionaries.btree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class a23TreeTest extends BTreeBaseTest {

    @Before
    public void setUp() throws Exception {
        selectDsMenuByName(a23Tree.dsName);
        tree = (BTree) getActiveDataStructure();
        clearActivePanel();
        keys = new int[] {50, 30, 10, 70, 60, 25, 80};
    }

    @Test
    public void testBuildTreeAndDelete() throws Exception {
        insertArray(0, keys.length - 1);
        insert(keys[0]);
        updateRootNodes();
        assertEquals(2, rootNode.numKeys);
        assertEquals(keys[1], rootNode.keys[0]);
        assertEquals(keys[4], rootNode.keys[1]);

        delete(keys[1]);
        updateRootNodes();
        assertEquals(2, rootNode.numKeys);
        assertEquals(keys[5], rootNode.keys[0]);
        assertEquals(keys[4], rootNode.keys[1]);

        delete(keys[6]);
        updateRootNodes();
        assertEquals(1, childNode3.numKeys);
        assertEquals(keys[3], childNode3.keys[0]);

        delete(keys[2]);
        updateRootNodes();
        assertEquals(1, rootNode.numKeys);
        assertEquals(keys[4], rootNode.keys[0]);

        delete(keys[0]);
        updateRootNodes();
        assertEquals(1, childNode1.numKeys);
        assertEquals(keys[5], childNode1.keys[0]);

        delete(keys[5]);
        updateRootNodes();
        assertEquals(2, rootNode.numKeys);
        assertEquals(keys[4], rootNode.keys[0]);
        assertEquals(keys[3], rootNode.keys[1]);
        assertNull(childNode1);
        assertNull(childNode2);
        assertNull(childNode3);

        delete(keys[3]);
        updateRootNodes();
        assertEquals(1, rootNode.numKeys);
        assertEquals(keys[4], rootNode.keys[0]);

        delete(keys[4]);
        updateRootNodes();
        assertNull(rootNode);
    }

    @Test
    public void testDeleteEmptyTree() throws Exception {
        delete(keys[0]);
        updateRootNodes();
        assertNull(rootNode);
    }

    @Test
    public void testNonexistentKey() throws Exception {
        insertArray(0, 0);
        delete(keys[1]);
        updateRootNodes();
        assertEquals(1, rootNode.numKeys);
        assertEquals(keys[0], rootNode.keys[0]);
        assertEquals(0, rootNode.numChildren);
    }

    @Test
    public void testDeleteChildNode2() throws Exception {
        keys = new int[] {100, 50, 75, 60, 55};
        insertArray(0, keys.length - 1);
        delete(keys[3]);
        updateRootNodes();

        assertEquals(1, rootNode.numKeys);
        assertEquals(keys[2], rootNode.keys[0]);

        assertEquals(2, childNode1.numKeys);
        assertEquals(keys[1], childNode1.keys[0]);
        assertEquals(keys[4], childNode1.keys[1]);

        assertEquals(1, childNode2.numKeys);
        assertEquals(keys[0], childNode2.keys[0]);

        assertNull(childNode3);
    }

    @Test
    public void testDeleteNodeWithGrantChildren() throws Exception {
        keys = new int[] {100, 50, 75, 60, 55, 40, 45};
        insertArray(0, keys.length - 1);
        delete(keys[4]);
        updateRootNodes();

        assertEquals(2, rootNode.numKeys);
        assertEquals(keys[6], rootNode.keys[0]);
        assertEquals(keys[3], rootNode.keys[1]);

        assertEquals(1, childNode1.numKeys);
        assertEquals(keys[5], childNode1.keys[0]);

        assertEquals(1, childNode2.numKeys);
        assertEquals(keys[1], childNode2.keys[0]);

        assertEquals(2, childNode3.numKeys);
        assertEquals(keys[2], childNode3.keys[0]);
        assertEquals(keys[0], childNode3.keys[1]);
    }

    @Test
    public void testDeleteMinorCase01() throws Exception {
        // the parent of node to be deleted has 2 children
        // the node to be deleted, the first child, has 1 child
        // its sibling, the second child, has 2 childrend
        keys = new int[] {100, 120, 150, 160};
        insertArray(0, keys.length - 1);
        delete(keys[0]);
        updateRootNodes();

        assertEquals(1, rootNode.numKeys);
        assertEquals(keys[2], rootNode.keys[0]);

        assertEquals(1, childNode1.numKeys);
        assertEquals(keys[1], childNode1.keys[0]);

        assertEquals(1, childNode2.numKeys);
        assertEquals(keys[3], childNode2.keys[0]);

        assertNull(childNode3);
    }

    @Test
    public void testDeleteMinorCase02() throws Exception {
        keys = new int[] {1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 2, 3};
        batchInsert(keys);
        delete(keys[3]);
        updateRootNodes();

        assertEquals(2, rootNode.numKeys);
        assertEquals(keys[1], rootNode.keys[0]);
        assertEquals(keys[7], rootNode.keys[1]);
    }

    @Test
    public void testDeleteMinorCase03() throws Exception {
        keys = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        batchInsert(keys);
        delete(keys[3]);
        updateRootNodes();

        assertEquals(2, rootNode.numKeys);
        assertEquals(keys[4], rootNode.keys[0]);
        assertEquals(keys[9], rootNode.keys[1]);
    }
}
