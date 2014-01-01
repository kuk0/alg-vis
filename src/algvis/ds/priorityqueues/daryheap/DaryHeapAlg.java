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

import algvis.core.Algorithm;
import algvis.core.Node;

abstract class DaryHeapAlg extends Algorithm {
    final DaryHeap H;

    DaryHeapAlg(DaryHeap H) {
        super(H.panel);
        this.H = H;
    }

    void bubbleup(DaryHeapNode v) throws InterruptedException {
        DaryHeapNode w = v.getParent();
        while (w != null && v.prec(w)) {
            final DaryHeapNode v1 = new DaryHeapNode(v);
            v1.mark();
            addToScene(v1);
            final DaryHeapNode v2 = new DaryHeapNode(w);
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
            v1.unmark();
            removeFromScene(v1);
            removeFromScene(v2);
            v = w;
            w = w.getParent();
        }

        addNote("done");
    }

    void bubbledown(DaryHeapNode v) throws InterruptedException {
        DaryHeapNode w;

        while (true) {
            if (v.isLeaf()) {
                break;
            }

            w = v.findMaxSon();
            if (v.prec(w)) {
                break;
            }
            final DaryHeapNode v1 = new DaryHeapNode(v);
            v1.mark();
            addToScene(v1);
            final DaryHeapNode v2 = new DaryHeapNode(w);
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
            v1.unmark();
            removeFromScene(v1);
            removeFromScene(v2);
            v = w;
        }

        addNote("done");
    }
}
