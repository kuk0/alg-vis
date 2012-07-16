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
package algvis.lazybinomialheap;

import algvis.binomialheap.BinHeapNode;
import algvis.core.Algorithm;

public class LazyBinHeapDelete extends Algorithm {
	private final LazyBinomialHeap H;
	private final int i;

	int lg(int n) {
		return (int) Math.ceil(Math.log(n) / Math.log(2));
	}

	public LazyBinHeapDelete(LazyBinomialHeap H, int i) {
		super(H);
		this.H = H;
		this.i = i;
		H.cleanup = new BinHeapNode[lg(H.size(i) + 1) + 1]; // TODO: change to rank
	}

	@Override
	public void run() {
		if (H.root[i] == null) {
			// empty
			H.cleanup = null;
			return;
		}
		mysuspend();
		// delete min
		BinHeapNode v, w, next;
		H.d = H.min[i];
		H.min[i] = null;
		if (H.root[i] == H.d) {
			if (H.d.right == H.d) {
				H.root[i] = null;
			} else {
				H.root[i] = H.d.right;
			}
		}
		H.d.unlink();
		H.d.goDown();

		v = w = H.d.child;
		H.d.child = null;
		if (w != null) {
			// reverse
			do {
				w.parent = null;
				BinHeapNode tl = w.left, tr = w.right;
				w.left = tr;
				w.right = tl;
				w = tr;
			} while (w != v);
			w = w.right;
			// link
			if (H.root[i] == null) {
				H.root[i] = w;
			} else {
				H.root[i].linkAll(w);
			}
		}
		H.reposition();
		mysuspend();

		// cleanup
		if (H.root[i] != null) {
			int h;
			w = H.root[i];
			do {
				w.mark();
				mysuspend();
				h = w.rank;
				v = H.cleanup[h];
				next = w.right;
				if (next == H.root[i]) {
					next = null;
				}
				w.unmark();
				while (v != null) {
					if (H.root[i] == w) {
						H.root[i] = w.right;
					}
					w.unlink();
					if (v.prec(w)) {
						v.linkChild(w);
						w = v;
					} else {
						v.linkRight(w);
						if (H.root[i] == v) {
							H.root[i] = w;
						}
						// mysuspend();
						v.unlink();
						w.linkChild(v);
						v = w;
					}
					// mysuspend();
					H.cleanup[h] = null;
					++h;
					v = H.cleanup[h];
					H.reposition();
					mysuspend();
				}
				H.cleanup[h] = w;
				w = next;
			} while (w != null);
		}
		H.cleanup = null;

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
	}
}
