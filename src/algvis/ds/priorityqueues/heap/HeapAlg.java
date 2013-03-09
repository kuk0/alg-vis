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
package algvis.ds.priorityqueues.heap;

import algvis.core.Algorithm;
import algvis.core.Node;

abstract class HeapAlg extends Algorithm {
	final Heap H;

	HeapAlg(Heap H) {
		super(H.panel, null);
		this.H = H;
	}

	void bubbleup(HeapNode v) throws InterruptedException {
		addStep(H.minHeap ? "minheapbubbleup" : "maxheapbubbleup");
		HeapNode w = v.getParent();
		while (w != null && v.prec(w)) {
			final HeapNode v1 = new HeapNode(v);
			final HeapNode v2 = new HeapNode(w);
			v1.mark();
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
			v1.unmark();
			removeFromScene(v1);
			removeFromScene(v2);
			v = w;
			w = w.getParent();
		}
		v.unmark();
		addNote("done");
	}
}
