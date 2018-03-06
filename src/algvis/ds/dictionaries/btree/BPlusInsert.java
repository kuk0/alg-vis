package algvis.ds.dictionaries.btree;

import algvis.core.NodeColor;
import algvis.ui.view.REL;

public class BPlusInsert extends BPlusAlg {

    public BPlusInsert(BPlusTree T, int x) {
        super(T, x);
    }

    @Override
    public void runAlgorithm() {
        setHeader("insert", K);
        final BPlusNode v = new BPlusNode(T, K);
        v.setColor(NodeColor.INSERT);
        addToScene(v);
        if (T.getRoot() == null) {
            T.setRoot(v);
            v.goAboveRoot();
            addStep(v, REL.TOP, "newroot");
            pause();
            v.setColor(NodeColor.NORMAL);
            removeFromScene(v);
        } else {
            BPlusNode w = T.getRoot();
            v.goAbove(w);
            addStep(v, REL.TOP, "bst-insert-start");
            pause();

            while (true) {
                if (w.isIn(K)) {
                    addStep(w, REL.BOTTOM, "alreadythere");
                    v.goDown();
                    removeFromScene(v);
                    return;
                }
                if (w.isLeaf()) {
                    break;
                }
                w = goToChild(w, v);
            }

            addStep(w, REL.BOTTOM, "binsertleaf");
            w.addLeaf(K);
            if (w.numKeys >= T.order) {
                w.setColor(NodeColor.NOTFOUND);
            }
            removeFromScene(v);
            pause();

            while (w.numKeys >= T.order) {
                addStep(w, REL.BOTTOM, "bsplit");
                final int o = (w.parent != null) ? w.order() : -1;
                w = w.split();
                if (w.parent == null) {
                    break;
                }
                w.parent.c[o] = w;
                pause();
                w.goBelow(w.parent);
                pause();
                w.parent.add(o, w);
                w = (BPlusNode) w.parent;
                if (w.numKeys >= T.order) {
                    w.setColor(NodeColor.NOTFOUND);
                }
                T.reposition();
                pause();
            }
            if (w.isRoot()) {
                T.setRoot(w);
            }
            T.reposition();
        }
    }

}
