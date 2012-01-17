package algvis.aatree;

import algvis.bst.BST;
import algvis.bst.BSTFind;
import algvis.bst.BSTNode;
import algvis.core.VisPanel;

public class AA extends BST {
	public static String dsName = "aatree";
	private boolean mode23 = false;

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
	
	public void setMode23(boolean setted) {
		mode23 = setted;
		scenario.addingNextStep();
		reposition();
	}
	
	public boolean getMode23() {
		return mode23;
	}

	public BSTNode skew(BSTNode w) {
		if (w.left != null && w.left.getLevel() == w.getLevel()) {
			w = w.left;
			rotate(w);
			reposition();
		}
		return w;
	}

	public BSTNode split(BSTNode w) {
		BSTNode r = w.right;
		if (r != null && r.right != null && r.right.getLevel() == w.getLevel()) {
			w = r;
			rotate(w);
			w.setLevel(w.getLevel() + 1);
			reposition();
		}
		return w;
	}
}
