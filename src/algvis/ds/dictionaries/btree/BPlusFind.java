package algvis.ds.dictionaries.btree;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.ui.view.REL;

public class BPlusFind extends Algorithm {
    BPlusTree T;
    BNode v;

    public BPlusFind(BPlusTree T, int x) {
        super(T.panel);
        this.T = T;
        addToScene(v = new BPlusNode(T, x));
        v.setColor(NodeColor.FIND);
        setHeader("search");
    }

    @Override
    public void runAlgorithm() {
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
