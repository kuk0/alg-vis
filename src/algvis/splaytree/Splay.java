package algvis.splaytree;

import algvis.bst.BST;
import algvis.core.View;
import algvis.core.VisPanel;

public class Splay extends BST {
	public static String dsName = "splaytree";
	SplayNode root2 = null, vv = null;

	@Override
	public String getName() {
		return "splaytree";
	}

	public Splay(VisPanel M) {
		super(M);
		scenario.enable(false);
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
	public void draw(View V) {
		if (root != null) {
			root.moveTree();
			root.drawTree(V);
		}
		if (root2 != null) {
			root2.moveTree();
			root2.drawTree(V);
		}
		if (v != null) {
			v.move();
			v.draw(V);
		}
		if (vv != null) {
			vv.move();
			vv.draw(V);
		}
	}

	/*
	 * public String stats() { return super.stats()+";   Potential: "+ ((root ==
	 * null) ? "0" : ((SplayNode)root).pot); }
	 */

	public void rotate2(SplayNode v) {
		if (v.isLeft()) {
			rightrot(v);
		} else {
			leftrot(v);
		}
		v.reposition();
		if (v.getLeft() != null) {
			v.getLeft().calc();
		}
		if (v.getRight() != null) {
			v.getRight().calc();
		}
		v.calc();
	}
}
