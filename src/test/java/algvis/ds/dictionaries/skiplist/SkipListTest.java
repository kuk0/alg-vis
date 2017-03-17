package algvis.ds.dictionaries.skiplist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import algvis.ui.BaseIntegrationTest;

public class SkipListTest extends BaseIntegrationTest {
    SkipList skipList;
    SkipNode leftNode, rightNode;

    @Before
    public void setUp() throws Exception {
        selectDsMenuByName(SkipList.dsName);
        skipList = (SkipList) getActiveDataStructure();
        clearActivePanel();
        keys = new int[] {50, 300, 70, 60, 200};
    }

    @Test
    public void testInsert() throws Exception {
        insertArray(0, 1);
        updateRootNodes();
        assertEquals(keys[0], leftNode.getKey());
        assertEquals(keys[1], rightNode.getKey());

        // insert duplicated key
        insert(keys[0]);
        updateRootNodes();
        assertEquals(keys[0], leftNode.getKey());
        assertFalse(keys[0] == leftNode.getRight().getKey());
    }

    @Test
    public void testDelete() throws Exception {
        insertArray(0, keys.length - 1);
        delete(keys[3]);
        updateRootNodes();
        assertEquals(keys[2], leftNode.getRight().getKey());

        // delete deleted key
        delete(keys[3]);
        updateRootNodes();
        assertEquals(keys[2], leftNode.getRight().getKey());
    }

    @Test
    public void testDeleteOnSkipListWithHeightGreaterThan1() throws Exception {
        do {
            clearActivePanel();
            insert(keys[0]);
        } while (skipList.height <= 1);

        delete(keys[0]);
        updateRootNodes();
        assertEquals(1, skipList.height);
        assertEquals(rightNode, leftNode.getLeft());
        assertEquals(leftNode, rightNode.getRight());
    }

    @Test
    public void testMouseClicked() throws Exception {
        insert(keys[0]);
        updateRootNodes();
        skipList.mouseClicked(leftNode.x, leftNode.y);
        assertEquals(keys[0], skipList.chosen.getKey());
    }

    @Test
    public void testFind() throws Exception {
        insert(keys[0]);

        // find existent node
        find(keys[0]);
        HashMap<String, Object> result = ((SkipFind) skipList.A).getResult();
        SkipNode foundNode = (SkipNode) result.get("node");
        assertNotNull(foundNode);
        assertEquals(keys[0], foundNode.getKey());

        // find nonexistent node
        find(keys[1]);
        result = ((SkipFind) skipList.A).getResult();
        foundNode = (SkipNode) result.get("node");
        assertNull(foundNode);
    }

    private void updateRootNodes() {
        if (skipList != null) {
            SkipNode rootNode = skipList.getRoot();
            while (rootNode.getDown() != null) {
                rootNode = rootNode.getDown();
            }
            leftNode = rootNode.getRight();

            SkipNode sentNode = skipList.sent;
            while (sentNode.getDown() != null) {
                sentNode = sentNode.getDown();
            }
            rightNode = sentNode.getLeft();
        }
    }
}
