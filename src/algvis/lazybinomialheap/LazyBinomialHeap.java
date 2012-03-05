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
import algvis.binomialheap.BinomialHeap;
import algvis.core.MeldablePQButtons;
import algvis.core.Node;
import algvis.core.Pair;
import algvis.core.View;
import algvis.core.VisPanel;

public class LazyBinomialHeap extends BinomialHeap {
	public static String dsName = "lazybinheap";
	BinHeapNode[] cleanup;
	public static int arrayheight = 2 * minsepy;

	@Override
	public String getName() {
		return "lazybinheap";
	}

	public LazyBinomialHeap(VisPanel M) {
		super(M);
		reposition();
	}

	@Override
	public void insert(int x) {
		start(new LazyBinHeapInsert(this, active, x));
	}

	@Override
	public void delete() {
		start(new LazyBinHeapDelete(this, active));
	}

	@Override
	public void meld(int i, int j) {
		Pair p = chooseHeaps(i, j);
		i = p.first;
		j = p.second;
		((MeldablePQButtons) M.B).activeHeap.setValue(i);
		start(new LazyBinHeapMeld(this, i, j));
	}

	@Override
	public void draw(View V) {
		super.draw(V);
		if (cleanup != null && root[active] != null) {
			int x = root[active].x, y = -arrayheight;
			for (int i = 0; i < cleanup.length; ++i) {
				V.drawSquare(x, y, Node.radius);
				V.drawStringTop("" + i, x, y - Node.radius + 1, 9);
				if (cleanup[i] == null) {
					V.drawLine(x-Node.radius, y+Node.radius, x+Node.radius, y-Node.radius);
				} else {
					V.drawArrow(x, y, cleanup[i].x, cleanup[i].y - minsepy + Node.radius);
				}
				x += 2 * Node.radius;
			}
		}
	}

	@Override
	public void reposition() {
		super.reposition();
		M.screen.V.miny = -arrayheight - 50;
	}
}
