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
import algvis.ui.view.REL;

public class HeapDelete extends HeapAlg {
    public HeapDelete(Heap H) {
        super(H);
    }

    @Override
    public void runAlgorithm() {
        setHeader(H.minHeap ? "delete-min" : "delete-max");
        if (H.getN() == 0) {
            addStep(H.getBoundingBoxDef(), 200, REL.TOP, "heapempty");
            addNote("done");
            return;
        }
        if (H.getN() == 1) {
            addStep(H, 200, REL.TOP, "heap-last");
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

        v = setNode(H.getN(), null);
        addToScene(v);
        H.setN(H.getN() - 1);
        addStep(v, REL.BOTTOM, "heap-replace-root");
        H.getRoot().setKey(Node.NOKEY);
        pause();
        
        v.goToRoot();
        H.reposition();
        pause();

        H.getRoot().setKey(v.getKey());
        removeFromScene(v);
        String bubbleDownText = (H.minHeap ? "min" : "max") + "heapbubbledown";
        addStep(H, 200, REL.TOP, bubbleDownText);

        bubbleDown(H.getRoot());
        addNote("done");
    }
}
