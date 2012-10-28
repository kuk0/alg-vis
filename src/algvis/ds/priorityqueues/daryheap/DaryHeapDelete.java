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

import algvis.core.Node;

public class DaryHeapDelete extends DaryHeapAlg {
	public DaryHeapDelete(DaryHeap H) {
		super(H);
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("deletion");
		if (H.root == null) {
			addStep("heapempty");
			H.last = null;
			return;
		}

		if (H.root.c.size() == 0) {
			DaryHeapNode v = H.root;
			if (H.minHeap) {
				addStep("minimum", H.root.getKey());
			} else {
				addStep("maximum", H.root.getKey());
			}
			H.root = null;
			addToScene(v);
			v.mark();
			// --H.n;
			pause();
			v.unmark();
			v.goDown();
			removeFromScene(v);
			return;
		}
		if (H.minHeap) {
			addStep("minimum", H.root.getKey());
		} else {
			addStep("maximum", H.root.getKey());
		}
		H.root.mark();
		pause();
		// H.root.unmark();
		addStep("heapchange");
		pause();
		H.root.unmark();

		DaryHeapNode v = new DaryHeapNode(H.last);
		DaryHeapNode v2 = new DaryHeapNode(H.root);
		addToScene(v);
		addToScene(v2);
		H.last.setKey(Node.NOKEY);
		H.root.setKey(Node.NOKEY);
		v.goToRoot();
		v2.goTo(H.last);
		v2.mark();
		pause();
		H.last.setKey(v2.getKey());
		H.root.setKey(v.getKey());
		H.last.setColor(v2.getColor());
		H.root.setColor(v.getColor());
		removeFromScene(v);
		removeFromScene(v2);

		v = H.last;
		addToScene(v);
		H.last = H.last.prevneighbour();
		v.goDown();
		removeFromScene(v);
		v.getParent().c.set(v.nson - 1, null);
		v.getParent().c.setSize(v.getParent().c.size() - 1);
		H.root.mark();
		H.reposition();

		if (H.minHeap) {
			addStep("mindheapbubbledown");
		} else {
			addStep("maxdheapbubbledown");
		}
		pause();

		H.root.unmark();
		bubbledown(H.root);
	}
}
