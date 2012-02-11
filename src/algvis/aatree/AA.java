package algvis.aatree;

import algvis.bst.BST;
import algvis.bst.BSTFind;
import algvis.bst.BSTNode;
import algvis.core.Layout;
import algvis.core.View;
import algvis.core.VisPanel;

public class AA extends BST {
	public static String dsName = "aatree";
	public boolean mode23 = false;

	@Override
	public String getName() {
		return "aatree";
	}

	public AA(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new AAInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new BSTFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new AADelete(this, x));
	}

	public void setMode23(boolean set) {
		mode23 = set;
		scenario.newStep();
		reposition();
	}

	public boolean getMode23() {
		return mode23;
	}

	public BSTNode skew(BSTNode w) {
		if (w.getLeft() != null && w.getLeft().getLevel() == w.getLevel()) {
			w = w.getLeft();
			rotate(w);
			reposition();
		}
		return w;
	}

	public BSTNode split(BSTNode w) {
		BSTNode r = w.getRight();
		if (r != null && r.getRight() != null
				&& r.getRight().getLevel() == w.getLevel()) {
			w = r;
			rotate(w);
			w.setLevel(w.getLevel() + 1);
			reposition();
		}
		return w;
	}

	@Override
	public void draw(View V) {
		if (getRoot() != null) {
			getRoot().moveTree();
			((AANode) getRoot()).drawTree2(V);
		}
		if (getV() != null) {
			getV().move();
			getV().draw(V);
		}
	}
	
	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}
}
