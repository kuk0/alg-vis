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

import algvis.core.Algorithm;
import algvis.core.visual.ZDepth;
import algvis.ds.priorityqueues.binomialheap.BinHeapNode;

public class LazyBinHeapInsert extends Algorithm {
    private final LazyBinomialHeap H;
    private final int x;

    public LazyBinHeapInsert(LazyBinomialHeap H, int x) {
        super(H.panel);
        this.H = H;
        this.x = x;
    }

    @Override
    public void runAlgorithm() throws InterruptedException {
        setHeader("insert", x);
        final int i = H.active;
        final BinHeapNode v = new BinHeapNode(H, x, ZDepth.ACTIONNODE);
        if (H.root[i] == null) {
            H.root[i] = H.min[i] = v;
        } else {
            H.root[i].linkLeft(v);
            if (v.prec(H.min[i])) {
                H.min[i] = v;
            }
        }
        H.reposition();
    }
}
