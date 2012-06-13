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

import algvis.core.Node;

public class HeapDelete extends HeapAlg {
	public HeapDelete(Heap H) {
		super(H);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (H.getN() == 0) {
			addStep("heapempty");
			return;
		}
		if (H.getN() == 1) {
			H.setV(H.getRoot());
			H.setRoot(null);
			H.setN(H.getN() - 1);
			H.getV().goDown();
			mysuspend();
			return;
		}
		HeapNode v, w;

		int n = H.getN(), k = 1 << 10;
		while ((k & n) == 0) {
			k >>= 1;
		}
		k >>= 1;
		w = H.getRoot();
		while (k > 0) {
			w = ((n & k) == 0) ? w.getLeft() : w.getRight();
			k >>= 1;
		}
		H.setV(w);
		H.setN(H.getN() - 1);
		if ((n & 1) == 0) {
			w.getParent().setLeft(null);
		} else {
			w.getParent().setRight(null);
		}
		H.getV().goToRoot();
		H.reposition();
		mysuspend();

		H.getRoot().setKey(H.getV().getKey());
		H.setV(null);
		if (H.minHeap) {
			addStep("minheapbubbledown");
		} else {
			addStep("maxheapbubbledown");
		}
		// mysuspend();

		v = H.getRoot();
		while (true) {
			w = null;
			if (v.getLeft() != null) {
				w = v.getLeft();
			}
			if (v.getRight() != null && v.getRight().prec(w)) {
				w = v.getRight();
			}
			if (w == null || v.prec(w)) {
				break;
			}
			H.setV(new HeapNode(v));
			H.setV2(new HeapNode(w));
			v.setKey(Node.NOKEY);
			w.setKey(Node.NOKEY);
			H.getV().goTo(w);
			H.getV2().goTo(v);
			mysuspend();
			v.setKey(H.getV2().getKey());
			w.setKey(H.getV().getKey());
			v.setColor(H.getV2().getColor());
			w.setColor(H.getV().getColor());
			H.setV(null);
			H.setV2(null);
			v = w;
		}

		addStep("done");
	}
}
