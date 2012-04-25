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
package algvis.fibonacciheap;

import algvis.binomialheap.BinHeapNode;
import algvis.binomialheap.BinomialHeap;
import algvis.core.Algorithm;
import algvis.core.InputField;

public class FibHeapDecrKey extends Algorithm {
	int delta, i;
	BinomialHeap H;
	BinHeapNode v;

	public FibHeapDecrKey(BinomialHeap H, BinHeapNode v, int delta, int i) {
		super(H);
		this.H = H;
		this.v = v;
		this.delta = delta;
		this.i = i;
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
		// if (w == null) return;
		while (w != null) {
			v.unlink();
			v.unmarkCut();
			H.root[i].linkLeft(v);
			if (v.prec(H.min[i])) {
				H.min[i] = v;
			}
			H.reposition();
			mysuspend();
			if (w.cut) {
				v = w;
				w = v.parent;
			} else {
				w.markCut();
				H.reposition();
				break;
			}
		}
	}
}
