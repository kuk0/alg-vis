package algvis.ds.priorityqueues.heap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import algvis.ds.priorityqueues.HeapBaseTest;
import algvis.ui.InputField;

public class HeapTest extends HeapBaseTest {
    Heap heap;
    HeapNode rootNode, leftNode, rightNode;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        heap = (Heap)priorityQueue;
    }

    @Test
    public void testInsert() throws Exception {
        insertArray(0, keys.length - 1);
        updateRootNodes();
        assertTrue(rootNode.prec(leftNode));
        assertTrue(rootNode.preceq(rootNode));
        assertTrue(rootNode.preceq(leftNode));
        assertEquals(keys[3], rootNode.getKey());
        assertEquals(keys[0], leftNode.getKey());
        assertEquals(keys[2], rightNode.getKey());
    }

    @Test
    public void testMinHeap() throws Exception {
        toogleMinHeapMode(true);
        assertTrue(heap.minHeap);

        insertArray(0, keys.length - 1);
        updateRootNodes();
        assertTrue(rootNode.prec(leftNode));
        assertTrue(rootNode.preceq(rootNode));
        assertTrue(rootNode.preceq(leftNode));
        assertEquals(keys[2], rootNode.getKey());
        assertEquals(keys[4], leftNode.getKey());
        assertEquals(keys[1], rightNode.getKey());
    }

    @Test
    public void testFullHeap() throws Exception {
        heap.setN(1000);
        insert(keys[0]);
        updateRootNodes();
        assertNull(rootNode);
    }

    @Test
    public void testMouseClicked() throws Exception {
        heap.mouseClicked(0, 0);
        assertNull(heap.chosen);

        insertArray(0, keys.length - 1);
        updateRootNodes();

        heap.mouseClicked(rootNode.x, rootNode.y);
        assertEquals(keys[3], heap.chosen.getKey());

        heap.mouseClicked(rootNode.x, rootNode.y);
        assertNull(heap.chosen);

        heap.mouseClicked(rootNode.x, rootNode.y);
        heap.mouseClicked(leftNode.x, leftNode.y);
        assertEquals(keys[0], heap.chosen.getKey());

        heap.mouseClicked(-100, -100);
        assertEquals(keys[0], heap.chosen.getKey());
    }

    @Test
    public void testDelete() throws Exception {
        // NOTE heap only has "delete root" functionality
        // delete on empty heap
        delete(0);
        assertEquals(0, heap.getN());

        // delete on heap with 1 node
        insert(keys[0]);
        assertEquals(1, heap.getN());
        delete(0);
        assertEquals(0, heap.getN());

        // delete on heap with many nodes
        insertArray(0, keys.length - 1);
        delete(0);
        updateRootNodes();
        assertEquals(keys[0], rootNode.getKey());
    }

    @Test
    public void testDeleteOnMinHeap() throws Exception {
        toogleMinHeapMode(true);
        insertArray(0, keys.length - 1);
        delete(0);
        updateRootNodes();
        assertEquals(keys[4], rootNode.getKey());
    }

    @Test
    public void testDeleteMinorCase01() throws Exception {
        keys = new int[] {200, 100, 150, 120, 110, 130};
        insertArray(0, keys.length - 1);
        delete(0);
        updateRootNodes();
        assertEquals(keys[2], rootNode.getKey());
    }

    @Test
    public void testDecrKey() throws Exception {
        insert(200);
        insert(100);
        updateRootNodes();
        heap.mouseClicked(leftNode.x, leftNode.y);
        decrKey(InputField.MAX);
        updateRootNodes();
        assertEquals(InputField.MAX, rootNode.getKey());
    }

    @Test
    public void testDecrKeyOnMinHeap() throws Exception {
        toogleMinHeapMode(true);
        insert(100);
        insert(200);
        updateRootNodes();
        heap.mouseClicked(leftNode.x, leftNode.y);
        decrKey(200);
        updateRootNodes();
        assertEquals(1, rootNode.getKey());
    }

    private void updateRootNodes() {
        if (heap != null) {
            rootNode = (HeapNode) heap.getRoot();
            if (rootNode != null) {
                leftNode = rootNode.getLeft();
                rightNode = rootNode.getRight();
            }
        }
    }

    @Override
    public String getDataStructureName() {
        return Heap.dsName;
    }
}
