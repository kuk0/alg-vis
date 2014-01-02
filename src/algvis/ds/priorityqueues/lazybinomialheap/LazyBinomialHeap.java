/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
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
package algvis.ds.priorityqueues.lazybinomialheap;

import java.util.Hashtable;

import algvis.core.Node;
import algvis.core.Pair;
import algvis.core.history.HashtableStoreSupport;
import algvis.ds.priorityqueues.MeldablePQButtons;
import algvis.ds.priorityqueues.binomialheap.BinHeapNode;
import algvis.ds.priorityqueues.binomialheap.BinomialHeap;
import algvis.ui.Fonts;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

public class LazyBinomialHeap extends BinomialHeap {
    public static String dsName = "lazybinheap";
    BinHeapNode[] cleanup;
    private static final int arrayheight = 2 * minsepy;

    @Override
    public String getName() {
        return "lazybinheap";
    }

    public LazyBinomialHeap(VisPanel M) {
        super(M);
        reposition();
    }

    @Override
    public void insert(int x) {
        start(new LazyBinHeapInsert(this, x));
    }

    @Override
    public void delete() {
        start(new LazyBinHeapDelete(this));
    }

    @Override
    public void meld(int i, int j) {
        final Pair p = chooseHeaps(i, j);
        i = p.first;
        j = p.second;
        ((MeldablePQButtons) panel.buttons).activeHeap.setValue(i);
        start(new LazyBinHeapMeld(this, i, j));
    }

    @Override
    public void draw(View V) {
        super.draw(V);
        if (cleanup != null && root[active] != null) {
            int x = root[active].x;
            final int y = -arrayheight;
            for (int i = 0; i < cleanup.length; ++i) {
                V.drawSquare(x, y, Node.RADIUS);
                V.drawStringTop("" + i, x, y - Node.RADIUS + 1, Fonts.NORMAL);
                if (cleanup[i] == null) {
                    V.drawLine(x - Node.RADIUS, y + Node.RADIUS, x
                        + Node.RADIUS, y - Node.RADIUS);
                } else {
                    V.drawArrow(x, y, cleanup[i].x, cleanup[i].y - minsepy
                        + Node.RADIUS);
                }
                x += 2 * Node.RADIUS;
            }
        }
    }

    @Override
    public void reposition() {
        super.reposition();
        panel.screen.V.miny = -arrayheight - 50;
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        if (cleanup != null) {
            HashtableStoreSupport.store(state, hash + "cleanup",
                cleanup.clone());
            // TODO mozno netreba ukladat (ak su vrcholy niekde inde ulozene)
            for (int i = 0; i < cleanup.length; ++i) {
                if (cleanup[i] != null) {
                    cleanup[i].storeTreeState(state);
                }
            }
        } else {
            HashtableStoreSupport.store(state, hash + "cleanup", null);
        }
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object cleanup = state.get(hash + "cleanup");
        if (cleanup != null) {
            this.cleanup = (BinHeapNode[]) HashtableStoreSupport
                .restore(cleanup);
        }
        if (this.cleanup != null) {
            for (int i = 0; i < this.cleanup.length; ++i) {
                if (this.cleanup[i] != null) {
                    this.cleanup[i].restoreTreeState(state);
                }
            }
        }
    }
}
