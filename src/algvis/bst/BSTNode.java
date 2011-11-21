package algvis.bst;

import java.awt.Color;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.NodePair;
import algvis.core.View;

public class BSTNode extends Node {
	public int leftw, rightw;
	public BSTNode left = null, right = null, parent = null;

	// variables for TR
	public int offset = 0; // offset from parent node
	public int level; // distance to root
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

	public void drawTree(View v) {
		if (this.state != INVISIBLE) {
			if (thread) {
				v.setColor(Color.red);
			} else {
				v.setColor(Color.black);
			}
			if ((left != null) && (left.state != INVISIBLE)) {
				v.drawLine(x, y, left.x, left.y);
			}
			if ((right != null) && (right.state != INVISIBLE)) {
				v.drawLine(x, y, right.x, right.y);
			}
		}
		if (left != null) {
			left.drawTree(v);
		}
		if (right != null) {
			right.drawTree(v);
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
	@SuppressWarnings("unused")
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
		fTRInitialization(0);
		fTRPrePosition();
		fTRDisposeThreads();
		fTRPetrification(0);
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
	 * Sets a proper level to self and children
	 * 
	 * @param level
	 *            current level in tree
	 */
	private void fTRInitialization(int level) {
		// this.state = INVISIBLE;
		this.level = level;
		this.offset = 0;
		if (left != null)
			left.fTRInitialization(level + 1);
		if (right != null)
			right.fTRInitialization(level + 1);
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
	private NodePair<BSTNode> fTRPrePosition() {
		NodePair<BSTNode> result = new NodePair<BSTNode>();
		NodePair<BSTNode> fromLeftSubtree = null, fromRightSubtree = null;

		// 1. & 2. work out left & right subtree
		if (left != null)
			fromLeftSubtree = left.fTRPrePosition();
		if (right != null)
			fromRightSubtree = right.fTRPrePosition();
		// 3. examine this node
		if (isLeaf()) {
			if (!isRoot()) {
				if (parent.key < key) {
					offset = D.minsepx / 2;
				} else {
					offset = -(D.minsepx / 2);
				}
			}
			result.left = this;
			result.right = this;
		} else {
			/*
			 * Is not a leaf! So at least one subtree must not be empty. In case
			 * of one subtree is empty, it is not necessary to make a new
			 * thread.
			 * 
			 * There is only one son so proper offset have to be set.
			 */
			if (left == null) {
				right.offset = D.minsepx / 2;
				result.left = fromRightSubtree.left;
				result.right = fromRightSubtree.right;
				return result;
			}

			if (right == null) {
				left.offset = -(D.minsepx / 2);
				result.left = fromLeftSubtree.left;
				result.right = fromLeftSubtree.right;
				return result;
			}

			/*
			 * Separate left & right subtrees. loffset - offset of a current
			 * node of the right contour of the left subtree. roffset - offset
			 * of a current node of the left contour of the right subtree.
			 * 
			 * Algorithm calculates offsets for left and right son.
			 */

			int loffset = 0;
			int roffset = 0;
			BSTNode L = this.left;
			BSTNode R = this.right;
			/*
			 * A little change - left.offset will be 0 and only right.offset
			 * accumulates. Offsets will be corrected after run. The reason is
			 * clear - it will be much more easier to transform to m-ary trees.
			 * Notice that offset could be a negative integer!
			 */
			left.offset = 0;
			right.offset = 0;

			while ((L != null) && (R != null)) {
				/*
				 * left.offset + loffset is a distance from L pointer to "this"
				 * node. Similar - right.offset + roffset is a distance from R
				 * pointer to "this" node
				 */
				int distance = (roffset - loffset);
				if (distance < D.minsepx) {
					right.offset += (D.minsepx - distance);
					roffset += (D.minsepx - distance);
				}
				/*
				 * When passes through thread there will be for sure incorrect
				 * offset! So Elevator calculate this new offset. In algorithm
				 * TR published by Reingold this value is already calculated.
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

			/*
			 * Left subtree is more shallow than right subtree. Thus extreme for
			 * this tree will be the same as for right subtree.
			 * 
			 * Notice that L & R pointers are set right there where we want it.
			 */
			if ((L == null) && (R != null)) {
				fromLeftSubtree.left.thread = true;
				fromLeftSubtree.left.right = R;
				result.left = fromRightSubtree.left;
				result.right = fromRightSubtree.right;
			} else
			// right subtree is more shallow then left subtree
			if ((L != null) && (R == null)) {
				fromRightSubtree.right.thread = true;
				fromRightSubtree.right.left = L;
				result.left = fromLeftSubtree.left;
				result.right = fromLeftSubtree.right;
			} else
			// subtrees have the same height
			if ((L == null) && (R == null)) {
				result.left = fromLeftSubtree.left;
				result.right = fromRightSubtree.right;
			} else {
				System.out.print("Error: unexpected finish in while loop at "
						+ "L: " + left.key + " R: " + right.key + "\n");
			}

		}
		return result;
	}

	private void fTRDisposeThreads() {
		if (this.thread) {
			this.thread = false;
			this.left = null;
			this.right = null;
		}
		if (left != null) {
			left.fTRDisposeThreads();
		}
		if (right != null) {
			right.fTRDisposeThreads();
		}
	}

	/**
	 * Fourth traverse in fbtr
	 * 
	 * Calculates real position from relative
	 * 
	 * @param xcoordinate
	 *            real x coordinate of parent node
	 */
	private void fTRPetrification(int xcoordinate) {
		tox = xcoordinate + this.offset;
		toy = this.level * D.minsepy;
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
		if (!thread) {
			if (left != null) {
				left.fTRPetrification(tox);
			}
			if (right != null) {
				right.fTRPetrification(tox);
			}
		}
	}
}
