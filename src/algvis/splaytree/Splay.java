package algvis.splaytree;

import java.awt.Graphics;

import algvis.bst.BST;
import algvis.bst.BSTNode;
import algvis.core.View;
import algvis.core.VisPanel;

public class Splay extends BST {
	BSTNode root2 = null, vv = null;

	public Splay(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new SplayInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new SplayFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new SplayDelete(this, x));
	}

	@Override
	public void clear() {
		root = null;
		setStats();
	}

	@Override
	public void clean() {
		root2 = v = vv = null;
	}

	@Override
	public void draw(Graphics G, View V) {
		if (root != null) {
			root.moveTree();
			root.drawTree(G, V);
		}
		if (root2 != null) {
			root2.moveTree();
			root2.drawTree(G, V);
		}
		if (v != null) {
			v.move();
			v.draw(G, V);
		}
		if (vv != null) {
			vv.move();
			vv.draw(G, V);
		}
	}

	/*
	 * public String stats() { return super.stats()+";   Potential: "+ ((root ==
	 * null) ? "0" : ((SplayNode)root).pot); }
	 */

	public void rotate2(BSTNode v) {
		if (v.isLeft()) {
			rightrot(v);
		} else {
			leftrot(v);
		}
		v._reposition();
		if (v.left != null) {
			v.left.calc();
		}
		if (v.right != null) {
			v.right.calc();
		}
		v.calc();
	}
}
