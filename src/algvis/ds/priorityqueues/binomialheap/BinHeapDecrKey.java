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
package algvis.ds.priorityqueues.binomialheap;

import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.ui.InputField;

import java.util.HashMap;

public class BinHeapDecrKey extends Algorithm {
	private final int delta;
	private final BinomialHeap H;
	private BinHeapNode v;

	public BinHeapDecrKey(BinomialHeap H, BinHeapNode v, int delta) {
		super(H.panel, null);
		this.H = H;
		this.v = v;
		this.delta = delta;
		// setHeader("insertion");
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		if (H.minHeap) {
			v.setKey(v.getKey() - delta);
			if (v.getKey() < 1)
				v.setKey(1);
		} else {
			v.setKey(v.getKey() + delta);
			if (v.getKey() > InputField.MAX)
				v.setKey(InputField.MAX);
		}
		BinHeapNode w = v.parent;
		while (w != null && v.prec(w)) {
			BinHeapNode v1 = new BinHeapNode(v);
			BinHeapNode v2 = new BinHeapNode(w);
			addToScene(v1);
			addToScene(v2);
			v1.setKey(Node.NOKEY);
			w.setKey(Node.NOKEY);
			v1.goTo(w);
			v2.goTo(v);
			pause();
			v.setKey(v2.getKey());
			w.setKey(v1.getKey());
			v.setColor(v2.getColor());
			w.setColor(v1.getColor());
			removeFromScene(v1);
			removeFromScene(v2);
			v = w;
			w = w.parent;
		}
		if (v.prec(H.min[H.active])) {
			H.min[H.active] = v;
		}
	}

	@Override
	public HashMap<String, Object> getResult() {
		return null;
	}
}
