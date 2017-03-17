package algvis.ds.dictionaries.btree;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import algvis.ui.VisPanel;

public class a234TreeTest extends BTreeBaseTest {

    @Before
    public void setUp() throws Exception {
        selectDsMenuByName(a234Tree.dsName);
        tree = (BTree) getActiveDataStructure();
    }

    @Test
    public void testPanelIsDisplayed() throws Exception {
        VisPanel panel = getActiveVisPanel(algVis);
        assertTrue(panel instanceof a234Panel);
    }
}
