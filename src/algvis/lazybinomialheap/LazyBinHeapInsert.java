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

public class LazyBinHeapInsert extends Algorithm {
	private final LazyBinomialHeap H;
	private final BinHeapNode v;
	private final int i;

	public LazyBinHeapInsert(LazyBinomialHeap H, int i, int x) {
		super(panel, d);
		this.H = H;
		this.i = i;
		v = new BinHeapNode(H, x);
	}

	@Override
	public void run() {
		if (H.root[i] == null) {
			H.root[i] = H.min[i] = v;
		} else {
			H.root[i].linkLeft(v);
			if (v.prec(H.min[i])) {
				H.min[i] = v;
			}
		}
		H.reposition();
		pause();
	}
}
