package algvis.treap;

import algvis.bst.BST;
import algvis.bst.BSTFind;
import algvis.core.VisPanel;

public class Treap extends BST {
	public static String dsName = "treap";
	
	public Treap(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new TreapInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new BSTFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new TreapDelete(this, x));
	}
}
