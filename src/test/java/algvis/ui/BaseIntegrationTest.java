package algvis.ui;

import static algvis.helper.ReflectionHelper.getFieldValue;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;

import algvis.BaseTest;
import algvis.core.DataStructure;
import algvis.internationalization.IButton;
import algvis.internationalization.IMenuItem;
import algvis.internationalization.Stringable;

public abstract class BaseIntegrationTest extends BaseTest {
    JFrame mainFrame;
    protected AlgVis algVis;
    protected int[] keys;
    
    @Before
    public void setUp() throws Exception {
        mainFrame = showMainFrame();
        algVis = getAlgVis(mainFrame);
    }

    @After
    public void tearDown() throws Exception {
        if (mainFrame != null) {
            mainFrame.dispose();
        }
    }
    
    protected void selectDsMenuByName(String dsName) {
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
    
    protected void insertArray(int startIndex, int endIndex) throws Exception {
        startIndex = Math.max(startIndex, 0);
        endIndex = Math.min(endIndex, keys.length - 1);
        
        for (int i = startIndex; i <= endIndex; i++) {
            insert(keys[i]);
        }
    }
    
    protected void delete(int key) throws Exception {
        clickButton("deleteB", key);
    }
}
