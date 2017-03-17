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
package algvis.ds.priorityqueues.leftistheap;

import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import algvis.core.Node;
import algvis.core.Pair;
import algvis.core.StringUtils;
import algvis.core.history.HashtableStoreSupport;
import algvis.ds.priorityqueues.MeldablePQ;
import algvis.ds.priorityqueues.MeldablePQButtonsNoDecr;
import algvis.internationalization.Languages;
import algvis.ui.VisPanel;
import algvis.ui.view.ClickListener;
import algvis.ui.view.View;

public class LeftHeap extends MeldablePQ implements ClickListener {
    public static String dsName = "leftheap";
    LeftHeapNode root[] = null;

    public LeftHeap(VisPanel M) {
        super(M);
        root = new LeftHeapNode[numHeaps + 1];
        M.screen.V.setDS(this);
    }

    @Override
    public void insert(int x) {
        start(new LeftHeapInsert(this, x));
    }

    @Override
    public void delete() {
        start(new LeftHeapDelete(this));

    }

    protected Pair chooseHeaps(int i, int j) {
        if (i < 1 || i > numHeaps) {
            i = -1;
        }
        if (j < 1 || j > numHeaps) {
            j = -1;
        }
        if (i == -1 || j == -1) {
            j = i;
            i = active;
            if (i == j || j == -1) {
                j = (active == 1) ? 2 : 1;
                for (int k = 0; k <= numHeaps; ++k) {
                    if (k != active && root[k] != null) {
                        j = k;
                        break;
                    }
                }
            }
        }
        return new Pair(i, j);
    }

    @Override
    public void meld(int i, int j) {
        final Pair p = chooseHeaps(i, j);
        i = p.first;
        j = p.second;
        ((MeldablePQButtonsNoDecr) panel.buttons).activeHeap.setValue(i);
        start(new LeftHeapMeld(this, i, j));
    }

    @Override
    public String stats() {

        if (root[active] == null) {
            return Languages.getString("size") + ": 0;   "
                + Languages.getString("height") + ": 0 =  1.00\u00b7"
                + Languages.getString("opt") + ";   "
                + Languages.getString("avedepth") + ": 0";
        } else {
            root[active].calcTree();
            return Languages.getString("size")
                + ": "
                + root[active].size
                + ";   "
                + Languages.getString("height")
                + ": "
                + root[active].height
                + " = "
                + StringUtils.format(
                    root[active].height
                        / (Math.floor(lg(root[active].size)) + 1), 2, 5)
                + "\u00b7"
                + Languages.getString("opt")
                + ";   "
                + Languages.getString("avedepth")
                + ": "
                + StringUtils.format(root[active].sumh
                    / (double) root[active].size, 2, -5);
        }

    }

    @Override
    public void clear() {
        for (int i = 0; i <= numHeaps; i++) {
            root[i] = null;
        }

        setStats();

    }

    @Override
    public void decreaseKey(Node v, int delta) {
        if (v == null) {
            // TODO: vypindat
        } else {
            start(new LeftHeapDecrKey(this, (LeftHeapNode) v, delta));
        }
    }

    @Override
    public void mouseClicked(int x, int y) {
        int h = 0;
        LeftHeapNode v = null;
        for (int i = 1; i <= numHeaps; ++i) {
            if (root[i] != null) {
                v = (LeftHeapNode) root[i].find(x, y);
                if (v != null) {
                    h = i;
                    break;
                }
            }
        }
        if (v != null) {
            if (v.marked) {
                v.unmark();
                chosen = null;
            } else {
                if (chosen != null) {
                    chosen.unmark();
                }
                if (h == active) {
                    v.mark();
                    chosen = v;
                } else {
                    ((MeldablePQButtonsNoDecr) panel.buttons).activeHeap
                        .setValue(h);
                    // lowlight();
                    // highlight(h);
                }
            }
        }
    }

    public void reposition() {
        final int HEAP_SEP = 20;
        int sumx = 0;
        for (int i = 1; i <= numHeaps; ++i) {
            if (root[i] != null) {
                root[i].reposition();
                root[i].reboxTree();
                sumx += root[i].leftw;
                root[i].repos(sumx, root[i].toy);
                sumx += root[i].rightw + HEAP_SEP;
            }

            if (i == active) {
                if (root[0] != null) {
                    root[0].reposition();
                    root[0].reboxTree();
                    sumx += root[0].leftw;
                    if (root[0].y >= 0) { // nie je na zaciatku vkladania
                        root[0].repos(sumx, root[0].y);
                    } else {
                        root[0].repos(sumx, root[0].toy);
                    }
                    sumx += root[0].rightw + HEAP_SEP;
                }
            }
        }
        panel.screen.V.setBounds(0, 0, sumx, y2);
    }

    @Override
    public void draw(View V) {
        for (int i = 0; i <= numHeaps; ++i) {
            if (root[i] != null) {
                root[i].drawTree(V);
            }
        }
    }

    @Override
    protected void move() {
        for (int i = 0; i <= numHeaps; ++i) {
            if (root[i] != null) {
                root[i].moveTree();
            }
        }
    }

    @Override
    protected Rectangle2D getBoundingBox() {
        Rectangle2D retVal = null;
        if (root != null) {
            for (int i = 0; i <= numHeaps; ++i) {
                if (root[i] != null) {
                    final Rectangle2D riBB = root[i].getBoundingBox();
                    if (retVal == null) {
                        retVal = riBB;
                    } else if (riBB != null) {
                        retVal = retVal.createUnion(riBB);
                    }
                }
            }
        }
        return retVal;
    }

    @Override
    protected void endAnimation() {
        if (root != null) {
            for (int i = 0; i <= numHeaps; ++i) {
                if (root[i] != null) {
                    root[i].endAnimation();
                }
            }
        }
    }

    @Override
    protected boolean isAnimationDone() {
        if (root != null) {
            for (int i = 0; i <= numHeaps; ++i) {
                if (root[i] != null && !root[i].isAnimationDone()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void highlight(int i) {
        active = i;
        if (root[active] != null) {
            root[active].highlightTree();
        }
    }

    @Override
    public void lowlight() {
        if (root[active] != null) {
            root[active].lowlightTree();
        }
    }

    @Override
    public String getName() {
        return "leftheap";
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        HashtableStoreSupport.store(state, hash + "root", root.clone());
        for (int i = 0; i <= numHeaps; ++i) {
            if (root[i] != null) {
                root[i].storeState(state);
            }
        }
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object root = state.get(hash + "root");
        if (root != null) {
            this.root = (LeftHeapNode[]) HashtableStoreSupport.restore(root);
        }
        for (int i = 0; i <= numHeaps; ++i) {
            if (this.root[i] != null) {
                this.root[i].restoreState(state);
            }
        }
    }
}