package algvis.ds.dictionaries.btree;

import algvis.core.NodeColor;
import algvis.ui.view.REL;

public class BPlusFind extends BPlusAlg {

    public BPlusFind(BPlusTree T, int x) {
        super(T, x);
    }

    @Override
    public void runAlgorithm() {
        BPlusNode v;
        addToScene(v = new BPlusNode(T, K));
        v.setColor(NodeColor.FIND);
        setHeader("search");

        if (T.getRoot() == null) {
            v.goToRoot();
            addStep(T.getBoundingBoxDef(), 200, REL.TOP, "empty");
            pause();
            v.goDown();
            v.setColor(NodeColor.NOTFOUND);
            addStep(T.getBoundingBoxDef(), 200, REL.TOP, "notfound");
        } else {
            BNode w = T.getRoot();
            v.goTo(w);
            addStep(v, REL.TOP, "bstfindstart");
            pause();

            BNode d = null;

            while (true) {
                if (w.isIn(v.keys[0])) {
                    if (w.isLeaf()) {
                        addStep(w, REL.BOTTOM, "found");
                        v.goDown();
                        v.setColor(NodeColor.FOUND);
                        if (d != null) {
                            d.setColor(NodeColor.NORMAL);
                        }
                        break;
                    } else {
                        w.setColor(NodeColor.FOUND);
                        d = w;
                    }
                }
                if (w.isLeaf()) {
                    addStep(w, REL.BOTTOM, "notfound");
                    v.setColor(NodeColor.NOTFOUND);
                    v.goDown();
                    break;
                }
                w = w.way(v.keys[0]);
                v.goTo(w);
                pause();
            }
        }
    }
}
