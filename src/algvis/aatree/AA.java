package algvis.aatree;

import algvis.bst.BST;
import algvis.bst.BSTFind;
import algvis.bst.BSTNode;
import algvis.core.VisPanel;

public class AA extends BST {
	public static String dsName = "aatree";
	boolean mode23 = false;

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

	public BSTNode skew(BSTNode w) {
		if (w.left != null && ((AANode) w.left).level == ((AANode) w).level) {
			w = w.left;
			rotate(w);
			reposition();
		}
		return w;
	}

	public BSTNode split(BSTNode w) {
		BSTNode r = w.right;
		if (r != null && r.right != null
				&& ((AANode) r.right).level == ((AANode) w).level) {
			w = r;
			rotate(w);
			((AANode) w).level++;
			reposition();
		}
		return w;
	}
}
