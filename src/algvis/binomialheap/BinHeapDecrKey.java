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
package algvis.binomialheap;

import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.gui.InputField;

public class BinHeapDecrKey extends Algorithm {
	private final int delta;
	private final BinomialHeap H;
	private BinHeapNode v;

	public BinHeapDecrKey(BinomialHeap H, BinHeapNode v, int delta) {
		super(H);
		this.H = H;
		this.v = v;
		this.delta = delta;
		// setHeader("insertion");
	}

	@Override
	public void run() {
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
			H.v = new BinHeapNode(v);
			H.v2 = new BinHeapNode(w);
			v.setKey(Node.NOKEY);
			w.setKey(Node.NOKEY);
			H.v.goTo(w);
			H.v2.goTo(v);
			mysuspend();
			v.setKey(H.v2.getKey());
			w.setKey(H.v.getKey());
			v.setColor(H.v2.getColor());
			w.setColor(H.v.getColor());
			H.v = null;
			H.v2 = null;
			v = w;
			w = w.parent;
		}
		if (v.prec(H.min[H.active])) {
			H.min[H.active] = v;
		}
	}
}
