package algvis.treenode;

import java.awt.Color;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.View;

public class TreeNode extends Node {

	public TreeNode child = null, right = null, parent = null;

	// variables for TR, probably there will be some changes to offset variables
	public int offset = 0; // offset from parent node
	public int level; // "height" from root
	public boolean thread = false; // is this node threaded?

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
		return child == null;
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
				if (this.height < T.height) {
					this.height = T.height;
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
		fTRFirst(0);
		fTRSecond();
		fTRThird();
		fTRFourth(0);
	}

	/**
	 * First traverse of tree in fmtr
	 * 
	 * Sets a proper level to self and sons
	 * 
	 * @param level
	 *            current level in tree
	 */
	private void fTRFirst(int level) {
		this.level = level;
		this.offset = 0;
		TreeNode T = child;
		level++;
		while (T != null) {
			T.fTRFirst(level);
			T = T.right;
		}
	}

	private TreeNode fTRSecond() {
		/*
		 * Notice that result contains leftmost and rightmost deepest node in
		 * form result.child for left and result.right for right
		 */
		TreeNode result = new TreeNode(null, -47);
		/*
		 * Results from subtrees are stored as children It seems this is not
		 * necessary
		 */
		TreeNode subresults = new TreeNode(null, -47);
		int minsep = D.xspan + 2 * D.radius;

		if (isLeaf()) {
			offset = 0;
			result.right = this;
			result.child = this;
			return result;
		}

		/*
		 * I think it is not necessary to do this if this is a leaf
		 */

		TreeNode S = subresults; // iterator for subresults
		TreeNode T = child;
		while (T != null) {
			S.child = T.fTRSecond();
			S.addRight(new TreeNode(null, -46));
			T = T.right;
		}

		/*
		 * Node has at least one child. Take their extremes as beginning
		 * extremes.
		 */
		TreeNode LeftExtremes = S.child;

		/*
		 * Ok. Now it's time to separate subtrees..
		 */

		TreeNode LeftSubtree = this.child;
		TreeNode RightSubtree = this.child;

		/*
		 * ..iff this node contains 2 and more vertices
		 */
		if (RightSubtree.right != null) {
			RightSubtree = RightSubtree.right;
			S = S.right;
			TreeNode RightExtremes = S.child;
			boolean firstrun = true;
			while ((RightSubtree.right != null) || (firstrun)) {
				if (firstrun)
					firstrun = false;
				TreeNode L = LeftSubtree;
				TreeNode R = RightSubtree;

				int loffset = L.offset;
				int roffset = L.offset;

				while ((L != null) && (R != null)) {
					/*
					 * LeftSubtree.offset + loffset is a distance from L pointer
					 * to "this" node. similar - RightSubtree.offset + roffset
					 * is a distance from R pointer to "this" node
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
					if (distance < minsep) {
						RightSubtree.offset += (minsep - distance);
						roffset += (minsep - distance);
					}
					// (Goin') To da floooooor!
					/*
					 * But watch out! When you pass through thread there could
					 * be and there will be for sure incorrect offset! And what
					 * if there is another threaded node?
					 */
					boolean LwasThread = L.thread, RwasThread = R.thread;
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
				 * Some changing of distances should be made here
				 */

				if ((L == null) && (R == null)) {
					LeftExtremes.child = LeftExtremes.child;
					LeftExtremes.right = RightExtremes.right;
				} else if ((L == null) && (R != null)) {
					LeftExtremes.child.thread = true;
					LeftExtremes.child.child = R;
					
					LeftExtremes.child = RightExtremes.child;
					LeftExtremes.right = RightExtremes.right;
				} else if ((L != null) && (R == null))  {
					RightExtremes.right.thread = true;
					RightExtremes.right.child = L;
					
					LeftExtremes.child = LeftExtremes.child;
					LeftExtremes.right = LeftExtremes.right;
				} else {
					System.out.print("Shit happened...\n");
				}

			}
		}

		result = LeftExtremes;
		return result;
	}

	/*
	 * There will be corrections to bad layouts
	 */
	private void fTRThird() {
		// TODO Auto-generated method stub

	}


	private void fTRFourth(int xcoordinate) {
		/*
		 * Change coordinates
		 */
		tox = xcoordinate + this.offset;
		toy = this.level * (D.yspan + 2 * D.radius);
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

		/*
		 * Dispose threads
		 */
		if (this.thread) {
			this.thread = false;
			this.child = null;
		}

		TreeNode T = child;
		while (T != null) {
			T.fTRFourth(tox);
			T = T.right;
		}
	}

}
