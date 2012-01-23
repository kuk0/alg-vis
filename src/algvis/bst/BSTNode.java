package algvis.bst;

import java.awt.Color;

import algvis.core.DataStructure;
import algvis.core.Layout;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.NodePair;
import algvis.core.View;
import algvis.scenario.commands.bstnode.LinkLeftCommand;
import algvis.scenario.commands.bstnode.LinkRightCommand;
import algvis.scenario.commands.bstnode.SetLevelCommand;

public class BSTNode extends Node {
	private BSTNode left = null, right = null, parent = null;
	public int leftw, rightw;

	// variables for the Reingold-Tilford layout
	int offset = 0; // offset from parent node
	private int level; // distance to root
	boolean thread = false; // is this node threaded?

	// statistics
	public int size = 1, height = 1, sumh = 1;

	public BSTNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public BSTNode(DataStructure D, int key) {
		super(D, key);
	}

	public BSTNode getLeft() {
		return left;
	}

	public void setLeft(BSTNode left) {
		this.left = left;
	}

	public void setLevel(int level) {
		if (this.level != level) {
			D.scenario.add(new SetLevelCommand(this, level));
			this.level = level;
		}
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
			D.scenario.add(new LinkLeftCommand(this, newLeft, true));
		}
	}

	/**
	 * removes edge between this and left
	 */
	public void unlinkLeft() {
		getLeft().setParent(null);
		D.scenario.add(new LinkLeftCommand(this, getLeft(), false));
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
			D.scenario.add(new LinkRightCommand(this, newRight, true));
		}
	}

	/**
	 * removes edge between this and right
	 */
	public void unlinkRight() {
		getRight().setParent(null);
		D.scenario.add(new LinkRightCommand(this, getRight(), false));
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

	static int i;

	public void drawTree(View v) {
		if (state != INVISIBLE) {
			if (thread) {
				v.setColor(Color.red);
			} else {
				v.setColor(Color.black);
			}
			if ((getLeft() != null) && (getLeft().state != INVISIBLE)) {
				v.drawLine(x, y, getLeft().x, getLeft().y);
			}
			if ((getRight() != null) && (getRight().state != INVISIBLE)) {
				v.drawLine(x, y, getRight().x, getRight().y);
			}
		}
		if (getLeft() != null) {
			getLeft().drawTree(v);
		}
		if (D instanceof BST && ((BST) D).order) {
			v.setColor(Color.LIGHT_GRAY);
			++i;
			v.drawLine(x, y, x, -20);
			v.drawString("" + i, x, -23, 10);
		}
		if (getRight() != null) {
			getRight().drawTree(v);
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
		leftw = (getLeft() == null) ? D.xspan + D.radius : getLeft().leftw
				+ getLeft().rightw;
		// rightw is computed analogically
		rightw = (getRight() == null) ? D.xspan + D.radius : getRight().leftw
				+ getRight().rightw;
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
			// System.out.println ("r" + key + " " +leftw +"  "+ rightw);
		}
		if (this.toy > D.y2) {
			D.y2 = this.toy;
		}
		if (getLeft() != null) {
			getLeft().goTo(this.tox - getLeft().rightw,
					this.toy + 2 * D.radius + D.yspan);
			getLeft().repos();
		}
		if (getRight() != null) {
			getRight().goTo(this.tox + getRight().leftw,
					this.toy + 2 * D.radius + D.yspan);
			getRight().repos();
		}
	}

	public void reposition() {
		if (D.M.S.layout == Layout.SIMPLE) { // simple layout
			reboxTree();
			repos();
		} else { // Reingold-Tilford layout
			RTPreposition();
			RTPetrification(0, 0);
			reboxTree();
		}
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
				offset = isLeft() ? -D.minsepx / 2 : +D.minsepx / 2;
			}
			result.left = this;
			result.right = this;
		} else { // This is not a leaf; at least one subtree is non-empty.
			/*
			 * If one subtree is empty, it is not necessary to make a new
			 * thread. A proper offset must be set.
			 */
			if (getLeft() == null) {
				getRight().offset = D.minsepx / 2;
				result.left = fromRightSubtree.left;
				result.right = fromRightSubtree.right;
				return result;
			}

			if (getRight() == null) {
				getLeft().offset = -D.minsepx / 2;
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
				int distance = (loffset + D.minsepx - roffset);
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
				L = (L.getRight() != null) ? L.getRight() : L.getLeft();
				if (L != null) {
					loffset += L.offset;
				}
				R = (R.getLeft() != null) ? R.getLeft() : R.getRight();
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
				fromLeftSubtree.left.setRight(R);
				result.left = fromRightSubtree.left;
				result.right = fromRightSubtree.right;
			} else if ((L != null) && (R == null)) { // the left subtree is
														// deeper than the right
														// subtree
				fromRightSubtree.right.thread = true;
				fromRightSubtree.right.setLeft(L);
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
			thread = false;
			setLeft(null);
			setRight(null);
		}
		if (getLeft() != null) {
			getLeft().RTPetrification(tox, y + D.minsepy);
		}
		if (getRight() != null) {
			getRight().RTPetrification(tox, y + D.minsepy);
		}
	}

	public void subtreeColor(NodeColor color) {
		setColor(color);
		if (getLeft() != null)
			getLeft().subtreeColor(color);
		if (getRight() != null)
			getRight().subtreeColor(color);
	}

	public BSTNode getRight() {
		return right;
	}

	public BSTNode setRight(BSTNode right) {
		this.right = right;
		return right;
	}

	public BSTNode getParent() {
		return parent;
	}

	public BSTNode setParent(BSTNode parent) {
		this.parent = parent;
		return parent;
	}
}
