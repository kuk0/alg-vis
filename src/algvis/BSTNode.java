package algvis;

import java.awt.Color;
import java.awt.Graphics;

public class BSTNode extends Node {
	int leftw, rightw;
	BSTNode left = null, right = null, parent = null;

	// statistics
	int size = 1, height = 1, sumh = 1;

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

	public void rebox() {
		leftw = (left == null) ? D.xspan + D.radius : left.leftw + left.rightw;
		rightw = (right == null) ? D.xspan + D.radius : right.leftw
				+ right.rightw;
	}

	public void reboxTree() {
		if (left != null) {
			left.reboxTree();
		}
		if (right != null) {
			right.reboxTree();
		}
		rebox();
	}

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
			left.goTo(this.tox - left.rightw, this.toy + 2 * D.radius
							+ D.yspan);
			left.repos();
		}
		if (right != null) {
			right.goTo(this.tox + right.leftw, this.toy + 2 * D.radius
					+ D.yspan);
			right.repos();
		}
	}

	/*
	 * void rerepos() { if (left!=null) left.rerepos(); if (right!=null)
	 * right.rerepos(); if (left!=null && right!=null) goTo
	 * ((left.tox+right.tox)/2, this.toy); }
	 */

	public void _reposition() {
		reboxTree();
		repos();
		// rerepos();
	}
	
	public BSTNode find(int x, int y) {
		if (inside(x,y)) return this;
		if (left != null) {
			BSTNode tmp = left.find(x, y);
			if (tmp != null) return tmp;
		}
		if (right != null) {
			return right.find(x, y);
		}
		return null;
	}
}
