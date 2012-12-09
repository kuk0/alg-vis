package algvis.ds.intervaltree.fenwick;

import algvis.core.Algorithm;
import algvis.ui.VisPanel;

public class FenwickInsert extends Algorithm {

	private FenwickTree T;
	private int X;
	protected FenwickInsert(FenwickTree t, int x) {
		super(t.panel);
		this.T = t;
		this.X = x;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("insert", X);
		
		if (T.root == null) {
			// TODO remove, never happens
			T.root = FenwickNode.createEmptyLeaf(T, 1);
			addStep("newroot");
		}

		// If the tree is full we have to extend it first
		if (T.root.isFull()) {
			T.extend();
			addStep("intervalextend");
			pause();
		}

		// Insert value into tree, mark the node
		FenwickNode node = T.root.insertOnly(X);
		node.mark();
		addStep("intervalinsert"); // TODO proper message
		pause();
		node.unmark();
		
		// Update values up to root
		node.updateStoredValue(X);
		addStep("intervalinsert");
		pause();		
		
		addNote("done");
	}

}
