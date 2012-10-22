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
package algvis.ds.priorityqueues.leftistheap;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ds.priorityqueues.MeldablePQ;
import algvis.ui.Fonts;
import algvis.ui.view.View;

import java.awt.*;
import java.util.Hashtable;

public class LeftHeapNode extends BSTNode {
	int rank = 1;
	private boolean doubleArrow = false;
	boolean dashedRightLine = false; // if true the line leading to the right son is dashed
	private boolean dashedLeftLine = false; // if true the line leading to the left son is dashed

	private LeftHeapNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		bgKeyColor();
	}

	public LeftHeapNode(DataStructure D, int key, int zDepth) {
		super(D, key, zDepth);
		bgKeyColor();
	}

	public LeftHeapNode(LeftHeapNode v) {
		this(v.D, v.getKey(), v.x, v.y);
	}

	/**
	 * v.prec(w) iff v precedes w in the heap order, i.e., should be higher in
	 * the heap v precedes w if v.key < w.key when we have a min heap, but v
	 * precedes w if v.key > w.key when we have a max heap
	 */

	public boolean prec(Node v) {
		if (((MeldablePQ) D).minHeap) {
			return this.getKey() < v.getKey();
		} else {
			return this.getKey() > v.getKey();
		}
	}

	/**
	 * Precedes or equals (see prec).
	 */
	public boolean preceq(Node v) {
		if (((MeldablePQ) D).minHeap) {
			return this.getKey() <= v.getKey();
		} else {
			return this.getKey() >= v.getKey();
		}
	}

	public void linkup(LeftHeapNode v) {
		if ((this.getParent() != null) && (v != null)) {
			LeftHeapNode tmp = this.getParent();
			v.setRight(this);
			this.setParent(v);
			v.setParent(tmp);
			v.getParent().setRight(v);
		}
	}

	public void swapChildren() {
		LeftHeapNode tmp = this.getLeft();
		this.setLeft(this.getRight());
		this.setRight(tmp);
	}

	public void setDoubleArrow(Node w) {
		dir = w;
		doubleArrow = true;
	}

	public void noDoubleArrow() {
		doubleArrow = false;
	}

	private void drawDoubleArrow(View v) {
		if (!doubleArrow || dir == null) {
			return;
		}
		int x1, y1, x2, y2;
		if (x < dir.x) {
			x1 = x;
			y1 = y;
			x2 = dir.x;
			y2 = dir.y;
		} else {
			x2 = x;
			y2 = y;
			x1 = dir.x;
			y1 = dir.y;
		}
		v.drawDoubleArrow(x1 + 2 * LeftHeapNode.RADIUS, y1, x2 - 2
				* LeftHeapNode.RADIUS, y2);
	}

	@Override
	public void draw(View v) {
		super.draw(v);
		drawDoubleArrow(v);
		String str = "" + rank;
		if (rank != -1) {
			if (this.getParent() != null && this.getParent().getLeft() == this) {
				v.drawString(str, x - LeftHeapNode.RADIUS, y
						- LeftHeapNode.RADIUS, Fonts.SMALL);
			} else {
				v.drawString(str, x + LeftHeapNode.RADIUS, y
						- LeftHeapNode.RADIUS, Fonts.SMALL);
			}
		}
	}

	public void repos(int px, int py) {
		this.goTo(px, py);

		if (this.getRight() != null) {
			this.getRight().repos(px + getRight().leftw,
					py + (LeftHeap.minsepy));// + 2 * LeftHeapNode.RADIUS));
		}
		if (this.getLeft() != null) {
			this.getLeft()
					.repos(px - getLeft().rightw, py + (LeftHeap.minsepy));// + 2 * LeftHeapNode.RADIUS));
		}
	}

	private void lowlight() {
		bgColor(new Color(200, 200 - getKey() / 10, 0));
	}

	private void highlight() {
		bgKeyColor();
	}

	public void lowlightTree() {
		lowlight();
		if (getLeft() != null) {
			getLeft().lowlightTree();
		}
		if (getRight() != null) {
			getRight().lowlightTree();
		}
	}

	public void highlightTree() {
		highlight();
		if (getLeft() != null) {
			getLeft().highlightTree();
		}
		if (getRight() != null) {
			getRight().highlightTree();
		}
	}

	@Override
	public void drawTree(View v) {

		if (this.state != INVISIBLE) {

			/*
			 * if (thread) { v.setColor(Color.red); } else {
			 */
			v.setColor(Color.black);
			// }

			if ((getLeft() != null) && (getLeft().state != INVISIBLE)) {
				if (dashedLeftLine) {
					v.drawDashedLine(x, y, getLeft().x, getLeft().y);
				} else {
					v.drawLine(x, y, getLeft().x, getLeft().y);
				}
			}
			if ((getRight() != null) && (getRight().state != INVISIBLE)) {
				if (dashedRightLine) {
					v.drawDashedLine(x, y, getRight().x, getRight().y);
				} else {
					v.drawLine(x, y, getRight().x, getRight().y);
				}
			}
		}
		if (getLeft() != null) {
			getLeft().drawTree(v);
		}
		if (getRight() != null) {
			getRight().drawTree(v);
		}
		draw(v);
	}

	@Override
	public LeftHeapNode getRight() {
		return (LeftHeapNode) super.getRight();
	}

	public void setRight(LeftHeapNode v) {
		super.setRight(v);
	}

	@Override
	public LeftHeapNode getLeft() {
		return (LeftHeapNode) super.getLeft();
	}

	void setLeft(LeftHeapNode v) {
		super.setLeft(v);
	}

	@Override
	public LeftHeapNode getParent() {
		return (LeftHeapNode) super.getParent();
	}

	public void setParent(LeftHeapNode v) {
		super.setParent(v);
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "rank", rank);
		HashtableStoreSupport.store(state, hash + "doubleArrow", doubleArrow);
		HashtableStoreSupport.store(state, hash + "dashedRightLine", dashedRightLine);
		HashtableStoreSupport.store(state, hash + "dashedLeftLine", dashedLeftLine);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object rank = state.get(hash + "rank");
		if (rank != null) this.rank = (Integer) HashtableStoreSupport.restore(rank);
		Object doubleArrow = state.get(hash + "doubleArrow");
		if (doubleArrow != null) this.doubleArrow = (Boolean) HashtableStoreSupport.restore(doubleArrow);
		Object dashedRightLine = state.get(hash + "dashedRightLine");
		if (dashedRightLine != null) this.dashedRightLine = (Boolean) HashtableStoreSupport.restore(dashedRightLine);
		Object dashedLeftLine = state.get(hash + "dashedLeftLine");
		if (dashedLeftLine != null) this.dashedLeftLine = (Boolean) HashtableStoreSupport.restore(dashedLeftLine);
	}
}
