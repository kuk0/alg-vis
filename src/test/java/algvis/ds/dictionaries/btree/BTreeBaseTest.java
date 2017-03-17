package algvis.ds.dictionaries.btree;

import algvis.ui.BaseIntegrationTest;

public class BTreeBaseTest extends BaseIntegrationTest {
    BTree tree;
    BNode rootNode, childNode1, childNode2, childNode3;

    protected void updateRootNodes() {
        if (tree != null) {
            rootNode = (BNode) tree.getRoot();
            if (rootNode != null && rootNode.c != null) {
                childNode1 = rootNode.numChildren > 0 && rootNode.c.length > 0 ? rootNode.c[0] : null;
                childNode2 = rootNode.numChildren > 1 && rootNode.c.length > 1 ? rootNode.c[1] : null;
                childNode3 = rootNode.numChildren > 2 && rootNode.c.length > 2 ? rootNode.c[2] : null;
            }
        }
    }
}
