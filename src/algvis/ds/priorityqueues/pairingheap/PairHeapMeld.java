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
package algvis.ds.priorityqueues.pairingheap;

import algvis.core.DataStructure;
import algvis.ui.view.REL;

public class PairHeapMeld extends PairHeapAlg {
    private final int i;
    private final int j;

    public PairHeapMeld(DataStructure H, int i, int j) {
        super(H);
        this.i = i;
        this.j = j;
    }

    @Override
    public void runAlgorithm() {
        setHeader("meld", i, j);
        if (i == j) {
            return;
        }
        if (H.root[i] == null) {
            H.root[i] = H.root[j];
            H.root[j] = null;
            if (H.root[i] != null) {
                H.root[i].highlightTree();
            }
            H.reposition();
            // heap #1 is empty; done;
            return;
        }

        if (H.root[j] == null) {
            // heap #2 is empty; done;
            return;
        }

        H.root[0] = H.root[j];
        if (j != 0) {
            H.root[j] = null;
        }
        H.active = i;
        H.root[0].highlightTree();
        H.root[i].highlightTree();
        H.root[0].mark();
        H.root[i].mark();
        if (H.root[i].getKey() < H.root[0].getKey()) {
            addStep(H.root[i], 200, REL.TOP,
                H.minHeap ? "pairlinkmin" : "pairlinkmax", H.root[i].getKeyS(),
                H.root[0].getKeyS());
        } else {
            addStep(H.root[0], 200, REL.TOP,
                H.minHeap ? "pairlinkmin" : "pairlinkmax", H.root[0].getKeyS(),
                H.root[i].getKeyS());
        }
        pause();
        H.root[0].unmark();
        H.root[i].unmark();
        link(i, 0);
        H.root[0] = null;
        H.reposition();
        addNote("done");
    }
}
