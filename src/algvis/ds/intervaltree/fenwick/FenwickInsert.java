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
		while (node != null) {
			
			// Move up to next real node, skipping through fake nodes
			while (node != null) {
				node = node.getParent();
				if (node != null && !node.isEvenOrFake()) {
					break;
				}
			}
			
			// If we found a real node update it's stored value
			if (node != null) {
				node.mark();
				node.updateStoredValueStep(X);
				addStep("intervalinsert");
				pause();
				node.unmark();
			}
		}

		addNote("done");
	}

}
