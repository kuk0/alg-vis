package algvis.treenode;

import java.awt.Color;
import java.awt.Graphics;

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
	public void drawTree(Graphics g, View v) {
		if (this.state != INVISIBLE) {
			if (thread) {
				g.setColor(Color.red);
				v.drawLine(x, y, child.x, child.y);
				g.setColor(Color.black);
			} else {
				TreeNode T = child;
				while (T != null) {
					g.setColor(Color.black);
					v.drawLine(x, y, T.x, T.y);
					T.drawTree(g, v);
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
		} else {
			child.addRight(T);
		}
	}
	
	public TreeNode LeftmostChild() {
		return child;
	}
	
	public TreeNode RightmostChild() {
		TreeNode T = child;
		if (T != null) {
			while (T.right != null) {
				T = T.right;
			}
		}
		return T;
	}

	public void reposition() {
		fTRFirst(0);
		fTRSecond();
		fTRThird();
		fTRFourth();
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
		 * Results from subtrees are stored as children
		 */
		TreeNode subresults = new TreeNode(null, -47);
		int minsep = D.xspan + 2 * D.radius;

		TreeNode T = child;
		while (T != null) {
			T.fTRSecond();
		}
		return result;
	}

	private void fTRThird() {
		// TODO Auto-generated method stub

	}

	private void fTRFourth() {
		// TODO Auto-generated method stub

	}

}
