package algvis.core;

import java.awt.Color;
import java.util.Stack;

public class TreeNode extends Node {
	public TreeNode child = null, right = null, parent = null;

	// variables for the Reingold-Tilford-Walker layout
	int offset = 0; // offset from base line, base line has x-coord
					// equaled to x-coord of leftmost child
	int level; // distance from the root
	boolean thread = false; // is this node threaded?
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
		return parent == null;
	}

	public boolean isLeaf() {
		return child == null;
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
			TreeNode w = child;
			while (w != null) {
				size += w.size;
				if (height <= w.height) {
					height = w.height + 1;
				}
				w = w.right;
			}
		}
	}

	/**
	 * Calculate height and size of subtree rooted by "this" node bottom-up
	 */
	public void calcTree() {
		if (!isLeaf()) {
			TreeNode w = child;
			while (w != null) {
				w.calcTree();
				w = w.right;
			}
		}
		calc();
	}

	public void setArc() {
		setArc(parent);
	}

	public void drawEdges(View v) {
		if (state != INVISIBLE) {
			if (thread) {
				v.setColor(Color.red);
				v.drawLine(x, y, child.x, child.y);
				v.setColor(Color.black);
			} else {
				TreeNode w = child;
				while (w != null) {
					v.setColor(Color.black);
					v.drawLine(x, y, w.x, w.y);
					w.drawEdges(v);
					w = w.right;
				}
			}
		}
	}

	public void drawVertices(View v) {
		TreeNode w = child;
		while (w != null) {
			w.drawVertices(v);
			w = w.right;
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
		TreeNode w = child;
		while (w != null) {
			w.moveTree();
			w = w.right;
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
		TreeNode w = child;
		TreeNode res = null;
		while ((w != null) && (res == null)) {
			res = w.find(x, y);
			w = w.right;
		}
		return res;
	}

	public void rebox() {
	}

	/**
	 * Rebox the whole subtree calculating the widths recursively bottom-up.
	 */
	public void reboxTree() {
		int bw = D.xspan / 2 + D.radius;
		int le = 9999999; // keeps current extreme leftw value
		int re = -9999999;
		TreeNode T = child;
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
			T = T.right;
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
		if (right == null) {
			right = w;
			w.parent = parent;
		} else {
			right.addRight(w);
		}
	}

	public void addChild(TreeNode w) {
		if (child == null) {
			child = w;
			w.parent = this;
			w.D = this.D;
		} else {
			child.addRight(w);
		}
	}

	public void deleteChild(TreeNode w) {
		if (w == child) {
			child = child.right;
			w.right = null;
		} else {
			TreeNode v = child;
			while ((v != null) && (v.right != w)) {
				v = v.right;
			}
			if (v != null) {
				v.right = w.right;
			}
			w.right = null;
		}
	}

	public TreeNode leftmostChild() {
		return child;
	}

	public TreeNode rightmostChild() {
		TreeNode w = child;
		if (w != null) {
			while (w.right != null) {
				w = w.right;
			}
		}
		return w;
	}

	public void append(int x, int j) {
		if (key == x) {
			addChild(new TreeNode(D, j));
		} else {
			TreeNode w = child;
			while (w != null) {
				w.append(x, j);
				w = w.right;
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
		D.x1 -= D.xspan + D.radius;
		D.x2 += D.xspan + D.radius;
		D.y1 -= D.yspan + D.radius;
		D.y2 += D.yspan + D.radius;
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
		TreeNode w = child;
		level++;
		int noofchild = 1;
		while (w != null) {
			w.fTRInitialization(level);
			w.number = noofchild;
			w = w.right;
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

		TreeNode LeftSubtree = child;
		TreeNode RightSubtree = child.right;

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
				int distance = (loffset + D.minsepx - roffset);
				if (distance > 0) {
					RightSubtree.offset += distance;
					roffset += distance;

					/*
					 * set spacing for smaller subtrees
					 */
					TreeNode Elevator = L;
					while (Elevator.parent != LeftSubtree.parent) {
						Elevator = Elevator.parent;
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
					TreeNode Elevator = L.child;
					int cursep = 0;
					while (Elevator.parent != LeftSubtree.parent) {
						cursep += Elevator.offset
								- Elevator.parent.leftmostChild().offset
								- Elevator.parent.toExtremeSon;
						Elevator = Elevator.parent;
					}

					loffset = Elevator.offset + cursep;
				}

				if (!R.thread) {
					roffset -= R.toExtremeSon;
				} else {
					roffset = 0;
					TreeNode Elevator = R.child;
					int cursep = 0;
					do {
						cursep += Elevator.offset
								- Elevator.parent.leftmostChild().offset
								- Elevator.parent.toExtremeSon;
						Elevator = Elevator.parent;
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
				fromLeftSubtree.left.child = R;

				fromLeftSubtree.left = fromRightSubtree.left;
				fromLeftSubtree.right = fromRightSubtree.right;
				// right subtree is more shallow
			} else if ((L != null) && (R == null)) {
				fromRightSubtree.right.thread = true;
				fromRightSubtree.right.child = L;
			}

			LeftSubtree = LeftSubtree.right;
			RightSubtree = RightSubtree.right;
		}

		/*
		 * spaces smaller subtrees: a traverse of children from right to left
		 */
		int distance = 0;
		int change = 0;
		TreeNode w = child;
		Stack<TreeNode> stack = new Stack<TreeNode>();
		while (w != null) {
			NodePair<TreeNode> N = new NodePair<TreeNode>();
			N.left = w;
			stack.push(w);
			w = w.right;
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
			child = null;
		}

		TreeNode w = child;
		while (w != null) {
			w.fTRDisposeThreads();
			w = w.right;
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
		tmpy = level * (D.minsepy);

		TreeNode w = child;
		while (w != null) {
			w.fTRPetrification(tmpx - toBaseline);
			w = w.right;
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

		TreeNode w = child;
		while (w != null) {
			w.fTRBounding(correction);
			w = w.right;
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
		TreeNode w = child;
		while (w != null) {
			w.shift(xamount, yamount);
			w = w.right;
		}
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
