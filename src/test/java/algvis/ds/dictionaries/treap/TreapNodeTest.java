package algvis.ds.dictionaries.treap;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import algvis.core.Node;
import algvis.core.NodeColor;

public class TreapNodeTest {
    private TreapNode node;

    @Before
    public void setUp() throws Exception {
        node = new TreapNode(null,10, 0);
    }

    @Test
    public void testAll() {
        Node left = node.getLeft();
        Node right = node.getRight();
        
        assertEquals(left, null);
        assertEquals(right, null);
        
        String str = String.valueOf(Math.round(100 * node.p) / 100.0);
        assertEquals(node.getPriorityString(), str);
        
        node.setColor(NodeColor.NORMAL);
        node.setColor(NodeColor.BLUE);
        node.draw(null);
        
        node.setState(Node.INVISIBLE);
        node.draw(null);
        
        node.mark();
        node.unmark();
      }
}
