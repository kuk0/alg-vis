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
import algvis.ui.view.REL;

public class HeapInsert extends HeapAlg {
    private final int K;

    public HeapInsert(Heap H, int x) {
        super(H);
        K = x;
    }

    @Override
    public void runAlgorithm() {
        setHeader("insert", K);
        final HeapNode v = new HeapNode(H, K, ZDepth.ACTIONNODE);
        addToScene(v);
        if (H.getN() == 1000) {
            addStep(v, REL.TOP, "heapfull");
            removeFromScene(v);
            return;
        }

        // link
        H.setN(H.getN() + 1);
        setNode(H.getN(), v);

        H.reposition();
        v.mark();
        addStep(v, REL.BOTTOM, "heap-insert-last");
        pause();
        removeFromScene(v);
        v.unmark();

        bubbleUp(v);
    }
}
