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
package algvis.ds.priorityqueues.binomialheap;

import algvis.ui.view.REL;

public class BinHeapDelete extends BinHeapAlg {

    public BinHeapDelete(BinomialHeap H) {
        super(H);
    }

    
    // TODO: fix deleting the whole Heap
    // TODO: simplify(?)
    
    @Override
    public void runAlgorithm() {
        setHeader(H.minHeap ? "delete-min" : "delete-max");
        final int i = H.active;
        if (H.root[i] == null) {
            // empty - done;
            addStep(H.getBoundingBoxDef(), 200, REL.TOP, "heapempty");
            addNote("done");
            pause();
            return;
        }
        BinHeapNode v, w;
        final BinHeapNode d = H.min[i];
        if (H.root[i] == d) {
            H.root[i] = d.right;
        }
        if (d.right == d) { // single bin tree
            H.min[i] = H.root[i] = null;
        }
        d.unlink();
        addToScene(d);
        d.goDown();
        removeFromScene(d);

        // find new min among the remaining bin trees
        if (H.root[i] != null) {
            w = H.min[i] = H.root[i];
            do {
                if (w.prec(H.min[i])) {
                    H.min[i] = w;
                }
                w = w.right;
            } while (w != H.root[i]);
            addStep(H.min[i], REL.TOP, H.minHeap ? "binheap-findmin" : "binheap-findmax");
        }
        H.root[0] = v = w = d.child;
        d.child = null;
        pause();

        H.reposition();
        if (w == null) {
            // no children - done;
            addStep(H.getBoundingBoxDef(), 200, REL.TOP, "binheap-nochildren");
            addNote("done");
            pause();
            return;
        }
        
        // reverse & find min
        addNote("binheap-meldchildren");
        H.min[0] = w;
        do {
            w.parent = null;
            if (w.prec(H.min[0])) {
                H.min[0] = w;
            }
            final BinHeapNode tl = w.left, tr = w.right;
            w.left = tr;
            w.right = tl;
            w = tr;
        } while (w != v);
        H.root[0] = w = v = w.right;
        H.reposition();
        pause();

        if (H.root[i] == null) {
            // heap #1 is empty; done;
            H.root[i] = H.root[0];
            H.min[i] = H.min[0];
            H.root[0] = null;
            H.min[0] = null;
            H.reposition();
            addStep(H.root[i], REL.TOP, "binheap-top-empty");
            addNote("done");
            pause();
            return;
        }

        meld(i);
    }
}
