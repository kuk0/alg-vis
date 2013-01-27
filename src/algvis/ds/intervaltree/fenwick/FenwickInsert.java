package algvis.ds.intervaltree.fenwick;

import algvis.core.Algorithm;

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
		addStep("fenwickinsert");
		pause();
		node.unmark();

		// Explain the next steps
		addNote("fenwickinsertnote");

		// Update values up to root
		while (node != null) {

			// Show the next idx calculation
			addNote("fenwickinsertnextidx",
					FenwickIdx.formatNextInsertIdx(node.idx, T.root.idx));

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
				addStep("fenwickinsertup", node.rangeMin, node.rangeMax);
				pause();
				node.unmark();
			}
		}

		addNote("done");
	}

}
