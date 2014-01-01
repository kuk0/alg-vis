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
package algvis.ds.priorityqueues.daryheap;

import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import algvis.core.AlgorithmAdapter;
import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.ds.priorityqueues.PriorityQueue;
import algvis.internationalization.Languages;
import algvis.ui.VisPanel;
import algvis.ui.view.ClickListener;
import algvis.ui.view.View;

public class DaryHeap extends PriorityQueue implements ClickListener {
    public static final String dsName = "daryheap";
    DaryHeapNode root = null;
    DaryHeapNode last = null;
    int order = 5;
    public static final int minsepx = 30; // zmenit na mensie

    public DaryHeap(VisPanel M) {
        super(M);
        M.screen.V.setDS(this);
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (root == null) {
            return;
        }
        final DaryHeapNode v = root.find(x, y);
        if (v != null) {
            if (v.marked) {
                v.unmark();
                chosen = null;
            } else {
                if (chosen != null) {
                    chosen.unmark();
                }
                v.mark();
                chosen = v;
            }
        }
    }

    @Override
    public void insert(int x) {
        start(new DaryHeapInsert(this, x));

    }

    @Override
    public void delete() {
        start(new DaryHeapDelete(this));

    }

    @Override
    public void decreaseKey(Node v, int delta) {
        if (v == null) {
            // TODO: vypindat
        } else {
            start(new DaryHeapDecrKey(this, (DaryHeapNode) v, delta));
        }
    }

    @Override
    public String getName() {
        return dsName;
    }

    @Override
    public String stats() {

        if (root == null) {
            return Languages.getString("size") + ": 0 ("
                + Languages.getString("emptyheap") + ")";
        } else if (root.nnodes == 1000) {
            return Languages.getString("size") + ": 1000 ("
                + Languages.getString("fullheap") + ")";
        } else {
            return Languages.getString("size") + ": " + root.nnodes;
        }
    }

    @Override
    public void clear() {
        if (root != null) {
            root = null;
            setStats();
            reposition();
        }
    }

    public void setOrder(final Integer order) {
        if (root != null || this.order != order) {
            start(new AlgorithmAdapter(panel) {
                @Override
                public void runAlgorithm() throws InterruptedException {
                    DaryHeap.this.order = order;
                    clear();
                }
            });
        }
    }

    @Override
    public void draw(View V) {
        if (root != null) {
            root.drawTree(V);
        }
    }

    @Override
    protected void move() {
        if (root != null) {
            root.moveTree();
        }
    }

    @Override
    protected Rectangle2D getBoundingBox() {
        return root == null ? null : root.getBoundingBox();
    }

    @Override
    protected void endAnimation() {
        root.endAnimation();
    }

    @Override
    protected boolean isAnimationDone() {
        return root.isAnimationDone();
    }

    public void reposition() {
        if (root != null) {
            root._reposition();
            panel.screen.V.setBounds(x1, y1, x2, y2);
        }
    }

    public int getOrder() {
        return this.order;
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        HashtableStoreSupport.store(state, hash + "root", root);
        if (root != null) {
            root.storeState(state);
        }
        HashtableStoreSupport.store(state, hash + "last", last);
        HashtableStoreSupport.store(state, hash + "order", order);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object root = state.get(hash + "root");
        if (root != null) {
            this.root = (DaryHeapNode) HashtableStoreSupport.restore(root);
        }
        if (this.root != null) {
            this.root.restoreState(state);
        }
        final Object last = state.get(hash + "last");
        if (last != null) {
            this.last = (DaryHeapNode) HashtableStoreSupport.restore(last);
        }
        final Object order = state.get(hash + "order");
        if (order != null) {
            this.order = (Integer) HashtableStoreSupport.restore(order);
            final DaryHeapButtons buttons = (DaryHeapButtons) panel.buttons;
            buttons.OS.removeChangeListener(buttons);
            buttons.OS.setValue(this.order);
            buttons.OS.addChangeListener(buttons);
        }
    }
}
