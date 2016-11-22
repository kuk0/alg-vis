package algvis.ds.priorityqueues;

import static algvis.helper.ReflectionHelper.getFieldValue;

import org.junit.Before;

import algvis.internationalization.IButton;
import algvis.internationalization.IRadioButton;
import algvis.ui.BaseIntegrationTest;
import algvis.ui.VisPanel;

public abstract class HeapBaseTest extends BaseIntegrationTest {
    public PriorityQueue priorityQueue;

    @Before
    public void setUp() throws Exception {
        selectDsMenuByName(getDataStructureName());
        priorityQueue = (PriorityQueue) getActiveDataStructure();
        clearActivePanel();
        keys = new int[] {200, 100, 50, 300, 75};
        toogleMinHeapMode(false);
    }

    public abstract String getDataStructureName();

    public void toogleMinHeapMode(boolean minHeap) throws Exception {
        boolean minHeapModeSet = false;
        VisPanel activeVisPanel = getActiveVisPanel(algVis);
        if (activeVisPanel != null && 
                activeVisPanel.buttons != null && activeVisPanel.buttons instanceof PQButtons) {
            PQButtons buttons = (PQButtons) activeVisPanel.buttons;
            IRadioButton button = (IRadioButton) getFieldValue(buttons, minHeap ? "minB" : "maxB");
            if (button != null) {
                button.doClick();
                minHeapModeSet = true;
            }
        }
        if (!minHeapModeSet) {
            priorityQueue.minHeap = minHeap;
        }
    }

    public void decrKey(int delta) throws Exception {
        VisPanel activeVisPanel = getActiveVisPanel(algVis);
        if (activeVisPanel != null && 
                activeVisPanel.buttons != null && activeVisPanel.buttons instanceof PQButtons) {
            PQButtons buttons = (PQButtons) activeVisPanel.buttons;
            buttons.I.setText(String.valueOf(delta));
            IButton decrKeyB = (IButton) getFieldValue(buttons, "decrKeyB");
            IButton next = (IButton) getFieldValue(buttons, "next");
            if (decrKeyB != null) {
                decrKeyB.doClick();
                Thread.sleep(buttonClickSleepTime);
                while (next.isEnabled()) {
                    next.doClick();
                    Thread.sleep(buttonClickSleepTime);
                }
            }
        }
    }
}
