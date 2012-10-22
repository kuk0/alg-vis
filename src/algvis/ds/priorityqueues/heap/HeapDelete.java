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

import algvis.core.Node;

import java.util.HashMap;

public class HeapDelete extends HeapAlg {
	public HeapDelete(Heap H) {
		super(H);
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("deletion");
		if (H.getN() == 0) {
			addStep("heapempty");
			return;
		}
		if (H.getN() == 1) {
			HeapNode v = H.getRoot();
			addToScene(v);
			H.setRoot(null);
			H.setN(H.getN() - 1);
			v.goDown();
			removeFromScene(v);
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
		v = w;
		addToScene(v);
		H.setN(H.getN() - 1);
		if ((n & 1) == 0) {
			w.getParent().setLeft(null);
		} else {
			w.getParent().setRight(null);
		}
		v.goToRoot();
		H.reposition();
		pause();

		// TODO Takto asi nie (a mozno hej)
		H.getRoot().setKey(v.getKey());
		removeFromScene(v);
		if (H.minHeap) {
			addStep("minheapbubbledown");
		} else {
			addStep("maxheapbubbledown");
		}
		// pause();

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
			HeapNode v1 = new HeapNode(v);
			HeapNode v2 = new HeapNode(w);
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
			removeFromScene(v1);
			removeFromScene(v2);
			v = w;
		}

		addStep("done");
	}

	@Override
	public HashMap<String, Object> getResult() {
		return null;
	}
}
