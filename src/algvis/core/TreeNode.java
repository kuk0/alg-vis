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
package algvis.core;

import java.awt.Color;
import java.util.Stack;

public class TreeNode extends Node {
	private TreeNode child = null, right = null, parent = null;

	// variables for the Reingold-Tilford-Walker layout
	int offset = 0; // offset from base line, base line has x-coord
					// equaled to x-coord of leftmost child
	int level; // distance from the root
	protected boolean thread = false; // is this node threaded?
	int toExtremeSon = 0; // offset from the leftmost son
	int toBaseline = 0; // distance to child's baseline
	int modifier = 0;
	int tmpx = 0, tmpy = 0; // temporary coordinates of the node
	int number = 1;

	int change = 0, shift = 0; // for evenly spaced smaller subtrees
	// TreeNode ancestor = this; // unused variable for now

	// statistics
	public int size = 1, height = 1;
	public int nos = 0; // number of sons, probably useless

	// from binary node
	public int leftw, rightw;

	public TreeNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public TreeNode(DataStructure D, int key) {
		super(D, key);
	}

	public boolean isRoot() {
		return getParent() == null;
	}

	public boolean isLeaf() {
		return getChild() == null;
	}

	// calc() and calcTree() methods analogous to BSTNode ones
	/**
	 * Calculate height and size of "this" node assuming these were calculated
	 * (properly) in its children.
	 */
	public void calc() {
		size = 1;
		height = 1;
		if (!isLeaf()) {
			TreeNode w = getChild();
			while (w != null) {
				size += w.size;
				if (height <= w.height) {
					height = w.height + 1;
				}
				w = w.getRight();
			}
		}
	}

	/**
	 * Calculate height and size of subtree rooted by "this" node bottom-up
	 */
	public void calcTree() {
		if (!isLeaf()) {
			TreeNode w = getChild();
			while (w != null) {
				w.calcTree();
				w = w.getRight();
			}
		}
		calc();
	}

	public void setArc() {
		setArc(getParent());
	}

	public void drawEdges(View v) {
		if (state != INVISIBLE) {
			if (thread) {
				v.setColor(Color.red);
				if (getChild() != null) {
					v.drawLine(x, y, getChild().x, getChild().y);
				} else
					System.out.println("child: " + getChild());
				v.setColor(Color.black);
			} else {
				TreeNode w = getChild();
				while (w != null) {
					v.setColor(Color.black);
					v.drawLine(x, y, w.x, w.y);
					w.drawEdges(v);
					w = w.getRight();
				}
			}
		}
	}

	public void drawVertices(View v) {
		TreeNode w = getChild();
		while (w != null) {
			w.drawVertices(v);
			w = w.getRight();
		}
		draw(v);
	}

	/**
	 * Draw edges, then the node itself. Don't draw invisible nodes and edges
	 * from and to them
	 */
	public void drawTree(View v) {
		drawEdges(v);
		drawVertices(v);
	}

	public void moveTree() {
		TreeNode w = getChild();
		while (w != null) {
			w.moveTree();
			w = w.getRight();
		}
		move();
	}

	/**
	 * Find the node at coordinates (x,y). This is used to identify the node
	 * that has been clicked by user.
	 */
	public TreeNode find(int x, int y) {
		if (inside(x, y))
			return this;
		TreeNode w = getChild();
		TreeNode res = null;
		while ((w != null) && (res == null)) {
			res = w.find(x, y);
			w = w.getRight();
		}
		return res;
	}

	public void rebox() {
	}

	/**
	 * Rebox the whole subtree calculating the widths recursively bottom-up.
	 */
	public void reboxTree() {
		int bw = DataStructure.minsepx / 2;
		int le = 9999999; // keeps current extreme leftw value
		int re = -9999999;
		TreeNode T = getChild();
		while (T != null) {
			T.reboxTree();

			int lxe = (T.tox - tox) - T.leftw;
			if (lxe < le) {
				le = lxe;
			}
			int rxe = (T.tox - tox) + T.rightw;
			if (rxe > re) {
				re = rxe;
			}
			T = T.getRight();
		}
		if (le > -bw) {
			le = -bw;
		}
		if (re < bw) {
			re = bw;
		}
		leftw = -le;
		rightw = re;
	}

	public void addRight(TreeNode w) {
		if (getRight() == null) {
			setRight(w);
			w.setParent(parent);
		} else {
			getRight().addRight(w);
		}
	}

	public void addChild(TreeNode w) {
		if (getChild() == null) {
			setChild(w);
			w.setParent(this);
			w.D = this.D;
		} else {
			getChild().addRight(w);
		}
	}

	public void deleteChild(TreeNode w) {
		if (w == getChild()) {
			setChild(getChild().getRight());
			w.setRight(null);
		} else {
			TreeNode v = getChild();
			while ((v != null) && (v.getRight() != w)) {
				v = v.getRight();
			}
			if (v != null) {
				v.setRight(w.getRight());
			}
			w.setRight(null);
		}
	}

	public TreeNode leftmostChild() {
		return getChild();
	}

	public TreeNode rightmostChild() {
		TreeNode w = getChild();
		if (w != null) {
			while (w.getRight() != null) {
				w = w.getRight();
			}
		}
		return w;
	}

	public void append(int x, int j) {
		if (key == x) {
			addChild(new TreeNode(D, j));
		} else {
			TreeNode w = getChild();
			while (w != null) {
				w.append(x, j);
				w = w.getRight();
			}
		}
	}

	public void reposition() {
		fTRInitialization(0);
		fTRPrePosition();
		fTRDisposeThreads();
		fTRPetrification(0);
		fTRBounding(-tmpx);
		reboxTree();
		// System.out.println(key+" "+leftw+" "+rightw);
		/*D.x1 -= D.minsepx;
		D.x2 += D.xspan + D.radius;
		D.y1 -= D.yspan + D.radius;
		D.y2 += D.yspan + D.radius;*/
		// System.out.println(D.x1 + " " + leftw + " " + D.x2 + " " + rightw);
	}

	/**
	 * First traverse of tree in repositioning process. Set some basic
	 * variables.
	 * 
	 * @param level
	 *            current level in tree
	 */
	private void fTRInitialization(int level) {
		// System.out.println(level);
		this.level = level;
		offset = modifier = shift = change = 0;
		toExtremeSon = 0;
		toBaseline = 0;
		leftw = rightw = 0;
		TreeNode w = getChild();
		level++;
		int noofchild = 1;
		while (w != null) {
			w.fTRInitialization(level);
			w.number = noofchild;
			w = w.getRight();
			noofchild++;
		}
	}

	/**
	 * The core of algorithm. Computes relative coordinates, uses threads to
	 * keep subtrees/subforests bounded and easy-track for spacing.
	 * 
	 * @return Leftmost and rightmost nodes on the last level of subtree
	 */
	private NodePair<TreeNode> fTRPrePosition() {
		NodePair<TreeNode> result = new NodePair<TreeNode>();

		if (isLeaf()) {
			offset = 0;
			result.left = this;
			result.right = this;
			return result;
		}

		TreeNode LeftSubtree = getChild();
		TreeNode RightSubtree = getChild().getRight();

		/*
		 * So lets get result from first child
		 */
		NodePair<TreeNode> fromLeftSubtree = LeftSubtree.fTRPrePosition();
		result = fromLeftSubtree;

		/*
		 * Spacing right subtree with left subforest. It will pre-compute shifts
		 * for distributing smaller subtrees.
		 */
		while (RightSubtree != null) {
			NodePair<TreeNode> fromRightSubtree = RightSubtree.fTRPrePosition();

			TreeNode L = LeftSubtree;
			TreeNode R = RightSubtree;
			int loffset = LeftSubtree.offset;
			int roffset = RightSubtree.offset = LeftSubtree.offset;

			while ((L != null) && (R != null)) {
				int distance = (loffset + DataStructure.minsepx - roffset);
				if (distance > 0) {
					RightSubtree.offset += distance;
					roffset += distance;

					/*
					 * set spacing for smaller subtrees
					 */
					TreeNode Elevator = L;
					while (Elevator.getParent() != LeftSubtree.getParent()) {
						Elevator = Elevator.getParent();
					}
					int theta = RightSubtree.number - Elevator.number;
					RightSubtree.change -= distance / theta;
					RightSubtree.shift += distance;
					Elevator.change += distance / theta;
				}
				/*
				 * I think this (Elevator) part causes O(n^2) (un)efficiency
				 */
				if (!L.thread) {
					loffset += L.toExtremeSon;
				} else {
					loffset = 0;
					TreeNode Elevator = L.getChild();
					int cursep = 0;
					while (Elevator.getParent() != LeftSubtree.getParent()) {
						cursep += Elevator.offset
								- Elevator.getParent().leftmostChild().offset
								- Elevator.getParent().toExtremeSon;
						Elevator = Elevator.getParent();
					}

					loffset = Elevator.offset + cursep;
				}

				if (!R.thread) {
					roffset -= R.toExtremeSon;
				} else {
					roffset = 0;
					TreeNode Elevator = R.getChild();
					int cursep = 0;
					do {
						cursep += Elevator.offset
								- Elevator.getParent().leftmostChild().offset
								- Elevator.getParent().toExtremeSon;
						Elevator = Elevator.getParent();
					} while (Elevator != RightSubtree);

					roffset = RightSubtree.offset + cursep;
				}

				L = L.rightmostChild();
				R = R.leftmostChild();
			}

			/*
			 * L & R pointers are set right for making thread easy
			 */
			// both left subforest and right subtree have the same height
			if ((L == null) && (R == null)) {
				fromLeftSubtree.left = fromLeftSubtree.left;
				fromLeftSubtree.right = fromRightSubtree.right;
				// left subforest is more shallow
			} else if ((L == null) && (R != null)) {
				fromLeftSubtree.left.thread = true;
				fromLeftSubtree.left.setChild(R);

				fromLeftSubtree.left = fromRightSubtree.left;
				fromLeftSubtree.right = fromRightSubtree.right;
				// right subtree is more shallow
			} else if ((L != null) && (R == null)) {
				fromRightSubtree.right.thread = true;
				fromRightSubtree.right.setChild(L);
			}

			LeftSubtree = LeftSubtree.getRight();
			RightSubtree = RightSubtree.getRight();
		}

		/*
		 * spaces smaller subtrees: a traverse of children from right to left
		 */
		int distance = 0;
		int change = 0;
		TreeNode w = getChild();
		Stack<TreeNode> stack = new Stack<TreeNode>();
		while (w != null) {
			NodePair<TreeNode> N = new NodePair<TreeNode>();
			N.left = w;
			stack.push(w);
			w = w.getRight();
		}

		while (!stack.empty()) {
			w = stack.pop();
			w.offset += distance;
			change += w.change;
			distance += w.shift + change;
		}

		/*
		 * finally, set proper offset to self
		 */
		toExtremeSon = (rightmostChild().offset - leftmostChild().offset) / 2;
		toBaseline = leftmostChild().offset + toExtremeSon;
		offset += toExtremeSon;

		return fromLeftSubtree;
	}

	/**
	 * Disposes threads. Useful as stand-alone only for testing.
	 */
	public void fTRDisposeThreads() {
		if (thread) {
			thread = false;
			setChild(null);
		}

		TreeNode w = getChild();
		while (w != null) {
			w.fTRDisposeThreads();
			w = w.getRight();
		}
	}

	/**
	 * Changes relative coordinates to absolute. Spacing smaller subtrees works
	 * with absolute coordinates.
	 * 
	 * @param baseline
	 *            x-coordinate of the baseline
	 */
	private void fTRPetrification(int baseline) {
		tmpx = baseline + offset;
		tmpy = level * (DataStructure.minsepy);

		TreeNode w = getChild();
		while (w != null) {
			w.fTRPetrification(tmpx - toBaseline);
			w = w.getRight();
		}
	}

	/**
	 * Final step of layout algorithm. Changes temporary coordinates to real
	 * one. And computes boundary of a tree.
	 * 
	 * @param correction
	 *            Useful when you want make root rooted at [0,0]
	 */
	private void fTRBounding(int correction) {
		goTo(tmpx + correction, tmpy);

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

		TreeNode w = getChild();
		while (w != null) {
			w.fTRBounding(correction);
			w = w.getRight();
		}
	}

	/**
	 * Beware! This method doesn't change bounds of the data structure!
	 * 
	 * @param xamount
	 *            amount of shift in x-axis
	 * @param yamount
	 *            amount of shift in y-axis
	 */
	public void shift(int xamount, int yamount) {
		goTo(tox + xamount, toy + yamount);
		// System.out.println(tox);
		TreeNode w = getChild();
		while (w != null) {
			w.shift(xamount, yamount);
			w = w.getRight();
		}
	}

	public TreeNode getChild() {
		return child;
	}

	public void setChild(TreeNode child) {
		this.child = child;
	}

	public TreeNode getRight() {
		return right;
	}

	public void setRight(TreeNode right) {
		this.right = right;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	// private void fTRGetInfo(int phase, int variable) {
	// System.out
	// .println("Node: " + key + " fakex: " + fakex + " mod: "
	// + modifier + " change: " + change + " shift: " + shift
	// + " offset: " + offset + " toE: " + toExtremeSon
	// + " toB: " + toBaseline + " thread: " + thread + " ("
	// + phase + ")");
	// }

}
