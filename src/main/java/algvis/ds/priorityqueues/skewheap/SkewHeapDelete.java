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
package algvis.ds.priorityqueues.skewheap;

import algvis.core.DataStructure;

public class SkewHeapDelete extends SkewHeapAlg {

    public SkewHeapDelete(SkewHeap H) {
        super(H);
    }

    @Override
    public void runAlgorithm() {
        setHeader(H.minHeap ? "delete-min" : "delete-max");
        final int i = H.active;

        if (H.root[i] == null) {
            return;
        }

        if (!H.minHeap) {
            addStep("maximum", H.root[i].getKey());
        } else {
            addStep("minimum", H.root[i].getKey());
        }

        pause();

        SkewHeapNode tmp = H.root[i];
        H.root[i] = tmp.getLeft();
        H.root[0] = tmp.getRight();
        tmp = null;

        if (H.root[i] == null) {
            H.root[i] = H.root[0];
            H.root[0] = null;
            if (H.root[i] != null) {
                H.root[i].highlightTree();
                H.root[i].repos(H.root[i].x, H.root[i].y
                    - (DataStructure.minsepy));// +
                // 2
                // *
                // SkewHeapNode.RADIUS));
            }
            // heap #1 is empty; done;
            return;
        }
        H.root[i].setParent(null);

        if (H.root[0] == null) {
            H.root[i].repos(H.root[i].x, H.root[i].y - (DataStructure.minsepy));// +
            // 2
            // *
            // SkewHeapNode.RADIUS));
            // heap #2 is empty; done;
            return;
        }
        H.root[0].setParent(null);

        H.root[i].repos(H.root[i].x, H.root[i].y - (DataStructure.minsepy));// + 2 *
        // SkewHeapNode.RADIUS));
        H.root[0].repos(H.root[0].x, H.root[0].y - (DataStructure.minsepy));// + 2 *
        // SkewHeapNode.RADIUS));

        pause();
        meld(i);
    }
}
