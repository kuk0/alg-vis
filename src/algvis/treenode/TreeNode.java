package algvis.treenode;

import java.awt.Color;
import java.util.Stack;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.NodePair;
import algvis.core.View;

public class TreeNode extends Node {

	public TreeNode child = null, right = null, parent = null;

	// variables for TR, probably there will be some changes to offset variables
	public int offset = 0; // offset from base line, base line has x-coord
							// equaled to x-coord of leftmost child
	public int level; // "height" from root
	public boolean thread = false; // is this node threaded?
	public int toExtremeSon = 0; // offset from leftest son
	public int modifier = 0;
	public int fakex = 0, fakey = 0; // temporary coordinates of the node

	// another variables for fAL
	public int prelim = 0;
	// public int sigma = 0;
	public int change = 0;
	public int shift = 0;
	public TreeNode ancestor = this;

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
		this.size = 1;
		this.height = 1;
		if (!isLeaf()) {
			TreeNode T = child;
			while (T != null) {
				this.size += T.size;
				if (this.height <= T.height) {
					this.height = T.height + 1;
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
		if (this.state != INVISIBLE) {
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
		TreeNode T = this.child;
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
			addChild(new TreeNode(this.D, j));
		} else {
			TreeNode T = child;
			while (T != null) {
				T.append(x, j);
				T = T.right;
			}
		}
	}

	public void reposition() {
		fTRDisposeThreads();
		fTRInitialization(0);
		fTRPrePosition();
		fTRPetrification(0);
		fTRCentering();
//		fTRDisposeThreads();
//		fTRCorrection();
//		fTRCorrection2(0);
		fTRBounding(-fakex);
		System.out.print("\n");
	}

	/**
	 * First traverse of tree in repositioning process
	 * 
	 * Sets a proper level to self and sons
	 * 
	 * @param level
	 *            current level in tree
	 */
	private void fTRInitialization(int level) {
		// System.out.print(level);
		this.level = level;
		this.offset = 0;
		this.modifier = 0;
		this.toExtremeSon = 0;
		TreeNode T = child;
		level++;
		while (T != null) {
			T.fTRInitialization(level);
			T = T.right;
		}
	}

	private NodePair<TreeNode> fTRPrePosition() {
		/*
		 * Notice that result contains leftmost and rightmost deepest node in
		 * form result.child for left and result.right for right
		 */
		NodePair<TreeNode> result = new NodePair<TreeNode>();

		if (isLeaf()) {
			offset = 0;
			result.left = this;
			result.right = this;
			return result;
		}

		TreeNode LeftSubtree = this.child;
		TreeNode RightSubtree = this.child.right;

		/*
		 * So lets get result from first child
		 */

		NodePair<TreeNode> fromLeftSubtree = LeftSubtree.fTRPrePosition();
		result = fromLeftSubtree;

		/*
		 * let's the fun begin
		 */
		while (RightSubtree != null) {
			NodePair<TreeNode> fromRightSubtree = RightSubtree.fTRPrePosition();

			TreeNode L = LeftSubtree;
			TreeNode R = RightSubtree;
			int loffset = L.offset;
			int roffset = RightSubtree.offset = L.offset;

			//
			while ((L != null) && (R != null)) {
				/*
				 * LeftSubtree.offset + loffset is a distance from L pointer to
				 * "this" node. similar - RightSubtree.offset + roffset is a
				 * distance from R pointer to "this" node
				 */
				int distance = (roffset - loffset);
				// System.out.print("Distance at L: " + L.key + " R: " +
				// R.key
				// + " is " + distance + ".\n");
				// System.out.print("RightSubtree.offset: " +
				// RightSubtree.offset +
				// " roffset: "
				// + roffset + " LeftSubtree.offset: " + LeftSubtree.offset
				// + " loffset: " + loffset + "\n");
				if (distance < D.minsepx) {
					RightSubtree.offset += (D.minsepx - distance);
					roffset += (D.minsepx - distance);
				}
				// (Goin') To da floooooor!
				// System.out.print("New distance at L: " + L.key + " R: " +
				// R.key
				// + " is " + distance + ".\n");
				// System.out.print("RightSubtree.offset: " +
				// RightSubtree.offset +
				// " roffset: "
				// + roffset + " LeftSubtree.offset: " + LeftSubtree.offset
				// + " loffset: " + loffset + "\n");
				/*
				 * But watch out! When you pass through thread there could be
				 * and there will be for sure incorrect offset! And what if
				 * there is another threaded node?
				 */
				boolean LwasThread = L.thread;
				TreeNode LNext = L.rightmostChild();
				if (LNext != null) {
					loffset += LNext.offset;
				}
				L = LNext;

				TreeNode RNext = R.leftmostChild();
				if (RNext != null) {
					roffset += RNext.offset;
				}
				R = RNext;

				// note that Elevator on R always finish in RightSubtree
				TreeNode Elevator = null;
				if (LwasThread) {
					LwasThread = false;
					loffset = 0;
					Elevator = L;
					// I am not very sure about references.. :-/
					while (Elevator.parent != this.parent) {
						loffset += Elevator.offset;
						Elevator = Elevator.parent;
					}
				}
			}

			/*
			 * Guess what?! L & R pointers are set right there where we want it.
			 * :)
			 */
			// both left (conjucture of) subtree(s) and right subtree have
			// the same height
			if ((L == null) && (R == null)) {
				fromLeftSubtree.left = fromLeftSubtree.left;
				fromLeftSubtree.right = fromRightSubtree.right;
				// left (conjucture of) subtree(s) is more shallow
			} else if ((L == null) && (R != null)) {
				fromLeftSubtree.left.thread = true;
				fromLeftSubtree.left.child = R;

				fromLeftSubtree.left = fromRightSubtree.left;
				fromLeftSubtree.right = fromRightSubtree.right;
				// right subtree is more shallow
			} else if ((L != null) && (R == null)) {
				fromRightSubtree.right.thread = true;
				fromRightSubtree.right.child = L;
			} else {
				System.out.print("Error: unexpected finish in while loop at "
						+ "L: " + LeftSubtree.key + " R: " + RightSubtree.key
						+ "\n");
			}

			LeftSubtree = LeftSubtree.right;
			// it should be the same as RightSubtree
			if (LeftSubtree != RightSubtree) {
				System.out.print("Error in while cycle.\n");
			}
			RightSubtree = RightSubtree.right;
		}

		return fromLeftSubtree;
	}

	/**
	 * 
	 * @param leftestx
	 *            x-coordinate of leftmost sibling (the baseline)
	 */
	private void fTRPetrification(int leftestx) {
		/*
		 * Change coordinates
		 */
		fakex = leftestx + this.offset;
		fakey = this.level * (D.minsepy);

		if (isLeaf()) {
			return;
		}
		TreeNode T = child;
		while (T != null) {
			T.fTRPetrification(fakex);
			T = T.right;
		}
	}

	/**
	 * Correcting centering of parents. From now on algorithm works only with
	 * fakex attribute.
	 */
	private void fTRCentering() {
		if (isLeaf()) {
			return;
		}

		TreeNode T = this.child;
		while (T != null) {
			T.fTRCentering();
			T = T.right;
		}

		// allmighty references! :-/
		T = this.child;
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

		T = this.child.right;
		U = child;
		while (T != null) {
			if (T.isLeaf()) {
				T.fakex = U.fakex + D.minsepx;
			}

			U = U.right;
			T = T.right;
		}

		this.toExtremeSon = (this.rightmostChild().fakex - this.leftmostChild().fakex) / 2;
		this.fakex = leftmostChild().fakex + toExtremeSon;
	}

	public void fTRDisposeThreads() {
		/*
		 * Dispose threads
		 */
		if (this.thread) {
			this.thread = false;
			this.child = null;
		}

		TreeNode T = child;
		while (T != null) {
			T.fTRDisposeThreads();
			T = T.right;
		}
	}

	private NodePair<TreeNode> fTRCorrection() {
		NodePair<TreeNode> result = new NodePair<TreeNode>();

		// nothing to correct
		if (isLeaf()) {
			result.left = this;
			result.right = this;
			return result;
		}

		TreeNode LeftSubtree = this.child;
		TreeNode RightSubtree = this.child.right;

		NodePair<TreeNode> fromLeftSubtree = LeftSubtree.fTRCorrection();

		while (RightSubtree != null) {
			TreeNode L = LeftSubtree;
			TreeNode R = RightSubtree;
			int leftx = LeftSubtree.fakex;
			int rightx = RightSubtree.fakex;
			int correction = 0;
			NodePair<TreeNode> fromRightSubtree = RightSubtree.fTRCorrection();

			while ((L != null) && (R != null)) {
				int distance = -(rightx - leftx - D.minsepx);
				System.out.print(L.key + "-" + leftx + "," + R.key + "-"
						+ rightx + "; " + distance + "\n");
				if (distance > 0) {
					correction += distance;
					rightx += distance;
				}

				leftx += L.toExtremeSon;
				rightx -= R.toExtremeSon;
				L = L.rightmostChild();
				R = R.leftmostChild();
			}

			if ((L == null) && (R == null)) {
				fromLeftSubtree.left = fromLeftSubtree.left;
				fromLeftSubtree.right = fromRightSubtree.right;
			} else if ((L == null) && (R != null)) {
				fromLeftSubtree.left.thread = true;
				fromLeftSubtree.left.child = R;

				fromLeftSubtree.left = fromRightSubtree.left;
				fromLeftSubtree.right = fromRightSubtree.right;
			} else if ((L != null) && (R == null)) {
				fromRightSubtree.right.thread = true;
				fromRightSubtree.right.child = L;
			} else {
				System.out.print("Error: unexpected finish in while loop at "
						+ "L: " + LeftSubtree.key + " R: " + RightSubtree.key
						+ "\n");
			}

			RightSubtree.modifier = -correction;

			LeftSubtree = LeftSubtree.right;
			if (LeftSubtree != RightSubtree) {
				System.out.print("Error in while cycle.\n");
			}
			RightSubtree = RightSubtree.right;
		}

		return fromLeftSubtree;
	}

	private void fTRCorrection2(int modsum) {
		modsum += modifier;
		// fakex += modsum;

		if (isLeaf()) {
			return;
		}
		TreeNode T = child;
		while (T != null) {
			T.fTRCorrection2(modsum);
			T = T.right;
		}

	}

	private void fTRBounding(int correction) {
		fakex += correction;
		tox = fakex;
		toy = fakey;
		// this.fTRGetInfo(3);

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
	private int fTRLeftestx() {
		if (child == null) {
			return fakex;
		} else {
			return leftmostChild().fTRLeftestx();
		}
	}

	@SuppressWarnings("unused")
	private void fTRGetInfo(int phase) {
		System.out.print("Node: " + key + " fakex: " + fakex + " mod: "
				+ modifier + " change: " + change + " shift: " + shift + " ("
				+ phase + ")" + "\n");
	}

	@SuppressWarnings("unused")
	private void fTRGetInfo(int phase, int modsum) {
		System.out.print("Node: " + key + " fakex: " + fakex + " mod: "
				+ modifier + " modsum: " + modsum + " change: " + change
				+ " shift: " + shift + " (" + phase + ")" + "\n");
	}

}