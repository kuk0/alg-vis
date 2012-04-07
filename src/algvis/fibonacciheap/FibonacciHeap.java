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
import algvis.core.Node;
import algvis.core.View;
import algvis.core.VisPanel;
import algvis.lazybinomialheap.LazyBinomialHeap;

public class FibonacciHeap extends LazyBinomialHeap {
	public static String dsName = "fibheap";
	FibNode root, u, v;
	
	@Override
	public String getName() {
		return "fibheap";
	}

	public FibonacciHeap(VisPanel M) {
		super(M);
		root = new FibNode();
		u = new FibNode();
		u.x = 200; u.y = 150; u.vx = 11;
		v = new FibNode();
		v.x = 300; v.y = 50; v.vx = 5;
	}

	@Override
	public void decreaseKey(Node v, int delta) {
		start(new FibHeapDecrKey(this, (BinHeapNode) v, delta, active));
	}
	
	@Override
	public void draw(View V) {
		if (root != null) {
			root.move();
			root.draw(V);
		}
		if (u != null) {
			u.move();
			u.draw(V);
		}
		if (v != null) {
			v.move();
			v.draw(V);
		}
	}
}
