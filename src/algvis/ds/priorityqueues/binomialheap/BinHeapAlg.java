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

import algvis.core.Algorithm;
import algvis.ui.view.REL;

abstract class BinHeapAlg extends Algorithm {
    final BinomialHeap H;

    BinHeapAlg(BinomialHeap H) {
        super(H.panel, null);
        this.H = H;
    }

    void meld(int i) {
        BinHeapNode v = H.root[i];
        v.mark();
        if ((H.min[0]).prec(H.min[i])) {
            H.min[i] = H.min[0];
            addStep(H.min[0], REL.BOTTOM, H.minHeap ? "binheap-newmin" : "binheap-newmax",
                H.min[i].getKeyS());
        } else {
            addStep(H.min[i], REL.TOP, H.minHeap ? "binheap-oldmin" : "binheap-oldmax",
                H.min[i].getKeyS());
        }
        addNote("binheap-meld-idea");
        H.min[0] = null;
        pause();
        while (true) {
            if (H.root[0] != null && v.rank > H.root[0].rank) {
                addStep(H.root[0], REL.TOP, "binheap-add-tree");
                final BinHeapNode u = H.root[0];
                if (H.root[0].right == H.root[0]) {
                    removeFromScene(H.root[0]);
                    H.root[0] = null;
                } else {
                    H.root[0] = H.root[0].right;
                }
                u.unlink();
                u.highlightTree(u);
                v.linkLeft(u);
                v.unmark();
                v = H.root[i] = u;
                v.mark();
            } else if (H.root[0] != null && v.rank <= H.root[0].rank
                && (v.right == H.root[i] || H.root[0].rank < v.right.rank)) {
                // pripojime vpravo
                final BinHeapNode u = H.root[0];
                addStep(u, REL.TOP, "binheap-add-tree");
                pause();
                if (H.root[0].right == H.root[0]) {
                    removeFromScene(H.root[0]);
                    H.root[0] = null;
                } else {
                    H.root[0] = H.root[0].right;
                }
                u.unlink();
                u.highlightTree(u);
                v.linkRight(u);
            } else if (v.left.rank == v.rank && v.left != v
                && (v.right == H.root[i] || v.rank < v.right.rank)) {
                final BinHeapNode u = v.left;
                if (u.prec(v)) { // napojime v pod u
                    addStep(v, REL.TOP, "binheap-link", v.getKeyS(), u.getKeyS());
                    pause();
                    v.unlink();
                    u.linkChild(v);
                    v.unmark();
                    v = u;
                    v.mark();
                } else { // napojime u pod v
                    addStep(u, REL.TOP, "binheap-link", u.getKeyS(), v.getKeyS());
                    pause();
                    if (H.root[i] == u) {
                        H.root[i] = v;
                    }
                    u.unlink();
                    v.linkChild(u);
                }
            } else if (v.right != H.root[i]) {
                // posunieme sa
                addStep(v.right, REL.TOP, "binheap-next");
                v.unmark();
                v = v.right;
                v.mark();
            } else {
                // koncime
                v.unmark();
                break;
            }
            H.reposition();
            pause();
        }
        addNote("done");
    }
}
