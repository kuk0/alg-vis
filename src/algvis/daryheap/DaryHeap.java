/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováè, Katarína Kotrlová, Pavol Lukèa, Viktor Tomkoviè, Tatiana Tóthová
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
package algvis.daryheap;

import algvis.core.Node;
import algvis.core.PriorityQueue;
import algvis.gui.VisPanel;
import algvis.gui.view.ClickListener;
import algvis.gui.view.View;

public class DaryHeap extends PriorityQueue implements ClickListener{
	public static String dsName = "daryheap";
	DaryHeapNode root = null, v = null, v2 = null;
	DaryHeapNode last = null;
	int order = 5;
	public static final int minsepx = 30;  //zmenit na mensie

	public DaryHeap(VisPanel M) {
		super(M, dsName);
		last = new DaryHeapNode(this, 47); 
		M.screen.V.setDS(this);
	}

	@Override
	public void mouseClicked(int x, int y) {
		if (root == null)
			return;
		DaryHeapNode v = root.find(x, y);
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

	@Override
	public void insert(int x) {
		start(new DaryHeapInsert(this, x));
		
	}

	@Override
	public void delete() {
		start(new DaryHeapDelete(this));
		
	}

	@Override
	public void decreaseKey(Node v, int delta) {
		if (v == null) {
		// TODO: vypindat
		} else {
			start(new DaryHeapDecrKey(this, (DaryHeapNode) v, delta));
		}
	}

	@Override
	public String getName() {
		return dsName;
	}

	@Override
	public String stats() {

		if (root == null) {
			return M.S.L.getString("size") + ": 0 ("
					+ M.S.L.getString("emptyheap") + ")";
		} else if (root.nnodes == 1000) {
			return M.S.L.getString("size") + ": 1000 ("
					+ M.S.L.getString("fullheap") + ")";
		} else {
			return M.S.L.getString("size") + ": " + root.nnodes;
		}
	}

	@Override
	public void clear() {
		root = v = null;
		setStats();
		
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
			root._reposition();
			M.screen.V.setBounds(x1, y1, x2, y2);
		}
	}

	public int getOrder(){
		return this.order;
	}

}
