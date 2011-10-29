package algvis.marynode;

import java.awt.Color;
import java.awt.Graphics;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.View;

public class MaryNode extends Node {

	public MaryNode child = null, right = null, parent = null;

	// variables for TR, probably there will be some changes to offset variables
	public int offset = 0; // offset from parent node
	public int level; // "height" from root
	public boolean thread = false; // is this node threaded?

	// statistics
	public int size = 1, height = 1, sumh = 1;
	public int nos = 0;// number of sons, probably useless

	public MaryNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public MaryNode(DataStructure D, int key) {
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
			MaryNode M = child;
			while (M != null) {
				this.size += M.size;
				if (this.height < M.height) {
					this.height = M.height;
				}
				M = M.right;
			}
		}
	}

	/**
	 * Calculate height and size of subtree rooted by "this" node bottom-up
	 */
	public void calcTree() {
		if (!isLeaf()) {
			MaryNode M = child;
			while (M != null) {
				M.calcTree();
				M = M.right;
			}
		}
		calc();
	}

	// self note: what is this? O.o
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
				MaryNode M = child;
				while (M != null) {
					g.setColor(Color.black);
					v.drawLine(x, y, M.x, M.y);
					M.drawTree(g, v);
					M = M.right;
				}
			}
		}
		draw(v);
	}

	public void movetTree() {
		MaryNode M = child;
		while (M != null) {
			M.movetTree();
		}
		move();
	}

	public void reposition() {

	}

	/**
	 * Find the node at coordinates (x,y). This is used to identify the node
	 * that has been clicked by user.
	 */
	public MaryNode find(int x, int y) {
		if (inside(x, y)) 
			return this;
		MaryNode M = this.child;
		MaryNode tmp = null;
		while ((M != null) && (tmp == null)) {
			tmp = M.find(x, y);
			M = M.right;
		}
		return tmp;
	}
}
