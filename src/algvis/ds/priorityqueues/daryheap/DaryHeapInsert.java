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
package algvis.ds.priorityqueues.daryheap;

import algvis.core.visual.ZDepth;

import java.util.HashMap;

public class DaryHeapInsert extends DaryHeapAlg {
	private final DaryHeap H; // prepisat na H
	private final DaryHeapNode v;

	public DaryHeapInsert(DaryHeap H, int x) {
		super(H);
		v = new DaryHeapNode(H, x, ZDepth.ACTIONNODE);
		this.H = H;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("insertion");
		addToScene(v);
		v.mark();
		if ((H.root != null) && (H.root.nnodes == 1000)) {
			addStep("heapfull");
			v.unmark();
			removeFromScene(v);
			return;
		}
		DaryHeapNode w;

		if (H.minHeap) {
			addStep("minheapbubbleup");
		} else {
			addStep("maxheapbubbleup");
		}

		// int n = H.root.nnodes - 1;
		if (H.root == null) {
			H.root = w = v;
			v.goToRoot();
			H.last = H.root;
			pause();
		} else { //najdeme miesto pre v
			w = H.last.nextNeighbour();
			w.linkNewSon(v);
			H.reposition();
			pause();
		}
		removeFromScene(v);

		++H.root.nnodes;
		// pause();
		v.unmark();
		bubbleup(v);
	}

	@Override
	public HashMap<String, Object> getResult() {
		return null;
	}

}
