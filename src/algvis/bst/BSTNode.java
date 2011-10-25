package algvis.bst;

import java.awt.Color;
import java.awt.Graphics;

import algvis.core.DataStructure;
import algvis.core.ExtremePair;
import algvis.core.Node;
import algvis.core.View;

public class BSTNode extends Node {
	public int leftw, rightw;
	public BSTNode left = null, right = null, parent = null;

	// variables for TR, probably there will be some changes to offset variables
	public int xofffromparent; // offset from parent node
	public int level; // "height" from root
	public boolean thread; // is this node threaded?

	// statistics
	public int size = 1, height = 1, sumh = 1;

	public BSTNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public BSTNode(DataStructure D, int key) {
		super(D, key);
	}

	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeaf() {
		return left == null && right == null;
	}

	public boolean isLeft() {
		return parent.left == this;
	}

	public void linkleft(BSTNode v) {
		left = v;
		if (v != null) {
			v.parent = this;
		}
	}

	public void linkright(BSTNode v) {
		right = v;
		if (v != null) {
			v.parent = this;
		}
	}

	public void isolate() {
		left = right = parent = null;
	}

	/**
	 * Calculate the height, size, and sum of heights of this node, assuming
	 * that this was already calculated for its children.
	 */
	public void calc() {
		int ls = 0, rs = 0, lh = 0, rh = 0, lsh = 0, rsh = 0;
		if (left != null) {
			ls = left.size;
			lh = left.height;
			lsh = left.sumh;
		}
		if (right != null) {
			rs = right.size;
			rh = right.height;
			rsh = right.sumh;
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
		if (left != null) {
			left.calcTree();
		}
		if (right != null) {
			right.calcTree();
		}
		calc();
	}

	public void setArc() {
		setArc(parent);
	}

	public void drawTree(Graphics g, View v) {
		if (left != null) {
			g.setColor(Color.black);
			v.drawLine(g, x, y, left.x, left.y);
			left.drawTree(g, v);
		}
		if (right != null) {
			g.setColor(Color.black);
			v.drawLine(g, x, y, right.x, right.y);
			right.drawTree(g, v);
		}
		draw(g, v);
	}

	public void moveTree() {
		if (left != null) {
			left.moveTree();
		}
		if (right != null) {
			right.moveTree();
		}
		move();
	}

	/**
	 * Create an (imaginary) box around the subtree rooted at this node.
	 * Calculate the width from the node to the left side (leftw) and the width
	 * from the node to the right side (rightw). Assumption: this box has
	 * already been created for both children.
	 */
	public void rebox() {
		// if there is a left child, leftw = width of the box enclosing the
		// whole left subtree,
		// i.e., leftw+rightw; otherwise the width is the node radius plus some
		// additional
		// space called xspan
		leftw = (left == null) ? D.xspan + D.radius : left.leftw + left.rightw;
		// rightw is computed analogically
		rightw = (right == null) ? D.xspan + D.radius : right.leftw
				+ right.rightw;
	}

	/**
	 * Rebox the whole subtree calculating the widths recursively bottom-up.
	 */
	public void reboxTree() {
		if (left != null) {
			left.reboxTree();
		}
		if (right != null) {
			right.reboxTree();
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
			// System.out.println ("r" + key + " " +leftw +"  "+ rightw);
		}
		if (this.toy > D.y2) {
			D.y2 = this.toy;
		}
		if (left != null) {
			left.goTo(this.tox - left.rightw, this.toy + 2 * D.radius + D.yspan);
			left.repos();
		}
		if (right != null) {
			right.goTo(this.tox + right.leftw, this.toy + 2 * D.radius
					+ D.yspan);
			right.repos();
		}
	}

	public void reposition() {
		reboxTree();
		repos();
	}

	/**
	 * Find the node at coordinates (x,y). This is used to identify the node
	 * that has been clicked by user.
	 */
	public BSTNode find(int x, int y) {
		if (inside(x, y))
			return this;
		if (left != null) {
			BSTNode tmp = left.find(x, y);
			if (tmp != null)
				return tmp;
		}
		if (right != null) {
			return right.find(x, y);
		}
		return null;
	}

	/**
	 * First traverse of tree in fbtr
	 * 
	 * Sets a proper level to self and sons
	 * 
	 * @param level
	 *            current level in tree
	 */
	public void fTRFirst(int level) {
		this.level = level;
		this.xofffromparent = 0;
		this.thread = false;
		if (left != null)
			left.fTRFirst(level + 1);
		if (right != null)
			right.fTRFirst(level + 1);
	}

	/**
	 * Second traverse of tree in fbtr
	 * 
	 * Sets threads with help from extreme nodes. Node is extreme when is lay at
	 * the highest level and is leftmost/rightmost.
	 * 
	 * Simplified (divide & conquer principle): - work out left and right
	 * subtrees - get extreme nodes from left and right subtree - set a new
	 * thread if required
	 * 
	 * @return Leftmost and rightmost node on the deepest level of a tree rooted
	 *         by this node
	 */
	public ExtremePair fTRSecond() {
		ExtremePair result = new ExtremePair();
		ExtremePair fromLeftSubtree = null, fromRightSubtree = null;

		if (left != null)
			fromLeftSubtree = left.fTRSecond();
		if (right != null)
			fromRightSubtree = right.fTRSecond();
		if (isLeaf()) {
			result.a.left = this;
			result.a.right = this;
		} else {
			// is not a leaf! so at least one subtree must not be empty
			if (left == null) {
				result.a.left = fromRightSubtree.a.left;
				result.a.right = fromRightSubtree.a.right;
				return result;
			}

			if (right == null) {
				result.a.left = fromLeftSubtree.a.left;
				result.a.right = fromLeftSubtree.a.right;
				return result;
			}

			/*
			 * general switch: we want to make a thread iff one of extremes is
			 * deeper than others. We assume that threads from subtrees are set
			 * properly.
			 */
			// left subtree is more shallow than right subtree
			if (true) {

			} else
			// right subtree is more shallow then left subtree
			if (true) {

			} else
			// subtrees have the same height
			{
				result.a.left = fromLeftSubtree.a.left;
				result.a.right = fromRightSubtree.a.right;
			}

		}
		return result;
	}

	/**
	 * Third traverse in fbtr
	 * 
	 * Calculates offset from parent
	 */
	public void fTRThird() {

	}

	/**
	 * Fourth traverse in fbtr
	 * 
	 * Calculates real position from relative
	 * 
	 * @param xcoordinate
	 *            real x coordinate of this node
	 */
	public void fTRFourth(int xcoordinate) {

	}
}
