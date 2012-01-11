package algvis.bst;

import java.awt.Color;

import algvis.core.DataStructure;
import algvis.core.Layout;
import algvis.core.Node;
import algvis.core.NodePair;
import algvis.core.View;

public class BSTNode extends Node {
	public BSTNode left = null, right = null, parent = null;
	public int leftw, rightw;

	// variables for the Reingold-Tilford layout
	int offset = 0; // offset from the parent node
	int level; // distance from the root
	boolean thread = false; // is this node threaded?

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
		return parent != null && parent.left == this;
	}

	public void linkLeft(BSTNode v) {
		left = v;
		if (v != null) {
			v.parent = this;
		}
	}

	public void linkRight(BSTNode v) {
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

	public void drawTree(View v) {
		if (state != INVISIBLE) {
			if (thread) {
				v.setColor(Color.red);
			} else {
				v.setColor(Color.black);
			}
			if ((left != null) && (left.state != INVISIBLE)) {
				v.drawLine(x, y, left.x, left.y);
				if (v.output) System.out.println("  Edge("+id+","+left.id+")");
			}
			if ((right != null) && (right.state != INVISIBLE)) {
				v.drawLine(x, y, right.x, right.y);
				if (v.output) System.out.println("  Edge("+id+","+right.id+")");
			}
		}
		if (left != null) {
			left.drawTree(v);
		}
		if (right != null) {
			right.drawTree(v);
		}
		if (false) {
			v.setColor(Color.LIGHT_GRAY);
			v.drawLine(x, y, x, -100);
		}
		draw(v);
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
		/*
		 * if there is a left child, leftw = width of the box enclosing the
		 * whole left subtree, i.e., leftw+rightw; otherwise the width is the
		 * node radius plus some additional space called xspan
		 */
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
		if (D.M.S.layout == Layout.SIMPLE) { // simple layout
			reboxTree();
			repos();
		} else { // Reingold-Tilford layout
			RTPreposition();
			RTPetrification(0, 0);
		}
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
		if (left != null)
			fromLeftSubtree = left.RTPreposition();
		if (right != null)
			fromRightSubtree = right.RTPreposition();
		// 3. examine this node
		if (isLeaf()) {
			if (!isRoot()) {
				offset = isLeft() ? -D.minsepx / 2 : +D.minsepx / 2;
			}
			result.left = this;
			result.right = this;
		} else { // This is not a leaf; at least one subtree is non-empty.
			/*
			 * If one subtree is empty, it is not necessary to make a new
			 * thread. A proper offset must be set.
			 */
			if (left == null) {
				right.offset = D.minsepx / 2;
				result.left = fromRightSubtree.left;
				result.right = fromRightSubtree.right;
				return result;
			}

			if (right == null) {
				left.offset = -D.minsepx / 2;
				result.left = fromLeftSubtree.left;
				result.right = fromLeftSubtree.right;
				return result;
			}

			// Calculate offsets for the left and the right son.
			int loffset = 0; // offset of this node from the right contour of
								// the left subtree.
			int roffset = 0; // offset of this node from the left contour of the
								// right subtree.
			BSTNode L = left;
			BSTNode R = right;
			/*
			 * First, left.offset is 0 and only right.offset accumulates. The
			 * offsets are corrected afterwards (this way is easier to
			 * generalize to m-ary trees). Note that offsets can be negative.
			 */
			left.offset = 0;
			right.offset = 0;

			// traverse the right contour of the left subtree and the left
			// counour of the right subtree
			while ((L != null) && (R != null)) {
				/*
				 * left.offset + loffset is the horizontal distance from L to
				 * this node. Similarly, right.offset + roffset is the
				 * horizontal distance from R to this node.
				 */
				int distance = (loffset + D.minsepx - roffset);
				if (distance > 0) {
					right.offset += distance;
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
						Elevator = Elevator.parent;
					}
				}
				if (RwasThread) {
					roffset = 0;
					Elevator = R;
					while (Elevator != this) {
						roffset += Elevator.offset;
						Elevator = Elevator.parent;
					}
				}
			}

			/*
			 * Now, distances should be 0 for left and some value for right.. So
			 * lets change it
			 */

			right.offset /= 2;
			left.offset = -right.offset;

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
	 * Calculate the absolute coordinates from the relative ones
	 * and dispose the threads.
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
		// this case should be always false
		if (toy < D.y1) {
			D.y1 = toy;
		}
		if (toy > D.y2) {
			D.y2 = toy;
		}

		if (thread) {
			thread = false;
			left = null;
			right = null;
		}
		if (left != null) {
			left.RTPetrification(tox, y + D.minsepy);
		}
		if (right != null) {
			right.RTPetrification(tox, y + D.minsepy);
		}
	}
}
