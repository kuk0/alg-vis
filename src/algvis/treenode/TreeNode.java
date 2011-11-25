package algvis.treenode;

import java.awt.Color;
import java.util.Stack;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.NodePair;
import algvis.core.View;

public class TreeNode extends Node {

	public TreeNode child = null, right = null, parent = null;

	// variables for TR
	public int offset = 0; // offset from base line, base line has x-coord
							// equaled to x-coord of leftmost child
	public int level; // "height" from root
	public boolean thread = false; // is this node threaded?
	public int toExtremeSon = 0; // offset from leftest son
	public int toBaseline = 0; // distance to child's baseline
	public int modifier = 0;
	public int fakex = 0, fakey = 0; // temporary coordinates of the node
	public int number = 1;

	public int change = 0, shift = 0; // for evenly spaced smaller subtrees
	public TreeNode ancestor = this; // unused variable for now

	// statistics
	public int size = 1, height = 1;
	public int nos = 0; // number of sons, probably useless

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
		return (child == null) || (thread);
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
			TreeNode T = child;
			while (T != null) {
				size += T.size;
				if (height <= T.height) {
					height = T.height + 1;
				}
				T = T.right;
			}
		}
	}

	/**
	 * Calculate height and size of subtree rooted by "this" node bottom-up
	 */
	public void calcTree() {
		if (!isLeaf()) {
			TreeNode T = child;
			while (T != null) {
				T.calcTree();
				T = T.right;
			}
		}
		calc();
	}

	public void setArc() {
		setArc(parent);
	}

	/**
	 * Draw edges, then the node itself. Don't draw invisible nodes and edges
	 * from and to them
	 */
	public void drawTree(View v) {
		if (state != INVISIBLE) {
			if (thread) {
				v.setColor(Color.red);
				v.drawLine(x, y, child.x, child.y);
				v.setColor(Color.black);
			} else {
				TreeNode T = child;
				while (T != null) {
					v.setColor(Color.black);
					v.drawLine(x, y, T.x, T.y);
					T.drawTree(v);
					T = T.right;
				}
			}
		}
		draw(v);
	}

	public void moveTree() {
		TreeNode T = child;
		while (T != null) {
			T.moveTree();
			T = T.right;
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
		TreeNode T = child;
		TreeNode tmp = null;
		while ((T != null) && (tmp == null)) {
			tmp = T.find(x, y);
			T = T.right;
		}
		return tmp;
	}

	public void addRight(TreeNode T) {
		if (right == null) {
			right = T;
			T.parent = parent;
		} else {
			right.addRight(T);
		}
	}

	public void addChild(TreeNode T) {
		if (child == null) {
			child = T;
			T.parent = this;
			T.D = this.D;
		} else {
			child.addRight(T);
		}
	}

	public TreeNode leftmostChild() {
		return child;
	}

	public TreeNode rightmostChild() {
		TreeNode T = child;
		if (T != null) {
			while (T.right != null) {
				T = T.right;
			}
		}
		return T;
	}

	public void append(int x, int j) {
		if (key == x) {
			addChild(new TreeNode(D, j));
		} else {
			TreeNode T = child;
			while (T != null) {
				T.append(x, j);
				T = T.right;
			}
		}
	}

	public void reposition() {
		fTRInitialization(0);
		fTRPrePosition();
		fTRDisposeThreads();
		fTRPetrification(0);
		fTRBounding(-fakex);
		System.out.print("\n");
	}

	/**
	 * First traverse of tree in repositioning process. Set some basic
	 * variables.
	 * 
	 * @param level
	 *            current level in tree
	 */
	private void fTRInitialization(int level) {
		// System.out.print(level);
		this.level = level;
		offset = modifier = shift = change = 0;
		toExtremeSon = 0;
		toBaseline = 0;
		TreeNode T = child;
		level++;
		int noofchild = 1;
		while (T != null) {
			T.fTRInitialization(level);
			T.number = noofchild;
			T = T.right;
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

			//
			while ((L != null) && (R != null)) {
				int distance = (roffset - loffset);
				if (distance < D.minsepx) {
					RightSubtree.offset += (D.minsepx - distance);
					roffset += (D.minsepx - distance);
				}
				/*
				 * I think this (Elevator) part causes O(n^2) (un)efficiency
				 */
				if (!L.thread) {
					loffset += L.toExtremeSon;
				} else {
					loffset = 0;

					System.out.print(L.key + "->" + L.child.key + ": ");
					TreeNode Elevator = L.child;
					int cursep = 0;
					while (Elevator.parent != LeftSubtree.parent) {
						System.out.print(cursep + ", ");
						cursep += Elevator.offset
								- Elevator.parent.leftmostChild().offset
								- Elevator.parent.toExtremeSon;
						Elevator = Elevator.parent;
					}

					loffset = Elevator.offset + cursep;
					System.out.print(loffset + "\n");
				}

				if (!R.thread) {
					roffset -= R.toExtremeSon;
				} else {
					roffset = 0;

					System.out.print(R.key + "->" + R.child.key + ": ");
					TreeNode Elevator = R.child;
					int cursep = 0;
					do {
						System.out.print(cursep + ", ");
						cursep += Elevator.offset
								- Elevator.parent.leftmostChild().offset
								- Elevator.parent.toExtremeSon;
						Elevator = Elevator.parent;
					} while (Elevator != RightSubtree);

					roffset = RightSubtree.offset + cursep;
					System.out.print(roffset + "\n");
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
	
		TreeNode T = child;
		while (T != null) {
			T.fTRDisposeThreads();
			T = T.right;
		}
	}

	/**
	 * Changes relative coordinates to absolute. Spacing smaller subtrees work
	 * with absolute coordinates.
	 * 
	 * @param baseline
	 *            x-coordinate of the baseline
	 */
	private void fTRPetrification(int baseline) {
		fakex = baseline + offset;
		fakey = level * (D.minsepy);

		if (isLeaf()) {
			return;
		}
		TreeNode T = child;
		while (T != null) {
			T.fTRPetrification(fakex - toBaseline);
			T = T.right;
		}
	}

	/**
	 * This will be overwritten.
	 */
	@SuppressWarnings("unused")
	private void fTRLeafGathering() {
		if (isLeaf()) {
			return;
		}

		TreeNode T = child;
		while (T != null) {
			T.fTRLeafGathering();
			T = T.right;
		}

		/*
		 * Traverse from right to left
		 */
		// allmighty references! :-/
		T = child;
		Stack<TreeNode> stack = new Stack<TreeNode>();
		while (T != null) {
			NodePair<TreeNode> N = new NodePair<TreeNode>();
			N.left = T;
			stack.push(T);
			T = T.right;
		}

		TreeNode U = stack.pop();
		while (!stack.empty()) {
			T = stack.pop();
			// System.out.print(T.key+ ", " + U.key+ " ");
			if (T.isLeaf()) {
				T.fakex = U.fakex - D.minsepx;
			}
			U = T;
		}
		// uff.. it's working correctly
		// but something does not :-/

		/*
		 * Traverse from left to right
		 */
		T = child.right;
		U = child;
		while (T != null) {
			if (T.isLeaf()) {
				T.fakex = U.fakex + D.minsepx;
			}

			U = U.right;
			T = T.right;
		}
	}

	/**
	 * this will be overwritten
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private NodePair<TreeNode> fTRCorrection() {
		NodePair<TreeNode> result = new NodePair<TreeNode>();
		return result;
	}

	/**
	 * this will be overwritten
	 * 
	 * @param modsum
	 */
	@SuppressWarnings("unused")
	private void fTRCorrection2(int modsum) {
		modsum += modifier;

		if (isLeaf()) {
			return;
		}
		TreeNode T = child;
		while (T != null) {
			T.fTRCorrection2(modsum);
			T = T.right;
		}

	}

	/**
	 * Final step of layout algorithm. Changes temporary coordinates to real
	 * one. And computes boundary of a tree.
	 * 
	 * @param correction
	 * 		Useful when you want make root rooted at [0,0]
	 */
	private void fTRBounding(int correction) {
		fakex += correction;
		tox = fakex;
		toy = fakey;
		this.fTRGetInfo(3, toExtremeSon);

		if (tox < D.x1) {
			D.x1 = tox;
		}
		if (tox > D.x2) {
			D.x2 = tox;
		}
		// this case should be always false
		if (toy < D.y1) {
			D.y1 = toy;
		}
		if (toy > D.y2) {
			D.y2 = toy;
		}
		this.goTo(tox, toy);

		if (isLeaf()) {
			return;
		}
		TreeNode T = child;
		while (T != null) {
			T.fTRBounding(correction);
			T = T.right;
		}
	}

	@SuppressWarnings("unused")
	private void fTRGetInfo(int phase) {
		System.out.print("Node: " + key + " fakex: " + fakex + " mod: "
				+ modifier + " change: " + change + " shift: " + shift + " ("
				+ phase + ")" + "\n");
	}

	private void fTRGetInfo(int phase, int variable) {
		System.out
				.print("Node: " + key + " fakex: " + fakex + " mod: "
						+ modifier + " change: " + change + " shift: " + shift
						+ " offset: " + offset + " toE: " + toExtremeSon
						+ " toB: " + toBaseline + " thread: " + thread + " ("
						+ phase + ")" + "\n");
	}

}