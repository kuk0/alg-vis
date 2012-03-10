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

public class HeapInsert extends HeapAlg {
	public HeapInsert(Heap H, int x) {
		super(H);
		H.v = v = new HeapNode(H, x);
		setHeader("insertion");
	}

	@Override
	public void run() {
		if (H.n == 1000) {
			addStep("heapfull");
			H.v = null;
			return;
		}
		HeapNode w;

		// link
		++H.n;
		int n = H.n, k = 1 << 10;
		if (n == 1) {
			H.root = w = v;
			v.goToRoot();
			mysuspend();
		} else {
			while ((k & n) == 0) {
				k >>= 1;
			}
			k >>= 1;
			w = H.root;
			while (k > 1) {
				w = ((n & k) == 0) ? w.getLeft() : w.getRight();
				k >>= 1;
			}
			if ((k & n) == 0) {
				w.linkLeft(v);
			} else {
				w.linkRight(v);
			}
			H.reposition();
			mysuspend();
		}
		H.v = null;

		// mysuspend();
		bubbleup(v);
	}
}
