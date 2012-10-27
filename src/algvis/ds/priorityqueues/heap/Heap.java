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
package algvis.ds.priorityqueues.heap;

import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ds.priorityqueues.PriorityQueue;
import algvis.internationalization.Languages;
import algvis.ui.VisPanel;
import algvis.ui.view.ClickListener;
import algvis.ui.view.View;

public class Heap extends PriorityQueue implements ClickListener {
	public static final String dsName = "heap";
	private int n = 0;
	private HeapNode root;

	@Override
	public String getName() {
		return "heap";
	}

	public Heap(VisPanel M) {
		super(M);
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
		if (root != null) {
			setRoot(null);
			setN(0);
			setStats();
		}
	}

	@Override
	public String stats() {
		if (getN() == 0) {
			return Languages.getString("size") + ": 0 ("
					+ Languages.getString("emptyheap") + ")";
		} else if (getN() == 1000) {
			return Languages.getString("size") + ": 1000 ("
					+ Languages.getString("fullheap") + ")";
		} else {
			return Languages.getString("size") + ": " + getN();
		}
	}

	@Override
	public void draw(View V) {
		if (getRoot() != null) {
			getRoot().drawTree(V);
		}
	}

	@Override
	protected void move() {
		if (root != null) {
			root.moveTree();
		}
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		return root == null ? null : root.getBoundingBox();
	}

	@Override
	protected void endAnimation() {
		// TODO radsej asi nejak preliezt strom, root.endAnimation by sa malo tykat len roota
		root.endAnimation();
	}

	@Override
	protected boolean isAnimationDone() {
		// TODO takisto (alebo este sa uvidi)
		return root.isAnimationDone();
	}

	public void reposition() {
		if (getRoot() != null) {
			getRoot().reposition();
			panel.screen.V.setBounds(x1, y1, x2, y2);
		}
	}

	@Override
	public void mouseClicked(int x, int y) {
		if (getRoot() == null)
			return;
		BSTNode v = getRoot().find(x, y);
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

	public HeapNode getRoot() {
		return root;
	}

	public void setRoot(HeapNode root) {
		this.root = root;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "n", n);
		HashtableStoreSupport.store(state, hash + "root", root);
		if (root != null) root.storeState(state);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object n = state.get(hash + "n");
		if (n != null) this.n = (Integer) HashtableStoreSupport.restore(n);
		Object root = state.get(hash + "root");
		if (root != null) this.root = (HeapNode) HashtableStoreSupport.restore(root);
		if (this.root != null) this.root.restoreState(state);
	}
}
