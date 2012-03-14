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

import algvis.bst.BSTNode;
import algvis.core.ClickListener;
import algvis.core.Node;
import algvis.core.PriorityQueue;
import algvis.core.View;
import algvis.core.VisPanel;

public class Heap extends PriorityQueue implements ClickListener {
	public static String dsName = "heap";
	int n = 0;
	HeapNode root = null, v = null, v2 = null;

	@Override
	public String getName() {
		return "heap";
	}

	public Heap(VisPanel M) {
		super(M, dsName);
		M.screen.V.setDS(this);
	}

	@Override
	public void insert(int x) {
		start(new HeapInsert(this, x));
	}

	@Override
	public void delete() {
		start(new HeapDelete(this));
	}

	@Override
	public void decreaseKey(Node v, int delta) {
		if (v == null) {
			// TODO: vypindat
		} else
			start(new HeapDecrKey(this, (HeapNode) v, delta));
	}

	@Override
	public void clear() {
		root = v = v2 = null;
		n = 0;
		setStats();
	}

	@Override
	public String stats() {
		if (n == 0) {
			return M.S.L.getString("size") + ": 0 ("
					+ M.S.L.getString("emptyheap") + ")";
		} else if (n == 1000) {
			return M.S.L.getString("size") + ": 1000 ("
					+ M.S.L.getString("fullheap") + ")";
		} else {
			return M.S.L.getString("size") + ": " + n;
		}
	}

	@Override
	public void draw(View V) {
		if (root != null) {
			root.moveTree();
			root.drawTree(V);
		}
		if (v != null) {
			v.move();
			v.draw(V);
		}
		if (v2 != null) {
			v2.move();
			v2.draw(V);
		}
	}

	public void reposition() {
		if (root != null) {
			root.reposition();
			M.screen.V.setBounds(x1, y1, x2, y2);
		}
	}

	@Override
	public void mouseClicked(int x, int y) {
		if (root == null)
			return;
		BSTNode v = root.find(x, y);
		if (v != null) {
			if (v.marked) {
				v.unmark();
				chosen = null;
			} else {
				if (chosen != null)
					chosen.unmark();
				v.mark();
				chosen = v;
			}
		}
	}
}
