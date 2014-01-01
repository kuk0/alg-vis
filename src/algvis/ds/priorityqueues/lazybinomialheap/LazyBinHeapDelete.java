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
package algvis.ds.priorityqueues.lazybinomialheap;

import algvis.core.Algorithm;
import algvis.ds.priorityqueues.binomialheap.BinHeapNode;

public class LazyBinHeapDelete extends Algorithm {
    private final LazyBinomialHeap H;

    int lg(int n) {
        return (int) Math.ceil(Math.log(n) / Math.log(2));
    }

    public LazyBinHeapDelete(LazyBinomialHeap H) {
        super(H.panel);
        this.H = H;
    }

    @Override
    public void runAlgorithm() throws InterruptedException {
        setHeader(H.minHeap ? "delete-min" : "delete-max");
        final int i = H.active;
        H.cleanup = new BinHeapNode[lg(H.size(i) + 1) + 1]; // TODO: change to
                                                            // rank
        if (H.root[i] == null) {
            // empty
            H.cleanup = null;
            return;
        }
        pause();
        // delete min
        BinHeapNode v, w, next;
        final BinHeapNode d = H.min[i];
        H.min[i] = null;
        if (H.root[i] == d) {
            if (d.right == d) {
                H.root[i] = null;
            } else {
                H.root[i] = d.right;
            }
        }
        addToScene(d);
        d.unlink();
        d.goDown();
        removeFromScene(d);

        v = w = d.child;
        d.child = null;
        if (w != null) {
            // reverse
            do {
                w.parent = null;
                final BinHeapNode tl = w.left, tr = w.right;
                w.left = tr;
                w.right = tl;
                w = tr;
            } while (w != v);
            w = w.right;
            // link
            if (H.root[i] == null) {
                H.root[i] = w;
            } else {
                H.root[i].linkAll(w);
            }
        }
        H.reposition();
        pause();

        // cleanup
        if (H.root[i] != null) {
            int h;
            w = H.root[i];
            do {
                w.mark();
                pause();
                h = w.rank;
                v = H.cleanup[h];
                next = w.right;
                if (next == H.root[i]) {
                    next = null;
                }
                w.unmark();
                while (v != null) {
                    if (H.root[i] == w) {
                        H.root[i] = w.right;
                    }
                    w.unlink();
                    if (v.prec(w)) {
                        v.linkChild(w);
                        w = v;
                    } else {
                        v.linkRight(w);
                        if (H.root[i] == v) {
                            H.root[i] = w;
                        }
                        // pause();
                        v.unlink();
                        w.linkChild(v);
                        v = w;
                    }
                    // pause();
                    H.cleanup[h] = null;
                    ++h;
                    v = H.cleanup[h];
                    H.reposition();
                    pause();
                }
                H.cleanup[h] = w;
                w = next;
            } while (w != null);
        }
        H.cleanup = null;

        // find new min
        v = w = H.min[i] = H.root[i];
        if (H.root[i] != null) {
            do {
                if (w.prec(H.min[i])) {
                    H.min[i] = w;
                }
                w = w.right;
            } while (w != v);
        }
    }
}
