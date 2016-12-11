package algvis.ds.dictionaries.splaytree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import algvis.ui.BaseIntegrationTest;

public class SplayTreeTest extends BaseIntegrationTest {
    SplayTree splayTree;
    SplayNode rootNode, leftNode, rightNode;

    @Before
    public void setUp() throws Exception {
        selectDsMenuByName(SplayTree.dsName);
        splayTree = (SplayTree) getActiveDataStructure();
        clearActivePanel();
        keys = new int[] {50, 300, 70, 60, 200};
    }

    @Test
    public void testInsert() throws Exception {
        insertArray(0, keys.length);
        updateRootNodes();
        assertEquals(200, rootNode.getKey());
        assertEquals(70, leftNode.getKey());
        assertEquals(300, rightNode.getKey());

        // insert duplicated key -> size remains
        insert(keys[0]);
        updateRootNodes();
        assertEquals(5, rootNode.size);
    }

    @Test
    public void testDelete() throws Exception {
        insertArray(0, keys.length);

        // delete invalid key -> splay 50
        delete(1);
        updateRootNodes();
        assertEquals(5, rootNode.size);
        assertEquals(50, rootNode.getKey());
        assertNull(leftNode);
        assertEquals(200, rightNode.getKey());

        // delete all keys
        for (int i = 0; i < keys.length; i++) {
            delete(keys[i]);
        }
        updateRootNodes();
        assertNull(rootNode);

        // delete on an empty tree -> nothing happens
        delete(keys[0]);
        updateRootNodes();
        assertNull(rootNode);
    }

    @Test
    public void testDeleteMinorCase01() throws Exception {
        keys = new int[] {50, 75, 100, 200, 60};
        insertArray(0, keys.length);
        delete(keys[1]);
    }

    @Test
    public void testFind() throws Exception {
        // find in an empty tree
        find(keys[0]);
        HashMap<String, Object> result = ((SplayFind) splayTree.A).getResult();
        SplayNode foundNode = (SplayNode) result.get("node");
        assertNull(foundNode);

        insert(keys[0]);

        // find existent node
        find(keys[0]);
        result = ((SplayFind) splayTree.A).getResult();
        foundNode = (SplayNode) result.get("node");
        assertNotNull(foundNode);
        assertEquals(keys[0], foundNode.getKey());

        // find nonexistent node
        find(keys[1]);
        result = ((SplayFind) splayTree.A).getResult();
        foundNode = (SplayNode) result.get("node");
        assertNull(foundNode);
    }

    @Test
    public void testGetSetW1W2() throws Exception {
        insertArray(0, keys.length);
        updateRootNodes();
        splayTree.setW1(rootNode);
        splayTree.setW2(leftNode);
        assertEquals(keys[4], splayTree.getW1().getKey());
        assertEquals(keys[2], splayTree.getW2().getKey());    	
    }

    private void updateRootNodes() {
        if (splayTree != null) {
            rootNode = (SplayNode) splayTree.getRoot();
            if (rootNode != null) {
                leftNode = rootNode.getLeft();
                rightNode = rootNode.getRight();
            }
        }
    }
}
