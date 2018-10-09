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

import algvis.core.Algorithm;
import algvis.ui.view.REL;

abstract class HeapAlg extends Algorithm {
    final Heap H;

    HeapAlg(Heap H) {
        super(H.panel);
        this.H = H;
    }

    HeapNode setNode(int n, HeapNode v) {
        int k = 1 << 10;
        if (n == 1) {
            H.setRoot(v);
            return null;
        } else {
            while ((k & n) == 0) {
                k >>= 1;
            }
            k >>= 1;
            HeapNode w = H.getRoot(), p;
            while (k > 1) {
                w = ((n & k) == 0) ? w.getLeft() : w.getRight();
                k >>= 1;
            }

            if ((n & 1) == 0) {
                p = w.getLeft();
                w.linkLeft(v);
            } else {
                p = w.getRight();
                w.linkRight(v);
            }
            return p;
        }
    }

    void bubbleUp(HeapNode v) {
        addStep(v, REL.BOTTOM,
            H.minHeap ? "minheapbubbleup" : "maxheapbubbleup");
        pause();
        HeapNode w = v.getParent();
        while (w != null && v.prec(w)) {
            final HeapNode v1 = new HeapNode(v);
            final HeapNode v2 = new HeapNode(w);
            v1.mark();
            addToScene(v1);
            addToScene(v2);
            v.setKey(v2.getKey());
            w.setKey(v1.getKey());
            v1.goTo(w);
            v2.goTo(v);
            pause();
            v.setColor(v2.getColor());
            w.setColor(v1.getColor());
            v1.unmark();
            removeFromScene(v1);
            removeFromScene(v2);
            v = w;
            w = w.getParent();
        }
        v.unmark();
        addNote("done");
    }

    void bubbleDown(HeapNode v) {
        while (true) {
            HeapNode w = null;
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
            v.setKey(v2.getKey());
            w.setKey(v1.getKey());
            v1.goTo(w);
            v2.goTo(v);
            pause();
            v.setColor(v2.getColor());
            w.setColor(v1.getColor());
            removeFromScene(v1);
            removeFromScene(v2);
            v = w;
        }
    }
}
