package algvis.splaytree;

import algvis.bst.BST;
import algvis.core.Layout;
import algvis.core.View;
import algvis.core.VisPanel;

public class SplayTree extends BST {
	public static String dsName = "splaytree";
	SplayNode root2 = null, vv = null;
	SplayNode w1 = null, w2 = null;


	@Override
	public String getName() {
		return "splaytree";
	}

	public SplayTree(VisPanel M) {
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
		if (w1 != null && w1.getParent() != null) {
			V.drawWideLine(w1.x, w1.y, w1.getParent().x, w1.getParent().y);
		}
		if (w2 != null && w2.getParent() != null) {
			V.drawWideLine(w2.x, w2.y, w2.getParent().x, w2.getParent().y);
		}

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

	@Override
	public void clear() {
		super.clear();
		vv = null;
	}
	
	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}
}
