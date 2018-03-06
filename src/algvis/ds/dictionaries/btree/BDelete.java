/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.ds.dictionaries.btree;

import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.ui.view.REL;

public class BDelete extends BAlg {

    public BDelete(BTree T, int x) {
        super(T, x);
    }

    @Override
    public void runAlgorithm() {
        BNode v = new BNode(T, K);
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
            BNode d = T.getRoot();
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

            d.setColor(NodeColor.FOUND);
            pause();
            d.setColor(NodeColor.NORMAL);
            removeFromScene(v);
            if (d.isLeaf()) {
                addStep(d, REL.BOTTOM, "bdelete1");
                if (d.isRoot() && d.numKeys == 1) {
                    addToScene(v = d);
                    T.setRoot(null);
                    v.goDown();
                } else {
                    addToScene(v = d.del(K));
                    T.reposition();
                    v.goDown();
                    pause();
                }
                removeFromScene(v);
            } else {
                addStep(d, REL.BOTTOM, "bdelete2");
                BNode s = d.way(K + 1);
                addToScene(v = new BNode(T, -Node.INF, d.tox, d.toy));
                v.setColor(NodeColor.FIND);
                v.goAbove(s);
                pause();
                while (!s.isLeaf()) {
                    s = s.c[0];
                    v.goAbove(s);
                    pause();
                }
                removeFromScene(v);
                addToScene(v = s.delMin());
                v.goTo(d);
                pause();
                d.replace(K, v.keys[0]);
                removeFromScene(v);
                pause();
                d.setColor(NodeColor.NORMAL);
                d = s;
            }

            while (!d.isRoot() && d.numKeys < (T.order - 1) / 2) {
                d.setColor(NodeColor.NOTFOUND);
                BNode s, s1 = null, s2 = null;
                final BNode p = d.parent;
                boolean lefts = true;
                int k = d.order(), n1 = 0, n2 = 0;
                if (k > 0) {
                    s1 = p.c[k - 1];
                    n1 = s1.numKeys;
                }
                if (k + 1 < p.numChildren) {
                    s2 = p.c[k + 1];
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
                    removeFromScene(v);
                    v = new BNode(T, pkey, p.tox, p.toy);
                    addToScene(v);
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
                    break;
                } else {
                    // treba spojit vrchol d + p.keys[k] + s
                    // zmenit p.c[k] na novy vrchol a posunut to
                    addStep(d, REL.BOTTOM, "bmerge");
                    if (p.isRoot() && p.numKeys == 1) {
                        v = new BNode(T.getRoot());
                        addToScene(v);
                        T.getRoot().keys[0] = Node.NOKEY;
                        v.goTo((d.tox + s.tox) / 2, d.y);
                        pause();
                        if (lefts) {
                            T.setRoot(new BNode(s, v, d));
                        } else {
                            T.setRoot(new BNode(d, v, s));
                        }
                        removeFromScene(v);
                        break;
                    } else {
                        addToScene(v = p.del(p.keys[k]));
                        v.goTo((d.tox + s.tox) / 2, d.y);
                        pause();
                        if (lefts) {
                            p.c[k] = new BNode(s, v, d);
                        } else {
                            p.c[k] = new BNode(d, v, s);
                        }
                        removeFromScene(v);
                        p.c[k].parent = p;
                        --p.numChildren;
                        pause();
                        System.arraycopy(p.c, k + 1 + 1, p.c, k + 1,
                            p.numChildren - (k + 1));
                        d = p;
                    }
                }
            }
            T.reposition();
        }
        removeFromScene(v);
        addNote("done");
    }
}
