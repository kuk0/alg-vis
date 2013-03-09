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

public class BinHeapDelete extends BinHeapAlg {

	public BinHeapDelete(BinomialHeap H) {
		super(H);
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		final int i = H.active;
		setHeader(H.minHeap ? "delete-min" : "delete-max");
		if (H.root[i] == null) {
			// empty - done;
			addStep("heapempty");
			addNote("done");
			return;
		}
		BinHeapNode v, w;
		final BinHeapNode d = H.min[i];
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
		addStep(H.minHeap ? "binheap-findmin" : "binheap-findmax");
		H.root[0] = v = w = d.child;
		d.child = null;
		pause();

		H.reposition();
		if (w == null) {
			// no children - done;
			addStep("binheap-nochildren");
			addNote("done");
			pause();
			return;
		}
		// reverse & find min
		addNote("binheap-meldchildren");
		H.min[0] = w;
		do {
			w.parent = null;
			if (w.prec(H.min[0])) {
				H.min[0] = w;
			}
			final BinHeapNode tl = w.left, tr = w.right;
			w.left = tr;
			w.right = tl;
			w = tr;
		} while (w != v);
		H.root[0] = w = v = w.right;
		H.reposition();
		pause();

		if (H.root[i] == null) {
			// heap #1 is empty; done;
			H.root[i] = H.root[0];
			H.min[i] = H.min[0];
			H.root[0] = null;
			H.min[0] = null;
			H.reposition();
			addStep("binheap-top-empty");
			addNote("done");
			return;
		}

		meld(i);
	}
}
