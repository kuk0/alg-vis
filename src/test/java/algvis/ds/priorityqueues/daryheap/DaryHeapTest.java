package algvis.ds.priorityqueues.daryheap;

import static algvis.helper.ReflectionHelper.getFieldValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.JSpinner;

import org.junit.Before;
import org.junit.Test;

import algvis.core.Node;
import algvis.ds.priorityqueues.HeapBaseTest;
import algvis.ui.InputField;
import algvis.ui.VisPanel;

public class DaryHeapTest extends HeapBaseTest {
    DaryHeap daryHeap;
    DaryHeapNode rootNode, childNode1, childNode2, childNode3;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        daryHeap = (DaryHeap)priorityQueue;
        setOrder(3);
        keys = new int [] {200, 100, 150, 300, 500, 250, 75};
    }

    @Test
    public void testInsert() throws Exception {
        insertArray(0, 2);
        updateRootNodes();
        assertEquals(keys[0], rootNode.getKey());
        assertEquals(keys[1], childNode1.getKey());
        assertEquals(keys[2], childNode2.getKey());
        assertNull(childNode3);
        assertTrue(rootNode.preceq(childNode1));
    }

    @Test
    public void testInsertOnMinHeap() throws Exception {
        toogleMinHeapMode(true);
        insertArray(0, 2);
        updateRootNodes();
        assertEquals(keys[1], rootNode.getKey());
        assertEquals(keys[0], childNode1.getKey());
        assertEquals(keys[2], childNode2.getKey());
        assertNull(childNode3);
        assertTrue(rootNode.preceq(childNode1));
    }

    @Test
    public void testInsertFullHeap() throws Exception {
        insert(keys[0]);
        daryHeap.root.nnodes = 1000;
        insert(keys[1]);
        assertTrue(daryHeap.root.c.isEmpty());
    }

    @Test
    public void testMouseClicked() throws Exception {
        daryHeap.mouseClicked(0, 0);
        assertNull(daryHeap.chosen);

        insertArray(0, 1);
        updateRootNodes();

        daryHeap.mouseClicked(rootNode.x, rootNode.y);
        assertEquals(keys[0], daryHeap.chosen.getKey());

        daryHeap.mouseClicked(rootNode.x, rootNode.y);
        assertNull(daryHeap.chosen);

        daryHeap.mouseClicked(rootNode.x, rootNode.y);
        daryHeap.mouseClicked(childNode1.x, childNode1.y);
        assertEquals(keys[1], daryHeap.chosen.getKey());

        daryHeap.mouseClicked(-100, -100);
        assertEquals(keys[1], daryHeap.chosen.getKey());
    }

    @Test
    public void testDelete() throws Exception {
        // NOTE heap only has "delete root" functionality
        // delete on empty heap
        delete(0);
        assertNull(daryHeap.root);

        // delete on heap with 1 node
        insert(keys[0]);
        assertNotNull(daryHeap.root);
        delete(0);
        assertNull(daryHeap.root);

        // delete on heap with many nodes
        insertArray(0, keys.length - 1);
        delete(0);
        updateRootNodes();
        assertEquals(keys[3], rootNode.getKey());
    }

    @Test
    public void testDeleteOnMinHeap() throws Exception {
        toogleMinHeapMode(true);

        // delete on heap with 1 node
        insert(keys[0]);
        assertNotNull(daryHeap.root);
        delete(0);
        assertNull(daryHeap.root);

        // delete on heap with many nodes
        insertArray(0, keys.length - 1);
        delete(0);
        updateRootNodes();
        assertEquals(keys[1], rootNode.getKey());
    }

    @Test
    public void testDecrKey() throws Exception {
        insert(keys[0]);
        insert(keys[1]);
        updateRootNodes();
        daryHeap.mouseClicked(childNode1.x, childNode1.y);
        decrKey(InputField.MAX);
        updateRootNodes();
        assertEquals(InputField.MAX, rootNode.getKey());
    }

    @Test
    public void testDecrKeyOnMinHeap() throws Exception {
        toogleMinHeapMode(true);
        insert(keys[1]);
        insert(keys[0]);
        updateRootNodes();
        daryHeap.mouseClicked(childNode1.x, childNode1.y);
        decrKey(200);
        updateRootNodes();
        assertEquals(1, rootNode.getKey());
    }

    @Test
    public void testNodeOrder() throws Exception {
        insertArray(0, 2);
        updateRootNodes();
        assertEquals(1, childNode2.order());
        assertEquals(-5, rootNode.order());
    }

    @Test
    public void testNextNeighbour() throws Exception {
        keys = new int [] {200, 100, 150, 300, 500, 250, 75, 125};
        insertArray(0, keys.length - 1);
        updateRootNodes();
        assertEquals(keys[7], childNode1.nextNeighbour().getKey());
    }

    @Test
    public void testPrevNeighbour() throws Exception {
        insert(keys[0]);
        assertNull(daryHeap.root.prevneighbour());

        insertArray(1, keys.length - 1);
        updateRootNodes();
        assertEquals(keys[3], childNode2.prevneighbour().getKey());

        DaryHeapNode firstChildOfFirstChild = childNode1.c.get(0);
        assertEquals(keys[0], firstChildOfFirstChild.prevneighbour().getKey());
    }

    @Test
    public void testCalcTree() throws Exception {
        insertArray(0, 1);
        rootNode = (DaryHeapNode) daryHeap.root;
        rootNode.calcTree();
        assertEquals(2, rootNode.nnodes);
        assertEquals(2, Integer.parseInt(getFieldValue(rootNode, "height").toString()));
    }

    @Test
    public void testGetBoundingBox() throws Exception {
        assertNull(daryHeap.getBoundingBox());

        insert(keys[0]);
        assertEquals(daryHeap.root.getBoundingBox(), daryHeap.getBoundingBox());
    }

    @Test
    public void testEndAnimation() throws Exception {
        insert(keys[0]);
        daryHeap.root.setState(Node.DOWN);
        daryHeap.endAnimation();
        assertEquals(Node.OUT, daryHeap.root.state);
    }

    @Test
    public void testIsAnimationDone() throws Exception {
        insert(keys[0]);
        rootNode = daryHeap.root;
        rootNode.setState(Node.INVISIBLE);
        assertTrue(daryHeap.isAnimationDone());
    }

    private void updateRootNodes() {
        if (daryHeap != null) {
            rootNode = (DaryHeapNode) daryHeap.root;
            if (rootNode != null && rootNode.c != null) {
                int rootChildCount = rootNode.c.size();
                childNode1 = rootChildCount > 0 ? rootNode.c.get(0) : null;
                childNode2 = rootChildCount > 1 ? rootNode.c.get(1) : null;
                childNode3 = rootChildCount > 2 ? rootNode.c.get(2) : null;
            }
        }
    }

    private void setOrder(int order) throws Exception {
        boolean orderSet = false;
        VisPanel activeVisPanel = getActiveVisPanel(algVis);
        if (activeVisPanel != null && 
                activeVisPanel.buttons != null && activeVisPanel.buttons instanceof DaryHeapButtons) {
            DaryHeapButtons buttons = (DaryHeapButtons) activeVisPanel.buttons;
            JSpinner OS = (JSpinner) getFieldValue(buttons, "OS");
            if (OS != null) {
                    OS.setValue(order);
                    orderSet = true;
            }
        }
        if (!orderSet) {
            daryHeap.setOrder(order);
        }
    }

    @Override
    public String getDataStructureName() {
        return DaryHeap.dsName;
    }
}
