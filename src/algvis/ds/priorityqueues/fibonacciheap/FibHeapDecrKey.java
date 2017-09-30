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
package algvis.ds.priorityqueues.fibonacciheap;

import algvis.core.Algorithm;
import algvis.ds.priorityqueues.binomialheap.BinHeapNode;
import algvis.ds.priorityqueues.binomialheap.BinomialHeap;

public class FibHeapDecrKey extends Algorithm {
    private final int delta;
    private final BinomialHeap H;
    private BinHeapNode v;

    public FibHeapDecrKey(BinomialHeap H, BinHeapNode v, int delta) {
        super(H.panel);
        this.H = H;
        this.v = v;
        this.delta = delta;
    }

    @Override
    public void runAlgorithm() {
        setHeader(H.minHeap ? "decreasekey" : "increasekey");
        final int i = H.active;
        v.decrKey(delta, H.minHeap);
        BinHeapNode w = v.parent;
        // if (w == null) return;
        while (w != null) {
            v.unlink();
            v.unmarkCut();
            H.root[i].linkLeft(v);
            if (v.prec(H.min[i])) {
                H.min[i] = v;
            }
            H.reposition();
            pause();
            if (w.cut) {
                v = w;
                w = v.parent;
            } else {
                w.markCut();
                H.reposition();
                break;
            }
        }
    }
}
