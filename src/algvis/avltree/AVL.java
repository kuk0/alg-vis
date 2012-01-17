package algvis.avltree;

import algvis.bst.BST;
import algvis.bst.BSTFind;
import algvis.core.VisPanel;

public class AVL extends BST {
	public static String dsName = "avltree";
	
	public AVL(VisPanel M) {
		super(M);
		scenario.enable();
	}

	@Override
	public void insert(int x) {
		start(new AVLInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new BSTFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new AVLDelete(this, x));
	}
}
