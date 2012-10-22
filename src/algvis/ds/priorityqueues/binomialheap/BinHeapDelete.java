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

import java.util.HashMap;

public class BinHeapDelete extends BinHeapAlg {

	public BinHeapDelete(BinomialHeap H) {
		super(H);
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		int i = H.active;
		if (H.root[i] == null) {
			// empty - done;
			return;
		}
		BinHeapNode v, w;
		BinHeapNode d = H.min[i];
		if (H.root[i] == d) {
			if (d.right == d) {
				H.root[i] = null;
			} else {
				H.root[i] = d.right;
			}
		}
		d.unlink();
		addToScene(d);
		d.goDown();
		removeFromScene(d);

		// find new min
		v = w = H.min[i] = H.root[i];
		if (H.root[i] != null) {
			do {
				if (w.prec(H.min[i])) {
					H.min[i] = w;
				}
				w = w.right;
			} while (w != v);
		}

		H.root[0] = v = w = d.child;
		d.child = null;
		H.reposition();
		if (w == null) {
			pause();
			// no children - done;
			return;
		}
		// reverse & find min
		H.min[0] = w;
		do {
			w.parent = null;
			if (w.prec(H.min[0])) {
				H.min[0] = w;
			}
			BinHeapNode tl = w.left, tr = w.right;
			w.left = tr;
			w.right = tl;
			w = tr;
		} while (w != v);
		H.root[0] = w = v = w.right;
		H.reposition();
		pause();

		if (H.root[i] == null) {
			H.root[i] = H.root[0];
			H.min[i] = H.min[0];
			H.root[0] = null;
			H.min[0] = null;
			H.reposition();
			// heap #1 is empty; done;
			return;
		}

		meld(i);
	}

	@Override
	public HashMap<String, Object> getResult() {
		return null;
	}
}
