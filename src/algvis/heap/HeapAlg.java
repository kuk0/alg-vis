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
package algvis.heap;

import algvis.core.Algorithm;
import algvis.visual.Node;

public class HeapAlg extends Algorithm {
	Heap H;
	HeapNode v;

	public HeapAlg(Heap H) {
		super(H);
		this.H = H;
	}

	public void bubbleup(HeapNode v) {
		if (H.minHeap) {
			addStep("minheapbubbleup");
		} else {
			addStep("maxheapbubbleup");
		}
		HeapNode w = v.getParent();
		while (w != null && v.prec(w)) {
			H.v = new HeapNode(v);
			H.v2 = new HeapNode(w);
			v.key = Node.NOKEY;
			w.key = Node.NOKEY;
			H.v.goTo(w);
			H.v2.goTo(v);
			mysuspend();
			v.key = H.v2.key;
			w.key = H.v.key;
			v.setColor(H.v2.getColor());
			w.setColor(H.v.getColor());
			H.v = null;
			H.v2 = null;
			v = w;
			w = w.getParent();
		}
		addStep("done");
	}
}
