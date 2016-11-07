package algvis.ui;

import static algvis.helper.ReflectionHelper.getFieldValue;

import javax.swing.JFrame;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import algvis.BaseTest;
import algvis.core.DataStructure;
import algvis.internationalization.IButton;
import algvis.internationalization.IMenuItem;
import algvis.internationalization.Stringable;

public abstract class BaseIntegrationTest extends BaseTest {
    static JFrame mainFrame;
    protected static AlgVis algVis;
    protected int[] keys;

    @BeforeClass
    public static void setUpTestSuite() throws Exception {
        mainFrame = showMainFrame();
        algVis = getAlgVis(mainFrame);
    }

    @AfterClass
    public static void tearDownTestSuite() throws Exception {
        if (algVis != null) {
            algVis = null;
        }
        if (mainFrame != null) {
            mainFrame.dispose();
            mainFrame = null;
        }
    }

    protected void selectDsMenuByName(String dsName) throws Exception {
        if (algVis == null || dsName == null || dsName.length() == 0) {
            return;
        }

        IMenuItem[] dsItems = (IMenuItem[]) getFieldValue(algVis, "dsItems");
        if (dsItems == null || dsItems.length == 0) {
            return;
        }

        for (IMenuItem menuItem : dsItems) {
            Stringable t = (Stringable) getFieldValue(menuItem, "t");
            if (t != null) {
                String s = (String) getFieldValue(t, "s");
                if (dsName.equals(s)) {
                    menuItem.doClick();
                    Thread.sleep(buttonClickSleepTime);
                    break;
                }
            }
        }
    }

    protected void clearActivePanel() throws Exception {
        clearVisPanel(getActiveVisPanel(algVis));
        Thread.sleep(buttonClickSleepTime);
    }

    protected DataStructure getActiveDataStructure() {
        VisPanel activeVisPanel = getActiveVisPanel(algVis);
        return activeVisPanel != null ? activeVisPanel.D : null;
    }

    protected void clickButton(String buttonName, int key) throws Exception {
        VisPanel activeVisPanel = getActiveVisPanel(algVis);
        if (activeVisPanel == null) {
            return;
        }

        Buttons buttons = activeVisPanel.buttons;

        IButton button = (IButton) getFieldValue(buttons, buttonName);

        if (button != null) {
            buttons.I.setText(String.valueOf(key));
            button.doClick();
            Thread.sleep(buttonClickSleepTime);
            while (buttons.next.isEnabled()) {
                buttons.next.doClick();
                Thread.sleep(buttonClickSleepTime);
            }
        }
    }

    protected void insert(int key) throws Exception {
        clickButton("insertB", key);
    }

    protected void find(int key) throws Exception {
        clickButton("findB", key);
    }

    protected void insertArray(int startIndex, int endIndex) throws Exception {
        startIndex = Math.max(startIndex, 0);
        endIndex = Math.min(endIndex, keys.length - 1);

        for (int i = startIndex; i <= endIndex; i++) {
            insert(keys[i]);
        }
    }

    protected void batchInsert(int... keys) throws Exception {
        if (keys == null) {
            return;
        }

        for (int i = 0; i < keys.length; i++) {
            insert(keys[i]);
        }
    }

    protected void delete(int key) throws Exception {
        clickButton("deleteB", key);
    }
}
