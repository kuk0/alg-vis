package algvis.ds.rotations;

import static algvis.helper.ReflectionHelper.getFieldValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.undo.CannotRedoException;

import org.junit.Before;
import org.junit.Test;

import algvis.core.Node;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.internationalization.ICheckBox;
import algvis.ui.BaseIntegrationTest;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;
import algvis.ui.view.Layout;

public class RotationsTest extends BaseIntegrationTest {
    Rotations rotations;
    BSTNode rootNode, leftNode, rightNode;

    @Before
    public void setUp() throws Exception {
        selectDsMenuByName(Rotations.dsName);
        rotations = (Rotations) getActiveDataStructure();
        clearActivePanel();
        keys = new int[] {100, 25, 125, 200, 45, 115, 15};
    }

    @Test
    public void testInsertAndRotate() throws Exception {
        for (int i = 0; i < keys.length; i++) {
            doInsert(keys[i]);
        }
        // insert duplicated
        doInsert(keys[0]);
        updateRootNodes();
        assertEquals(keys[0], rootNode.getKey());

        // rotate invalid key
        doRotate(1);
        updateRootNodes();
        assertEquals(keys[0], rootNode.getKey());

        // rotate at root
        doRotate(keys[0]);
        updateRootNodes();
        assertEquals(keys[0], rootNode.getKey());

        // rotate at right
        doRotate(keys[2]);
        updateRootNodes();
        assertEquals(keys[2], rootNode.getKey());
        assertEquals(keys[0], leftNode.getKey());
        assertEquals(keys[3], rightNode.getKey());

        // rotate at left
        doRotate(keys[1]);
        updateRootNodes();
        assertEquals(keys[1], leftNode.getKey());
        BSTNode lrNode = leftNode.getRight();
        assertNotNull(lrNode);
        assertEquals(keys[0], lrNode.getKey());
    }

    @Test
    public void testRotateMinorCase01() throws Exception {
        for (int i = 0; i < keys.length; i++) {
            doInsert(keys[i]);
        }

        // rotate at left node then left of the new right node
        doRotate(keys[1]);
        doRotate(keys[4]);
        updateRootNodes();
        assertEquals(keys[4], rightNode.getKey());
        BSTNode rrNode = rightNode.getRight();
        assertNotNull(rrNode);
        assertEquals(keys[0], rrNode.getKey());
    }

    @Test
    public void testRotateMinorCase02() throws Exception {
        doInsert(keys[0]);
        doInsert(keys[1]);
        doRotate(keys[1]);
        updateRootNodes();
        assertEquals(keys[1], rootNode.getKey());
        assertEquals(keys[0], rightNode.getKey());
    }
    @Test
    public void testMouseClicked() throws Exception {
        // click on an empty tree
        doMouseClick(0, 0);
        assertNull(rotations.chosen);

        // mark a node
        doInsert(keys[0]);
        doInsert(keys[1]);
        updateRootNodes();
        doMouseClick(rootNode.x, rootNode.y);
        assertNotNull(rotations.chosen);
        assertEquals(keys[0], rotations.chosen.getKey());

        // unmark a node
        doMouseClick(rootNode.x, rootNode.y);
        assertNull(rotations.chosen);

        // click another to unmark current node
        doMouseClick(rootNode.x, rootNode.y);
        doMouseClick(leftNode.x, leftNode.y);
        assertEquals(keys[1], rotations.chosen.getKey());
    }

    @Test
    public void testMinorMethods() throws Exception {
        assertEquals(Layout.SIMPLE, rotations.getLayout());
        assertNull(rotations.getBoundingBox());
        assertTrue(rotations.isAnimationDone());
    }

    @Test
    public void testCheckboxes() throws Exception {
        doClickCheckbox("order");
        assertFalse(rotations.T.order);
        doClickCheckbox("order");

        doClickCheckbox("subtrees");
        assertFalse(rotations.subtrees);
        doClickCheckbox("subtrees");
    }

    @Test
    public void testEndAnimation() throws Exception {
        doInsert(keys[0]);
        doInsert(keys[1]);
        doInsert(keys[2]);
        updateRootNodes();

        leftNode.setState(Node.LEFT);
        rightNode.setState(Node.RIGHT);
        rotations.endAnimation();
        assertEquals(Node.OUT, leftNode.state);
        assertEquals(Node.OUT, rightNode.state);
    }

    private void updateRootNodes() {
        if (rotations != null && rotations.T != null) {
            rootNode = rotations.T.getRoot();
            leftNode = null;
            rightNode = null;
            if (rootNode != null) {
                leftNode = rootNode.getLeft();
                rightNode = rootNode.getRight();
            }
        }
    }

    private void doInsert(int key) throws Exception {
        rotations.insert(key);
        Thread.sleep(buttonClickSleepTime);
    }

    private void doRotate(int key) throws Exception {
        try {
            clickButton("rotB", key);
        } catch (CannotRedoException cannotRedoException) {
            cannotRedoException.printStackTrace();
        }
    }

    private void doClickCheckbox(String name) throws Exception {
        VisPanel activeVisPanel = getActiveVisPanel(algVis);
        if (activeVisPanel != null) {
            Buttons buttons = activeVisPanel.buttons;
            ICheckBox checkbox = (ICheckBox) getFieldValue(buttons, name);
            if (checkbox != null) {
                checkbox.doClick();
                Thread.sleep(buttonClickSleepTime);
            }
        }
    }

    private void doMouseClick(int x, int y) throws Exception {
        rotations.mouseClicked(x, y);
        Thread.sleep(buttonClickSleepTime);
    }
}
