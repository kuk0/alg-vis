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
package algvis.ds.dictionaries.scapegoattree;

import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;
import algvis.ui.view.REL;

public class GBInsert extends GBAlg {
    public GBInsert(GBTree T, int x) {
        super(T, x);
    }

    @Override
    public void runAlgorithm() {
        setHeader("insert", K);
        v = new GBNode(T, K, ZDepth.ACTIONNODE);
        v.setColor(NodeColor.INSERT);
        addToScene(v);
        if (T.getRoot() == null) {
            T.setRoot(v);
            v.goToRoot();
            addStep(v, REL.BOTTOM, "newroot");
            pause();
            v.setColor(NodeColor.NORMAL);
            removeFromScene(v);
        } else {
            GBNode w = (GBNode) T.getRoot();
            v.goAboveRoot();
            addStep(v, REL.TOP, "bst-insert-start");
            pause();

            while (true) {
                if (w.getKey() == K) {
                    if (w.isDeleted()) {
                        addStep(w, REL.BOTTOM, "gbinsertunmark");
                        w.setDeleted(false);
                        w.setColor(NodeColor.NORMAL);
                        T.setDel(T.getDel() - 1);
                    } else {
                        addStep(w, REL.BOTTOM, "alreadythere");
                        v.goDown();
                        v.setColor(NodeColor.NOTFOUND);
                    }
                    removeFromScene(v);
                    return;
                } else if (w.getKey() < K) {
                    if (w.getRight() == null) {
                        v.pointInDir(45);
                    } else {
                        v.pointAbove(w.getRight());
                    }
                    addStep(v, REL.LEFT, "bst-insert-right", "" + K,
                        w.getKeyS());
                    pause();
                    v.noArrow();
                    if (w.getRight() != null) {
                        w = w.getRight();
                    } else {
                        w.linkRight(v);
                        break;
                    }
                } else {
                    if (w.getLeft() == null) {
                        v.pointInDir(135);
                    } else {
                        v.pointAbove(w.getLeft());
                    }
                    addStep(v, REL.RIGHT, "bst-insert-left", "" + K,
                        w.getKeyS());
                    pause();
                    v.noArrow();
                    if (w.getLeft() != null) {
                        w = w.getLeft();
                    } else {
                        w.linkLeft(v);
                        break;
                    }
                }
                v.goAbove(w);
                pause();
            }
            v.setColor(NodeColor.NORMAL);
            removeFromScene(v);
            T.reposition();
            pause();

            GBNode b = null;
            while (w != null) {
                w.calc();
                if (w.height > Math.ceil(T.alpha * T.lg(w.size)) && b == null) {
                    b = w;
                }
                w = w.getParent();
            }

            // rebuilding
            if (b != null) {
                GBNode r = b;
                int s = 0;
                addStep(b, REL.TOP, "gbtoohigh");
                r.mark();
                pause();
                // to vine
                addStep(b, REL.TOP, "gbrebuild1");
                while (r != null) {
                    if (r.getLeft() == null) {
                        r.unmark();
                        if (r.isDeleted()) {
                            T.setDel(T.getDel() - 1);
                            if (b == r) {
                                b = r.getRight();
                            }
                            final GBNode v = r;
                            addToScene(v);
                            if (r.getParent() == null) {
                                r = (GBNode) T.setRoot(r.getRight());
                                if (r != null) {
                                    r.setParent(null);
                                }
                            } else {
                                r.getParent().linkRight(r = r.getRight());
                            }
                            v.goDown();
                            removeFromScene(v);
                        } else {
                            r = r.getRight();
                            ++s;
                        }
                        if (r != null) {
                            r.mark();
                        }
                    } else {
                        if (b == r) {
                            b = r.getLeft();
                        }
                        r.unmark();
                        r = r.getLeft();
                        r.mark();
                        T.rotate(r);
                    }
                    T.reposition();
                    pause();
                }

                // to tree
                addStep(b, REL.TOP, "gbrebuild2");
                int c = 1;
                for (int i = 0, l = (int) Math.floor(T.lg(s + 1)); i < l; ++i) {
                    c *= 2;
                }
                c = s + 1 - c;

                b = compr(b, c);
                s -= c;
                while (s > 1) {
                    b = compr(b, s /= 2);
                }
            }
        }
        T.reposition();
        addNote("done");
    }
}
