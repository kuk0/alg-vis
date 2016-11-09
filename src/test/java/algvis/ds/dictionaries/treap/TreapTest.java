package algvis.ds.dictionaries.treap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import algvis.ds.dictionaries.bst.BSTFind;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.BaseIntegrationTest;

public class TreapTest extends BaseIntegrationTest {

    Treap treap;
    TreapNode rootNode, leftNode, rightNode;

    @Before
    public void setUp() throws Exception {
        selectDsMenuByName(Treap.dsName);
        treap = (Treap) getActiveDataStructure();
        clearActivePanel();
        keys = new int[] {100, 200, 50};
    }

    @Test
    public void testInsertRoot() throws Exception {
        int key = keys[0];
        double expectedProbability = (new Random(key)).nextDouble();
        insert(key);
        updateRootNodes();
        assertEquals(expectedProbability, rootNode.p, 0);
    }

    @Test
    public void testDelete() throws Exception {
        insertArray(0, keys.length - 1);

        delete(keys[2]);
        updateRootNodes();
        assertEquals(keys[0], leftNode.getKey());

        delete(keys[1]);
        updateRootNodes();
        assertNull(leftNode);
        assertNull(rightNode);

        delete(keys[0]);
        updateRootNodes();
        assertNull(rootNode);
    }

    @Test
    public void testDeleteMinorCase01() throws Exception {
        keys = new int[] {50, 25, 5};
        insertArray(0, keys.length - 1);
        delete(keys[1]);
        updateRootNodes();
        assertEquals(keys[2], rootNode.getKey());
    }

    @Test
    public void testDeleteMinorCase02() throws Exception {
        keys = new int[] {550, 650, 750};
        insertArray(0, keys.length - 1);
        delete(keys[1]);
        updateRootNodes();
        assertEquals(keys[2], rootNode.getKey());
    }

    @Test
    public void testFind() throws Exception {
        insert(keys[0]);
        find(keys[0]);
        HashMap<String, Object> result = ((BSTFind) treap.A).getResult();
        assertNotNull(result);

        BSTNode foundNode = (BSTNode) result.get("node");
        assertNotNull(foundNode);
        assertEquals(keys[0], foundNode.getKey());
    }

    private void updateRootNodes() {
        if (treap != null) {
            rootNode = (TreapNode) treap.getRoot();
            if (rootNode != null) {
                leftNode = rootNode.getLeft();
                rightNode = rootNode.getRight();
            }
        }
    }
}
