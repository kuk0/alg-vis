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
	public int offset = 0; // offset from parent node
	public int level; // "height" from root
	public boolean thread = false; // is this node threaded?

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
		if (thread) {
			if (left != null) {
				g.setColor(Color.red);
				v.drawLine(g, x, y, left.x, left.y);
				g.setColor(Color.black);
			}
			if (right != null) {
				g.setColor(Color.red);
				v.drawLine(g, x, y, right.x, right.y);
				g.setColor(Color.black);
			}
		} else {
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
		// reboxTree();
		// repos();
		// System.out.print("New run.\n");
		fTRFirst(0);
		fTRSecond();
		fTRFourth(0);
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
		this.offset = 0;
		if (left != null)
			left.fTRFirst(level + 1);
		if (right != null)
			right.fTRFirst(level + 1);
	}

	/**
	 * Second & third traverse of tree in fbtr
	 * 
	 * Sets threads with help from extreme nodes. Node is extreme when is lay at
	 * the highest level and is leftmost/rightmost.
	 * 
	 * Simplified (divide & conquer principle): 1. work out left and right
	 * subtree 2. get extreme nodes from left and right subtree 3. calculates
	 * offset from parent & set a new thread if required
	 * 
	 * @return Leftmost and rightmost node on the deepest level of a tree rooted
	 *         by this node
	 */
	public ExtremePair fTRSecond() {
		ExtremePair result = new ExtremePair();
		ExtremePair fromLeftSubtree = null, fromRightSubtree = null;
		int minsep = D.xspan + 2 * D.radius;

		// 1. & 2. work out left & right subtree
		if (left != null)
			fromLeftSubtree = left.fTRSecond();
		if (right != null)
			fromRightSubtree = right.fTRSecond();
		// 3. examine this node
		if (isLeaf()) {
			if (!isRoot()) {
				if (parent.key < key) {
					offset = minsep / 2;
				} else {
					offset = -(minsep / 2);
				}
			}
			result.a.left = this;
			result.a.right = this;
		} else {
			/*
			 * Is not a leaf! So at least one subtree must not be empty. In case
			 * of one subtree is empty, it is not necessary to make a new thread
			 */
			/*
			 * But wait! If there is only one son he will be hanging right below
			 * his parent so make him stand his ground and decide - is he
			 * smaller or greater than his parent.
			 */
			if (left == null) {
				right.offset = minsep / 2;
				result.a.left = fromRightSubtree.a.left;
				result.a.right = fromRightSubtree.a.right;
				return result;
			}

			if (right == null) {
				left.offset = -(minsep / 2);
				result.a.left = fromLeftSubtree.a.left;
				result.a.right = fromLeftSubtree.a.right;
				return result;
			}

			/*
			 * Separate left & right subtrees. loffset - offset of a current
			 * node of the right contour of the left subtree. roffset - offset
			 * of a current node of the left contour of the right subtree.
			 * minsep - minimal separation between two nodes on x-axis.
			 * Algorithm calculate offsets for left and right son.
			 */

			int loffset = 0;
			int roffset = 0;
			BSTNode L = this.left;
			BSTNode R = this.right;
			// if (L == null) {
			// System.out.print("L pointer of " + key
			// + " is null and pointer exception will be raised!");
			// }
			// if (R == null) {
			// System.out.print("R pointer of " + key
			// + " is null and pointer exception will be raised!");
			// }

			/*
			 * // Notice that offset could be a negative integer L.offset =
			 * -(minsep / 2); R.offset = minsep / 2;
			 */
			/*
			 * A little change - left.offset will be 0 and only right.offset
			 * accumulates. Offsets will be corrected after run. The reason is
			 * clear - it will be much more easier to transform to m-ary trees.
			 * Notice that offset could be a negative integer!
			 */
			left.offset = 0;
			right.offset = 0;

			// while both pointers are not null
			while ((L != null) && (R != null)) {
				/*
				 * left.offset + loffset is a distance from L pointer to "this"
				 * node. similar - right.offset + roffset is a distance from R
				 * pointer to "this" node
				 */
				int distance = (roffset - loffset);
				// System.out.print("Distance at L: " + L.key + " R: " + R.key
				// + " is " + distance + ".\n");
				// System.out.print("right.offset: " + right.offset +
				// " roffset: "
				// + roffset + " left.offset: " + left.offset
				// + " loffset: " + loffset + "\n");
				if (distance < minsep) {
					right.offset += (minsep - distance);
					roffset += (minsep - distance);
				}
				// (Goin') To da floooooor!
				/*
				 * But watch out! When you pass through thread there could be
				 * and there will be for sure incorrect offset! And what if
				 * there is another threaded node?
				 */
				boolean LwasThread = L.thread, RwasThread = R.thread;
				if (L.right != null) {
					L = L.right;
					loffset += L.offset;
				} else {
					L = L.left;
					if (L != null) {
						loffset += L.offset;
					}
				}
				if (R.left != null) {
					R = R.left;
					roffset += R.offset;
				} else {
					R = R.right;
					if (R != null) {
						roffset += R.offset;
					}
				}

				BSTNode Elevator = null;
				if (LwasThread) {
					LwasThread = false;
					loffset = 0;
					Elevator = L;
					// I am not very sure about references.. :-/
					while (Elevator.key != this.key) {
						loffset += Elevator.offset;
						Elevator = Elevator.parent;
					}
				}
				if (RwasThread) {
					roffset = 0;
					Elevator = R;
					while (Elevator.key != this.key) {
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
			 * General switch: we want to make a thread iff one pair of extremes
			 * is deeper than others. We assume that threads from subtrees are
			 * set properly.
			 */

			// if (fromLeftSubtree.a.left.level ==
			// fromLeftSubtree.a.right.level) {
			// System.out.print("Left extremes are even at " + this.key
			// + " with left extreme: " + fromLeftSubtree.a.left.key
			// + " and right extreme: " + fromLeftSubtree.a.right.key
			// + "\n");
			// } else {
			// System.out.print("Left extremes are uneven at " + this.key
			// + "!!!\n");
			// }
			// if (fromRightSubtree.a.left.level ==
			// fromRightSubtree.a.right.level) {
			// System.out.print("Right extremes are even at " + this.key
			// + " with left extreme: " + fromRightSubtree.a.left.key
			// + " and right extreme: " + fromRightSubtree.a.right.key
			// + "\n");
			// } else {
			// System.out.print("Right extremes are uneven at " + this.key
			// + "!!!\n");
			// }

			int left_height = fromLeftSubtree.a.left.level;
			int right_height = fromRightSubtree.a.right.level;
			/*
			 * Guess what?! L & R pointers are set right there where we want it.
			 * :)
			 */
			/*
			 * Left subtree is more shallow than right subtree. Thus extreme for
			 * this tree will be the same as for right subtree.
			 */
			if (right_height > left_height) {
				fromLeftSubtree.a.left.thread = true;
				fromLeftSubtree.a.left.right = R;
				result.a.left = fromRightSubtree.a.left;
				result.a.right = fromRightSubtree.a.right;
			} else
			// right subtree is more shallow then left subtree
			if (left_height > right_height) {
				fromRightSubtree.a.right.thread = true;
				fromRightSubtree.a.right.left = L;
				result.a.left = fromLeftSubtree.a.left;
				result.a.right = fromLeftSubtree.a.right;
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
	 * Fourth traverse in fbtr
	 * 
	 * Calculates real position from relative
	 * 
	 * @param xcoordinate
	 *            real x coordinate of parent node
	 */
	public void fTRFourth(int xcoordinate) {
		tox = xcoordinate + this.offset;
		toy = this.level * (D.yspan + 2 * D.radius);
		if (tox < D.x1) {
			D.x1 = tox;
		}
		if (tox > D.x2) {
			D.x2 = tox;
		}
		// this case should not happened
		if (toy < D.y1) {
			D.y1 = toy;
		}
		if (toy > D.y2) {
			D.y2 = toy;
		}
		this.goTo(tox, toy);
		if (!thread) {
			if (left != null) {
				left.fTRFourth(tox);
			}
			if (right != null) {
				right.fTRFourth(tox);
			}
		}
	}

	public void deleteThreads() {
		if (this.thread) {
			this.thread = false;
			this.left = null;
			this.right = null;
		}
		if (left != null) {
			left.deleteThreads();
		}
		if (right != null) {
			right.deleteThreads();
		}
	}
}
