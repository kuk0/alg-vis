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
		if (T.root == null) {
			T.root = FenwickNode.createEmptyLeaf(T, 1);
		}

		if (T.root.isFull()) {
			T.extend();
		}

		T.root.insert(X);
	}

}
