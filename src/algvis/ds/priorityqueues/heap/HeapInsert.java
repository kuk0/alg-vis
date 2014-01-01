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
package algvis.ds.priorityqueues.heap;

import algvis.core.visual.ZDepth;

public class HeapInsert extends HeapAlg {
    private final int K;

    public HeapInsert(Heap H, int x) {
        super(H);
        K = x;
    }

    @Override
    public void runAlgorithm() throws InterruptedException {
        setHeader("insert", K);
        final HeapNode v = new HeapNode(H, K, ZDepth.ACTIONNODE);
        addToScene(v);
        if (H.getN() == 1000) {
            addStep("heapfull");
            removeFromScene(v);
            return;
        }
        HeapNode w;

        // link
        H.setN(H.getN() + 1);
        final int n = H.getN();
        int k = 1 << 10;
        if (n == 1) {
            H.setRoot(w = v);
            v.goToRoot();
            pause();
        } else {
            while ((k & n) == 0) {
                k >>= 1;
            }
            k >>= 1;
            w = H.getRoot();
            while (k > 1) {
                w = ((n & k) == 0) ? w.getLeft() : w.getRight();
                k >>= 1;
            }
            if ((k & n) == 0) {
                w.linkLeft(v);
            } else {
                w.linkRight(v);
            }
            v.mark();
            H.reposition();
            pause();
        }
        removeFromScene(v);
        v.unmark();

        // pause();
        bubbleup(v);
    }
}
