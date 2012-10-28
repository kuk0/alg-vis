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
package algvis.ds.priorityqueues.lazybinomialheap;

import algvis.core.Algorithm;

public class LazyBinHeapMeld extends Algorithm {
	private final LazyBinomialHeap H;
	private final int i;
	private final int j;

	public LazyBinHeapMeld(LazyBinomialHeap H, int i, int j) {
		super(H.panel);
		this.H = H;
		this.i = i;
		this.j = j;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		if (i == j) {
			return;
		}
		if (H.root[j] != null) {
			H.root[j].highlightTree();
		}
		if (H.root[i] == null) {
			// heap #1 is empty; done;
			H.root[i] = H.root[j];
			H.min[i] = H.min[j];
			H.root[j] = H.min[j] = null;
		} else if (H.root[j] == null) {
			// heap #2 is empty; done;
		} else {
			H.root[i].linkAll(H.root[j]);
			if (H.min[j].prec(H.min[i])) {
				H.min[i] = H.min[j];
			}
			H.root[j] = H.min[j] = null;
		}
		H.reposition();
	}
}
