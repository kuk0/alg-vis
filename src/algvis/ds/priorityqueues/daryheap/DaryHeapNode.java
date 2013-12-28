/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
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
package algvis.ds.priorityqueues.daryheap;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Vector;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.core.visual.ZDepth;
import algvis.ds.priorityqueues.PriorityQueue;
import algvis.ds.priorityqueues.heap.HeapNode;
import algvis.ui.view.View;

public class DaryHeapNode extends HeapNode {
	private int width;// , leftw, rightw;
	private DaryHeapNode parent = null;
	int nson = -1; // kolky je to syn svojho otca
	protected Vector<DaryHeapNode> c;

	int nnodes = 1;
	private int height = 1; // pre setStats

	private DaryHeapNode(DaryHeap D, int key, int x, int y, int zDepth) {
		super(D, key, x, y, zDepth);
		bgKeyColor();
		c = new Vector<DaryHeapNode>(D.getOrder());
		// setColor(NodeColor.NORMAL);
		width = DaryHeap.minsepx;
		// mark();
	}

	public DaryHeapNode(DaryHeap D, int key, int zDepth) {
		this(D, key, 0, Node.UPY, zDepth);
	}

	public DaryHeapNode(DaryHeapNode v) {
		this((DaryHeap) v.D, v.getKey(), v.tox, v.toy, ZDepth.ACTIONNODE);
	}

	@Override
	public boolean isRoot() {
		return parent == null;
	}

	@Override
	public boolean isLeaf() {
		return c.isEmpty();
	}

	/*
	 * public boolean isLeft() { return parent.left==this; } public void
	 * linkleft(BSTNode v) { left = v; v.parent = this; } public void
	 * linkright(BSTNode v) { right = v; v.parent = this; } public void
	 * isolate() { left = right = parent = null; }
	 */

	@Override
	public void calcTree() {
		nnodes = 1;
		for (final DaryHeapNode node : c) {
			node.calcTree();
			nnodes += node.nnodes;
		}
		height = 1 + (isLeaf() ? 0 : c.get(0).height);
	}

	/*
	 * public void addLeaf(int x) { c[numChildren++].key = x; for (int i =
	 * numChildren - 1; i > 0; --i) { if (c[i].key < c[i - 1].key) { int tmp =
	 * c[i].key; c[i].key = c[i - 1].key; c[i - 1].key = tmp; } } width =
	 * _width(); }
	 */

	public int order() {
		for (int i = 0; i < parent.c.size(); ++i) {
			if (getParent().c.get(i) == this) {
				return i;
			}
		}
		return -5; // TODO: vypindat exception
	}

	/*
	 * int _width() {
	 * 
	 * if (key != Node.NOKEY && numChildren > 0) { return (2 * Node.radius +
	 * DaryHeap.minsepx)*numChildren - DaryHeap.minsepx; } else { return
	 * 2*Node.radius; } }
	 */

	@Override
	public void drawTree(View v) {
		drawTree2(v);
	}

	private void drawTree2(View v) {
		if (state != INVISIBLE) {// && !thread) {
			/*
			 * if (thread) { v.setColor(Color.yellow); } else {
			 */
			v.setColor(Color.black);
			// }
			for (final DaryHeapNode node : c) {
				v.drawLine(x, y, node.x, node.y);
			}
		}
		for (final DaryHeapNode node : c) {
			node.drawTree2(v);
		}
		/*
		 * v.setColor(Color.LIGHT_GRAY); ++i; v.drawLine(x, y, x, -20);
		 * v.drawString("" + i, x, -23, 10);
		 */
		draw(v);
	}

	/*
	 * public void drawTree(View v) { for (int i = 0; i < numChildren; ++i) {
	 * v.setColor(Color.black); /* int xx, yy; if (i==0 || i==numChildren-1) {
	 * xx = x; yy = y; } else { xx = (pos(i-1)+pos(i))/2; yy = y+D.radius; }
	 *//*
		 * v.drawLine(x, y, c[i].x, c[i].y - Node.radius); c[i].drawTree(v); }
		 * draw(v); }
		 */
	@Override
	public void moveTree() {
		for (final DaryHeapNode node : c) {
			node.moveTree();
		}
		move();
	}

	@Override
	public void reboxTree() {
		for (final DaryHeapNode node : c) {
			node.reboxTree();
		}
		rebox();
	}

	@Override
	public void rebox() {

		leftw = 0; // -DaryHeap.minsepx / 2;
		rightw = 0; // -DaryHeap.minsepx / 2;
		if (isLeaf()) {
			leftw = DaryHeap.minsepx / 2;
			rightw = DaryHeap.minsepx / 2;
			width = leftw + rightw;
			return;
		}

		if (c.size() < ((DaryHeap) D).getOrder()) {
			leftw = (((DaryHeap) D).getOrder() / 2) * DaryHeap.minsepx;
			if (((DaryHeap) D).getOrder() % 2 == 1) {
				leftw += (DaryHeap.minsepx / 2);
			}
			if (c.size() > ((DaryHeap) D).getOrder() / 2) {
				rightw = leftw - (((DaryHeap) D).getOrder() - c.size())
						* DaryHeap.minsepx;
			} else {
				rightw = DaryHeap.minsepx / 2;
			}

			width = leftw + rightw;
			return;
		}

		leftw = 0;
		rightw = 0;
		for (int i = 1; i <= ((DaryHeap) D).getOrder() / 2; i++) {
			leftw += c.get(i - 1).width;
		}

		for (int i = (((DaryHeap) D).getOrder() / 2) + 1; i <= ((DaryHeap) D)
				.getOrder(); i++) {
			rightw += c.get(i - 1).width;
		}

		if (((DaryHeap) D).getOrder() % 2 == 1) {
			rightw -= c.get(((DaryHeap) D).getOrder() / 2).leftw;
			leftw += c.get(((DaryHeap) D).getOrder() / 2).leftw;
		}

		width = leftw + rightw;
	}

	private void repos() {
		if (isRoot()) {
			goToRoot();
			D.x1 = -width / 2; // -leftw;
			D.x2 = width / 2; // rightw;
			D.y2 = this.toy;
		}
		if (this.toy > D.y2) {
			D.y2 = this.toy;
		}
		/*
		 * if (numChildren == 0) { return; }
		 */

		for (int i = 0; i < c.size(); i++) {
			if (i == 0) {
				c.firstElement().goTo(
						this.tox - (this.leftw) + c.firstElement().leftw,
						this.toy + DataStructure.minsepy);
			} else {
				c.get(i)
						.goTo(c.get(i - 1).tox + c.get(i - 1).rightw
								+ c.get(i).leftw,
								this.toy + DataStructure.minsepy);
			}
			c.get(i).repos();
		}
	}

	public void _reposition() {
		reboxTree();
		repos();
	}

	@Override
	public DaryHeapNode getParent() {
		return parent;
	}

	void setParent(DaryHeapNode v) {
		this.parent = v;
	}

	public boolean prec(DaryHeapNode v) {
		if (((PriorityQueue) D).minHeap) {
			return this.getKey() < v.getKey();
		} else {
			return this.getKey() > v.getKey();
		}
	}

	/**
	 * Precedes or equals (see prec).
	 */
	public boolean preceq(DaryHeapNode v) {
		if (((PriorityQueue) D).minHeap) {
			return this.getKey() <= v.getKey();
		} else {
			return this.getKey() >= v.getKey();
		}
	}

	// vracia otca najblizsieho suseda vpravo
	// funguje iba pre listy
	public DaryHeapNode nextNeighbour() {
		if (isRoot()) {
			return this;
		}

		if (getParent().c.size() < ((DaryHeap) D).getOrder()) { // pre root
																// nson ==
																// -1
			// System.out.print("malo synov kluca " + getParent().key +
			// ", konkretne " + getParent().numChildren + " a order mame prave "
			// + ((DaryHeap) D).getOrder() + "\n" );
			return getParent();
		}

		// if (this.nson == ((DaryHeap) D).getOrder()){
		DaryHeapNode v = this;
		while ((!v.isRoot()) && (v.nson == ((DaryHeap) D).getOrder())) {
			v = v.getParent();
		}

		if (v.isRoot()) {
			while (!v.c.isEmpty()) {
				v = v.c.firstElement();
			}
			return v;
		}

		v = v.getParent().c.get(v.nson); // v poli je n-ty syn na mieste (nson -
											// 1)
		while (!v.c.isEmpty()) {
			v = v.c.firstElement();
		}
		return v;
		// }

		// return this.getParent();
	}

	// vracia najblizsieho suseda vlavo, (prerobit na otca?)
	// funguje iba pre listy
	public DaryHeapNode prevneighbour() {
		if (isRoot()) {
			return null;
		}

		if (nson > 1) { // pre root nson == -1
			// System.out.print("dost synov, konkretne " +
			// getParent().numChildren + " a order mame prave " + ((DaryHeap)
			// D).getOrder() + "\n" );
			return getParent().c.get(nson - 2);
		}

		DaryHeapNode v = this;
		while ((!v.isRoot()) && (v.nson == 1)) {
			v = v.getParent();
		}
		if (!v.isRoot()) {
			v = v.getParent().c.get(v.nson - 2);
		}

		while ((v.c.get(((DaryHeap) D).getOrder() - 1) != null)) { // !v.isLeaf()
			v = v.c.get(((DaryHeap) D).getOrder() - 1);
		}

		return v;

	}

	public void linkNewSon(DaryHeapNode v) {
		v.setParent(this);
		c.add(v);
		v.nson = c.size();
		((DaryHeap) D).last = v;
	}

	@Override
	public DaryHeapNode find(int x, int y) {
		if (inside(x, y)) {
			return this;
		}

		for (final DaryHeapNode node : c) {
			final DaryHeapNode tmp = node.find(x, y);
			if (tmp != null) {
				return tmp;
			}
		}
		return null;
	}

	public DaryHeapNode findMaxSon() {
		DaryHeapNode v = c.firstElement();
		for (final DaryHeapNode node : c) {
			if (node.prec(v)) {
				v = node;
			}
		}

		return v;
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "parent", parent);
		HashtableStoreSupport.store(state, hash + "c", c.clone());
		for (final DaryHeapNode node : c) {
			node.storeState(state);
		}
		HashtableStoreSupport.store(state, hash + "nson", nson);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		final Object parent = state.get(hash + "parent");
		if (parent != null) {
			this.parent = (DaryHeapNode) HashtableStoreSupport.restore(parent);
		}

		final Object c = state.get(hash + "c");
		if (c != null) {
			this.c = (Vector<DaryHeapNode>) HashtableStoreSupport.restore(c);
		}
		for (final DaryHeapNode node : this.c) {
			node.restoreState(state);
		}

		final Object nson = state.get(hash + "nson");
		if (nson != null) {
			this.nson = (Integer) HashtableStoreSupport.restore(nson);
		}
	}
}