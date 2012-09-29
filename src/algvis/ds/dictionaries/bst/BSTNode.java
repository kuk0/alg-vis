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
package algvis.ds.dictionaries.bst;

import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.NodePair;
import algvis.core.history.HashtableStoreSupport;
import algvis.ds.DataStructure;
import algvis.gui.Fonts;
import algvis.gui.view.Layout;
import algvis.gui.view.View;

import java.awt.*;
import java.util.Hashtable;
import java.util.Stack;

public class BSTNode extends Node {
	private BSTNode left = null, right = null, parent = null;
	public int leftw, rightw;
	public boolean markSubtree = false;
	private NodeColor markedSubtreeColor;

	// variables for the Reingold-Tilford layout
    private int offset = 0; // offset from parent node
	private int level; // distance to root
	private boolean thread = false; // is this node threaded?

	// statistics
	public int size = 1, height = 1, sumh = 1;

	protected BSTNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public BSTNode(DataStructure D, int key, int zDepth) {
		super(D, key, zDepth);
	}

	public BSTNode(DataStructure d, int key, int x, int y, int zDepth) {
		super(d, key, x, y, zDepth);
	}

	public BSTNode getLeft() {
		if (thread)
			return null;
		else
			return left;
	}

	public void setLeft(BSTNode left) {
		if (thread) {
			thread = false;
			right = null;
		}
		this.left = left;
	}

	public BSTNode getRight() {
		if (thread)
			return null;
		else
			return right;
	}

	public BSTNode setRight(BSTNode right) {
		if (thread) {
			thread = false;
			left = null;
		}
		this.right = right;
		return right;
	}

	public BSTNode getParent() {
		return parent;
	}

	public BSTNode setParent(BSTNode parent) {
		return this.parent = parent;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public boolean isRoot() {
		return getParent() == null;
	}

	public boolean isLeaf() {
		return getLeft() == null && getRight() == null;
	}

	public boolean isLeft() {
		return getParent() != null && getParent().getLeft() == this;
	}

	/**
	 * removes edge between this and left; removes edge between newLeft and its
	 * parent; creates new edge between this and newLeft
	 */
	public void linkLeft(BSTNode newLeft) {
		if (getLeft() != newLeft) {
			if (getLeft() != null) {
				// remove edge between this and left
				unlinkLeft();
			}
			if (newLeft != null) {
				if (newLeft.getParent() != null) {
					// remove edge between newLeft and its parent
					newLeft.unlinkParent();
				}
				// create new edge between this and newLeft
				newLeft.setParent(this);
			}
			setLeft(newLeft);
		}
	}

	/**
	 * removes edge between this and left
	 */
	public void unlinkLeft() {
		getLeft().setParent(null);
		setLeft(null);
	}

	/**
	 * removes edge between this and right; removes edge between newRight and
	 * its parent; creates new edge between this and newRight
	 */
	public void linkRight(BSTNode newRight) {
		if (getRight() != newRight) {
			if (getRight() != null) {
				// remove edge between this and right
				unlinkRight();
			}
			if (newRight != null) {
				if (newRight.getParent() != null) {
					// remove edge between newRight and its parent
					newRight.unlinkParent();
				}
				// create new edge between this and newRight
				newRight.setParent(this);
			}
			setRight(newRight);
		}
	}

	/**
	 * removes edge between this and right
	 */
	public void unlinkRight() {
		getRight().setParent(null);
		setRight(null);
	}

	private void unlinkParent() {
		if (isLeft()) {
			getParent().unlinkLeft();
		} else {
			getParent().unlinkRight();
		}
	}

	public void isolate() {
		setLeft(setRight(setParent(null)));
	}

	/**
	 * Calculate the height, size, and sum of heights of this node, assuming
	 * that this was already calculated for its children.
	 */
	public void calc() {
		int ls = 0, rs = 0, lh = 0, rh = 0, lsh = 0, rsh = 0;
		if (getLeft() != null) {
			ls = getLeft().size;
			lh = getLeft().height;
			lsh = getLeft().sumh;
		}
		if (getRight() != null) {
			rs = getRight().size;
			rh = getRight().height;
			rsh = getRight().sumh;
		}
		size = ls + rs + 1;
		height = Math.max(lh, rh) + 1;
		sumh = lsh + rsh + size;
	}

	/**
	 * Calculate the height, size, and sum of heights for all the nodes in this
	 * subtree (recursively bottom-up).
	 */
	public void calcTree() {
		if (getLeft() != null) {
			getLeft().calcTree();
		}
		if (getRight() != null) {
			getRight().calcTree();
		}
		calc();
	}

	public void setArc() {
		setArc(getParent());
	}

	private static int i;

	public void drawTree(View v) {
		i = 0;
		drawTree2(v);
	}

	private void drawTree2(View v) {
		if (D instanceof BST && ((BST) D).subtrees && markSubtree) {
			Polygon p = new Polygon();
			p.addPoint(x - 1, y - 1);
			if (D.getLayout() == Layout.SIMPLE) {
				if (height == 1) {
					p.addPoint(x - 7, y + 10);
					p.addPoint(x + 7, y + 10);
				} else {
					int x1 = x - leftw + DataStructure.minsepx / 2, x2 = x
							+ rightw - DataStructure.minsepx / 2, y1 = y
							+ DataStructure.minsepy, y2 = y + (height - 1)
							* DataStructure.minsepy;
					p.addPoint(x1, y1);
					p.addPoint(x1, y2);
					p.addPoint(x2, y2);
					p.addPoint(x2, y1);
				}
			} else {
				BSTNode u = this, w = this;
				Stack<BSTNode> tmp = new Stack<BSTNode>();
				while (u != null && w != null) {
					p.addPoint(u.x - 1, u.y);
					tmp.add(w);
					if (u.thread && w.thread)
						break;
					u = (u.left != null) ? u.left : u.right;
					w = (w.right != null) ? w.right : w.left;
				}
				while (!tmp.isEmpty()) {
					w = tmp.pop();
					p.addPoint(w.x + 1, w.y);
				}
			}
			p.addPoint(x + 1, y - 1);
			v.fillPolygon(p);
		}
		if (state != INVISIBLE && parent != null) {
			v.setColor(Color.black);
			v.drawLine(x, y, parent.x, parent.y);
		}
		if (getLeft() != null) {
			//System.out.println("kreslim lavy " + getLeft().key + " " + this.key);
			getLeft().drawTree2(v);
		}
		if (D instanceof BST && ((BST) D).order) { // && D.panel.S.layout ==
													// Layout.SIMPLE
			v.setColor(Color.LIGHT_GRAY);
			++i;
			if (i%10 == 0) {
				v.drawLine(x, y, x, -22);
			} else {
				v.drawLine(x, y, x, -20);
			}
			if (i%10 == 0) {
				v.drawString("" + i, x, -29, Fonts.NORMAL);
			} else if (i%10 == 5) {
				v.drawString("5", x, -27, Fonts.NORMAL);
			} else {
				v.drawString("" + i%10, x, -27, Fonts.SMALL);
			}
		}
		if (getRight() != null) {
			getRight().drawTree2(v);
		}
		draw(v);
	}

	public void moveTree() {
		if (getLeft() != null) {
			getLeft().moveTree();
		}
		if (getRight() != null) {
			getRight().moveTree();
		}
		move();
	}

	public void shiftTree(int dx, int dy) {
		if (getLeft() != null) {
			getLeft().shiftTree(dx, dy);
		}
		if (getRight() != null) {
			getRight().shiftTree(dx, dy);
		}
		goTo(tox + dx, toy + dy);
	}

	/**
	 * Create an (imaginary) box around the subtree rooted at this node.
	 * Calculate the width from the node to the left side (leftw) and the width
	 * from the node to the right side (rightw). Assumption: this box has
	 * already been created for both children.
	 */
    protected void rebox() {
		/*
		 * if there is a left child, leftw = width of the box enclosing the
		 * whole left subtree, i.e., leftw+rightw; otherwise the width is the
		 * node RADIUS plus some additional space called xspan
		 */
		leftw = (getLeft() == null) ? DataStructure.minsepx / 2
				: getLeft().leftw + getLeft().rightw;
		// rightw is computed analogically
		rightw = (getRight() == null) ? DataStructure.minsepx / 2
				: getRight().leftw + getRight().rightw;
	}

	/**
	 * Rebox the whole subtree calculating the widths recursively bottom-up.
	 */
	public void reboxTree() {
		if (getLeft() != null) {
			getLeft().reboxTree();
		}
		if (getRight() != null) {
			getRight().reboxTree();
		}
		rebox();
	}

	/**
	 * Calculate the coordinates of each node from the widths of boxes around
	 * them and direct the nodes to their new positions.
	 */
	private void repos() {
		if (isRoot()) {
			goToRoot();
			D.x1 = -leftw;
			D.x2 = rightw;
			D.y2 = this.toy;
		}
		if (this.toy > D.y2) {
			D.y2 = this.toy;
		}
		if (getLeft() != null) {
			getLeft().goTo(this.tox - getLeft().rightw,
					this.toy + DataStructure.minsepy);
			getLeft().repos();
		}
		if (getRight() != null) {
			getRight().goTo(this.tox + getRight().leftw,
					this.toy + DataStructure.minsepy);
			getRight().repos();
		}
	}

	public void repos(int x, int y) {
		goTo(x, y);
		if (getLeft() != null) {
			getLeft().repos(this.tox - getLeft().rightw,
					this.toy + DataStructure.minsepy);
		}
		if (getRight() != null) {
			getRight().repos(this.tox + getRight().leftw,
					this.toy + DataStructure.minsepy);
		}
		if (isRoot()) {
			D.x1 = x - leftw;
			D.x2 = x + rightw;
			D.y2 = this.toy;
		}
		if (this.toy > D.y2) {
			D.y2 = this.toy;
		}
	}

	public void reposition() {
		if (D.getLayout() == Layout.SIMPLE) { // simple layout
			reboxTree();
			repos();
		} else { // Reingold-Tilford layout
			RTThreads();
			RTPreposition();
			RTPetrification(0, 0);
			reboxTree();
		}
	}

	private void RTThreads() {
		if (thread) {
			thread = false;
			left = null;
			right = null;
		}
		if (getLeft() != null)
			left.RTThreads();
		if (getRight() != null)
			right.RTThreads();
	}

	/**
	 * Find the node at coordinates (x,y). This is used to identify the node
	 * that has been clicked by user.
	 */
	public BSTNode find(int x, int y) {
		if (inside(x, y))
			return this;
		if (getLeft() != null) {
			BSTNode tmp = getLeft().find(x, y);
			if (tmp != null)
				return tmp;
		}
		if (getRight() != null) {
			return getRight().find(x, y);
		}
		return null;
	}

	/**
	 * Set up the threads with the help of extreme nodes. A node is "extreme"
	 * when it is the leftmost/rightmost in the lowest level.
	 * 
	 * 1. work out left and right subtree 2. get extreme nodes from the left and
	 * right subtree 3. calculate the offset from parent & set a new thread if
	 * required
	 * 
	 * @return the leftmost and the rightmost node on the deepest level of the
	 *         subtree rooted at this node
	 */
	private NodePair<BSTNode> RTPreposition() {
		NodePair<BSTNode> result = new NodePair<BSTNode>();
		NodePair<BSTNode> fromLeftSubtree = null, fromRightSubtree = null;
		offset = 0;

		// 1. & 2. work out left & right subtree
		if (getLeft() != null)
			fromLeftSubtree = getLeft().RTPreposition();
		if (getRight() != null)
			fromRightSubtree = getRight().RTPreposition();
		// 3. examine this node
		if (isLeaf()) {
			if (!isRoot()) {
				offset = isLeft() ? -DataStructure.minsepx / 2
						: +DataStructure.minsepx / 2;
			}
			result.left = this;
			result.right = this;
		} else { // This is not a leaf; at least one subtree is non-empty.
			/*
			 * If one subtree is empty, it is not necessary to make a new
			 * thread. A proper offset must be set.
			 */
			if (getLeft() == null) {
				getRight().offset = DataStructure.minsepx / 2;
				result.left = fromRightSubtree.left;
				result.right = fromRightSubtree.right;
				return result;
			}

			if (getRight() == null) {
				getLeft().offset = -DataStructure.minsepx / 2;
				result.left = fromLeftSubtree.left;
				result.right = fromLeftSubtree.right;
				return result;
			}

			// Calculate offsets for the left and the right son.
			int loffset = 0; // offset of this node from the right contour of
								// the left subtree.
			int roffset = 0; // offset of this node from the left contour of the
								// right subtree.
			BSTNode L = getLeft();
			BSTNode R = getRight();
			/*
			 * First, left.offset is 0 and only right.offset accumulates. The
			 * offsets are corrected afterwards (this way is easier to
			 * generalize to m-ary trees). Note that offsets can be negative.
			 */
			getLeft().offset = 0;
			getRight().offset = 0;

			// traverse the right contour of the left subtree and the left
			// counour of the right subtree
			while ((L != null) && (R != null)) {
				/*
				 * left.offset + loffset is the horizontal distance from L to
				 * this node. Similarly, right.offset + roffset is the
				 * horizontal distance from R to this node.
				 */
				int distance = (loffset + DataStructure.minsepx - roffset);
				if (distance > 0) {
					getRight().offset += distance;
					roffset += distance;
				}
				/*
				 * When passes through thread there will be for sure incorrect
				 * offset! So Elevator calculate this new offset. In algorithm
				 * TR published by Reingold this value is already calculated.
				 */
				boolean LwasThread = L.thread, RwasThread = R.thread;
				L = (L.right != null) ? L.right : L.left;
				if (L != null) {
					loffset += L.offset;
				}
				R = (R.left != null) ? R.left : R.right;
				if (R != null) {
					roffset += R.offset;
				}

				BSTNode Elevator = null;
				if (LwasThread) {
					LwasThread = false;
					loffset = 0;
					Elevator = L;
					while (Elevator != this) {
						loffset += Elevator.offset;
						Elevator = Elevator.getParent();
					}
				}
				if (RwasThread) {
					roffset = 0;
					Elevator = R;
					while (Elevator != this) {
						roffset += Elevator.offset;
						Elevator = Elevator.getParent();
					}
				}
			}

			/*
			 * Now, distances should be 0 for left and some value for right.. So
			 * lets change it
			 */

			getRight().offset /= 2;
			getLeft().offset = -getRight().offset;

			/*
			 * General switch of making a new thread: we want to make a thread
			 * iff one pair of extremes is deeper than others. We assume that
			 * threads from subtrees are set properly.
			 */

			if ((R != null) && (L == null)) { // the right subtree is deeper
												// than the left subtree
				fromLeftSubtree.left.thread = true;
				fromLeftSubtree.left.right = R;
				result.left = fromRightSubtree.left;
				result.right = fromRightSubtree.right;
			} else if ((L != null) && (R == null)) { // the left subtree is
														// deeper than the right
														// subtree
				fromRightSubtree.right.thread = true;
				fromRightSubtree.right.left = L;
				result.left = fromLeftSubtree.left;
				result.right = fromLeftSubtree.right;
			} else if ((L == null) && (R == null)) { // both subtrees have the
														// same height
				result.left = fromLeftSubtree.left;
				result.right = fromRightSubtree.right;
			}

		}
		return result;
	}

	/**
	 * Calculate the absolute coordinates from the relative ones and dispose the
	 * threads.
	 * 
	 * @param x
	 *            real x coordinate of parent node
	 */
	private void RTPetrification(int x, int y) {
		goTo(x + offset, y);
		if (tox < D.x1) {
			D.x1 = tox;
		}
		if (tox > D.x2) {
			D.x2 = tox;
		}
		if (toy < D.y1) {
			// this case should be always false
			D.y1 = toy;
		}
		if (toy > D.y2) {
			D.y2 = toy;
		}

		if (thread) {
			// thread = false;
			// setLeft(null);
			// setRight(null);
		}
		if (getLeft() != null) {
			getLeft().RTPetrification(tox, y + DataStructure.minsepy);
		}
		if (getRight() != null) {
			getRight().RTPetrification(tox, y + DataStructure.minsepy);
		}
	}

	/**
	 * Set color to this subtree.
	 * @param color
	 */
	public void subtreeColor(NodeColor color) {
		setColor(color);
		if (getLeft() != null)
			getLeft().subtreeColor(color);
		if (getRight() != null)
			getRight().subtreeColor(color);
	}

	/**
	 * Similar to subtreeColor(NodeColor color), but subtree has this color only if this subtree is 
	 * marked. Set markSubtree property to mark this subtree.
	 * @param color
	 */
	public void markedSubtreeColor(NodeColor color) {
		markedSubtreeColor = color;
		if (getLeft() != null)
			getLeft().markedSubtreeColor(color);
		if (getRight() != null)
			getRight().markedSubtreeColor(color);
	}

	@Override
	protected Color getBgColor() {
		if (D instanceof BST && ((BST) D).subtrees && markedSubtreeColor != null) {
			return markedSubtreeColor.bgColor;
		} else {
			return super.getBgColor();
		}
	}

	@Override
	protected Color getFgColor() {
		if (D instanceof BST && ((BST) D).subtrees && markedSubtreeColor != null) {
			return markedSubtreeColor.fgColor;
		} else {
			return super.getFgColor();
		}
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "left", left);
		HashtableStoreSupport.store(state, hash + "right", right);
		HashtableStoreSupport.store(state, hash + "parent", parent);
		HashtableStoreSupport.store(state, hash + "level", level);
		HashtableStoreSupport.store(state, hash + "thread", thread);
		HashtableStoreSupport.store(state, hash + "leftw", leftw);
		HashtableStoreSupport.store(state, hash + "rightw", rightw);
		HashtableStoreSupport.store(state, hash + "markSubtree", markSubtree);
		HashtableStoreSupport.store(state, hash + "markedSubtreeColor", markedSubtreeColor);
		if (left != null) left.storeState(state);
		if (right != null) right.storeState(state);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object left = state.get(hash + "left");
		if (left != null) this.left = (BSTNode) HashtableStoreSupport.restore(left);
		Object right = state.get(hash + "right");
		if (right != null) this.right = (BSTNode) HashtableStoreSupport.restore(right);
		Object parent = state.get(hash + "parent");
		if (parent != null) this.parent = (BSTNode) HashtableStoreSupport.restore(parent);
		Object level = state.get(hash + "level");
		if (level != null) this.level = (Integer) HashtableStoreSupport.restore(level);
		Object thread = state.get(hash + "thread");
		if (thread != null) this.thread = (Boolean) HashtableStoreSupport.restore(thread);
		Object leftw = state.get(hash + "leftw");
		if (leftw != null) this.leftw = (Integer) HashtableStoreSupport.restore(leftw);
		Object rightw = state.get(hash + "rightw");
		if (rightw != null) this.rightw = (Integer) HashtableStoreSupport.restore(rightw);
		Object markSubtree = state.get(hash + "markSubtree");
		if (markSubtree != null) this.markSubtree = (Boolean) HashtableStoreSupport.restore(markSubtree);
		Object markedSubtreeColor = state.get(hash + "markedSubtreeColor");
		if (markedSubtreeColor != null) this.markedSubtreeColor = (NodeColor) HashtableStoreSupport.restore
				(markedSubtreeColor);
		
		if (this.left != null) this.left.restoreState(state);
		if (this.right != null) this.right.restoreState(state);
	}
}
