package algvis.ds.dictionaries.btree;

import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.ui.view.REL;

public class BPlusDelete extends BPlusAlg {

    public BPlusDelete(BPlusTree T, int x) {
        super(T, x);
    }

    @Override
    public void runAlgorithm() {
        BPlusNode v = new BPlusNode(T, K);
        v.setColor(NodeColor.DELETE);
        addToScene(v);
        setHeader("delete", K);
        if (T.getRoot() == null) {
            v.goToRoot();
            addStep(T.getBoundingBoxDef(), 200, REL.TOP, "empty");
            pause();
            v.goDown();
            v.setColor(NodeColor.NOTFOUND);
            addStep(T.getBoundingBoxDef(), 200, REL.TOP, "notfound");
        } else {
            BPlusNode d = T.getRoot();
            v.goAbove(d);
            addStep(v, REL.TOP, "bstdeletestart");
            pause();

            while (true) {
                if (d.isIn(K)) {
                    break;
                }
                if (d.isLeaf()) {
                    addStep(d, REL.BOTTOM, "notfound");
                    v.goDown();
                    removeFromScene(v);
                    return;
                }
                d = goToChild(d, v);
            }

            //now the key is found in some node..
            // if it's an index node, we have to find the keys in a leaf
            d.setColor(NodeColor.FOUND);
            boolean isInLeaf = d.isLeaf();
            final BPlusNode b = d;
            while (!d.isLeaf()) {
                d = goToChild(d, v);
            }

            d.setColor(NodeColor.FOUND);
            pause();
            d.setColor(NodeColor.NORMAL);
            addStep(d, REL.BOTTOM, "bdelete1");
            if (d.isRoot() && d.numKeys == 1) {
                v = d;
                T.setRoot(null);
                addToScene(v);
                v.goDown();
            } else {
                v = d.del(K);
                addToScene(v);
                T.reposition();
                v.goDown();
                pause();

            }
            while (!d.isRoot() && d.numKeys < (T.order - 1) / 2) {
                d.setColor(NodeColor.NOTFOUND);
                BPlusNode s, s1 = null, s2 = null;
                final BPlusNode p = (BPlusNode) d.parent;
                boolean lefts = true;
                int k = d.order(), n1 = 0, n2 = 0;
                if (k > 0) {
                    s1 = (BPlusNode) p.c[k - 1];
                    n1 = s1.numKeys;
                }
                if (k + 1 < p.numChildren) {
                    s2 = (BPlusNode) p.c[k + 1];
                    n2 = s2.numKeys;
                }
                if (n1 >= n2) {
                    s = s1;
                    --k;
                } else {
                    s = s2;
                    lefts = false;
                }

                if (s.numKeys > (T.order - 1) / 2) {
                    // treba zobrat prvok z s, nahradit nim p.keys[k]
                    // a p.keys[k] pridat do d
                    // tiez treba prehodit pointer z s ku d
                    addStep(d, REL.BOTTOM, lefts ? "bleft" : "bright");
                    addToScene(v = lefts ? s.delMax() : s.delMin());
                    v.goTo(p);
                    pause();
                    if (d.isLeaf()) {
                        int pkey;
                        if (lefts) {
                            pkey = v.keys[0];
                        } else {
                            pkey = s.keys[0];
                        }
                        p.keys[k] = pkey;
                        removeFromScene(v);
                        addToScene(v = new BPlusNode(T, v.keys[0], p.x, p.y));
                        v.goTo(d);
                        pause();

                        if (lefts) {
                            d.insMin(v.keys[0]);
                        } else {
                            d.insMax(v.keys[0]);
                        }
                        d.setColor(NodeColor.NORMAL);
                    } else {
                        final int pkey = p.keys[k];
                        p.keys[k] = v.keys[0];
                        addToScene(v = new BPlusNode(T, pkey, p.x, p.y));
                        v.goTo(d);
                        pause();
                        if (lefts) {
                            d.insMin(pkey);
                            d.insMinCh(s.delMaxCh());
                            d.c[0].parent = d;
                        } else {
                            d.insMax(pkey);
                            d.insMaxCh(s.delMinCh());
                            d.c[d.numChildren - 1].parent = d;
                        }
                        d.setColor(NodeColor.NORMAL);
                    }
                    removeFromScene(v);
                    break;
                } else {
                    // treba spojit vrchol d + p.keys[k] + s
                    // zmenit p.c[k] na novy vrchol a posunut to
                    addStep(d, REL.BOTTOM, "bmerge");
                    if (p.isRoot() && p.numKeys == 1) {
                        addToScene(v = new BPlusNode(T.getRoot()));
                        T.getRoot().keys[0] = Node.NOKEY;
                        v.goTo((d.tox + s.tox) / 2, d.y);
                        pause();
                        if (lefts) {
                            T.setRoot(new BPlusNode(s, v, d));
                        } else {
                            T.setRoot(new BPlusNode(d, v, s));
                        }
                        break;
                    } else {
                        if (d.isLeaf()) {
                            v = p.del(p.keys[k]);
                            addToScene(v);
                            v.goDown();
                            pause();
                            if (lefts) {
                                p.c[k] = new BPlusNode(s, d);
                            } else {
                                p.c[k] = new BPlusNode(d, s);
                            }
                            p.c[k].parent = p;
                            --p.numChildren;
                            for (int i = k + 1; i < p.numChildren; ++i) {
                                p.c[i] = p.c[i + 1];
                            }
                            d = p;
                        } else {
                            addToScene(v = p.del(p.keys[k]));
                            v.goTo((d.tox + s.tox) / 2, d.y);
                            pause();
                            if (lefts) {
                                p.c[k] = new BPlusNode(s, v, d);
                            } else {
                                p.c[k] = new BPlusNode(d, v, s);
                            }
                            removeFromScene(v);
                            p.c[k].parent = p;
                            --p.numChildren;
                            pause();
                            for (int i = k + 1; i < p.numChildren; ++i) {
                                p.c[i] = p.c[i + 1];
                            }
                            d = p;
                        }
                    }
                }
            }
            removeFromScene(v);
            T.reposition();
            
            // now handle the case when the key is also in the index node (b)
            d = b;
            if (!d.isIn(K)) {
                isInLeaf = true; //tzn, ze uz je to cislo prepisane
                d.setColor(NodeColor.NORMAL);
            }
            if (!isInLeaf) {
                pause();
                addStep(d, REL.BOTTOM, "bdelete2");
                BPlusNode s = (BPlusNode) d.way(K + 1);
                addToScene(v = new BPlusNode(T, -Node.INF, d.x, d.y));
                v.goAbove(s);
                pause();
                while (!s.isLeaf()) {
                    s = (BPlusNode) s.c[0];
                    v.goAbove(s);
                    pause();
                }
                v = new BPlusNode(s.D, s.keys[0],
                    s.x - (s.numKeys - 1) * Node.RADIUS, s.y);
                addToScene(v);
                v.goTo(d);
                pause();
                d.replace(K, v.keys[0]);
                removeFromScene(v);
                pause();
                d.setColor(NodeColor.NORMAL);
                d = s;

                while (!d.isRoot() && d.numKeys < (T.order - 1) / 2) {
                    d.setColor(NodeColor.NOTFOUND);
                    BPlusNode s1 = null, s2 = null;
                    final BPlusNode p = (BPlusNode) d.parent;
                    boolean lefts = true;
                    int k = d.order(), n1 = 0, n2 = 0;
                    if (k > 0) {
                        s1 = (BPlusNode) p.c[k - 1];
                        n1 = s1.numKeys;
                    }
                    if (k + 1 < p.numChildren) {
                        s2 = (BPlusNode) p.c[k + 1];
                        n2 = s2.numKeys;
                    }
                    if (n1 >= n2) {
                        s = s1;
                        --k;
                    } else {
                        s = s2;
                        lefts = false;
                    }

                    if (s.numKeys > (T.order - 1) / 2) {
                        // treba zobrat prvok z s, nahradit nim p.keys[k]
                        // a p.keys[k] pridat do d
                        // tiez treba prehodit pointer z s ku d
                        addStep(d, REL.BOTTOM, lefts ? "bleft" : "bright");
                        addToScene(v = lefts ? s.delMax() : s.delMin());
                        v.goTo(p);
                        pause();
                        final int pkey = p.keys[k];
                        p.keys[k] = v.keys[0];
                        addToScene(v = new BPlusNode(T, pkey, p.x, p.y));
                        v.goTo(d);
                        pause();
                        if (lefts) {
                            d.insMin(pkey);
                            if (!d.isLeaf()) {
                                d.insMinCh(s.delMaxCh());
                                d.c[0].parent = d;
                            }
                        } else {
                            d.insMax(pkey);
                            if (!d.isLeaf()) {
                                d.insMaxCh(s.delMinCh());
                                d.c[d.numChildren - 1].parent = d;
                            }
                        }
                        d.setColor(NodeColor.NORMAL);
                        removeFromScene(v);
                        break;
                    } else {
                        // treba spojit vrchol d + p.keys[k] + s
                        // zmenit p.c[k] na novy vrchol a posunut to
                        addStep(d, REL.BOTTOM, "bmerge");
                        if (p.isRoot() && p.numKeys == 1) {
                            addToScene(v = new BPlusNode(T.getRoot()));
                            T.getRoot().keys[0] = Node.NOKEY;
                            v.goTo((d.tox + s.tox) / 2, d.y);
                            pause();
                            if (lefts) {
                                T.setRoot(new BPlusNode(s, v, d));
                            } else {
                                T.setRoot(new BPlusNode(d, v, s));
                            }
                            break;
                        } else {
                            addToScene(v = p.del(p.keys[k]));
                            v.goTo((d.tox + s.tox) / 2, d.y);
                            pause();
                            if (lefts) {
                                p.c[k] = new BPlusNode(s, v, d);
                            } else {
                                p.c[k] = new BPlusNode(d, v, s);
                            }
                            removeFromScene(v);
                            p.c[k].parent = p;
                            --p.numChildren;
                            for (int i = k + 1; i < p.numChildren; ++i) {
                                p.c[i] = p.c[i + 1];
                            }
                            d = p;
                        }
                    }
                }
            }

            removeFromScene(v);
            T.reposition();
        }
    }
}
