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

import algvis.core.Node;

public class HeapDelete extends HeapAlg {
    public HeapDelete(Heap H) {
        super(H);
    }

    @Override
    public void runAlgorithm() throws InterruptedException {
        setHeader(H.minHeap ? "delete-min" : "delete-max");
        if (H.getN() == 0) {
            addStep("heapempty");
            addNote("done");
            return;
        }
        if (H.getN() == 1) {
            addStep("heap-last");
            addNote("done");
            final HeapNode v = H.getRoot();
            addToScene(v);
            H.setRoot(null);
            H.setN(H.getN() - 1);
            v.goDown();
            removeFromScene(v);
            return;
        }
        HeapNode v, w;

        final int n = H.getN();
        int k = 1 << 10;
        while ((k & n) == 0) {
            k >>= 1;
        }
        k >>= 1;
        w = H.getRoot();
        while (k > 0) {
            w = ((n & k) == 0) ? w.getLeft() : w.getRight();
            k >>= 1;
        }
        v = w;
        addToScene(v);
        H.setN(H.getN() - 1);
        if ((n & 1) == 0) {
            w.getParent().setLeft(null);
        } else {
            w.getParent().setRight(null);
        }
        v.goToRoot();
        H.reposition();
        pause();

        // TODO Takto asi nie (a mozno hej)
        H.getRoot().setKey(v.getKey());
        removeFromScene(v);
        if (H.minHeap) {
            addStep("minheapbubbledown");
        } else {
            addStep("maxheapbubbledown");
        }
        // pause();

        v = H.getRoot();
        while (true) {
            w = null;
            if (v.getLeft() != null) {
                w = v.getLeft();
            }
            if (v.getRight() != null && v.getRight().prec(w)) {
                w = v.getRight();
            }
            if (w == null || v.prec(w)) {
                break;
            }
            final HeapNode v1 = new HeapNode(v);
            final HeapNode v2 = new HeapNode(w);
            addToScene(v1);
            addToScene(v2);
            v.setKey(Node.NOKEY);
            w.setKey(Node.NOKEY);
            v1.goTo(w);
            v2.goTo(v);
            pause();
            v.setKey(v2.getKey());
            w.setKey(v1.getKey());
            v.setColor(v2.getColor());
            w.setColor(v1.getColor());
            removeFromScene(v1);
            removeFromScene(v2);
            v = w;
        }

        addNote("done");
    }
}
