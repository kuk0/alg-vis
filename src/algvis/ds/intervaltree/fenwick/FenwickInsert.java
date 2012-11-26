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

		if (T.root.isFull()) {
			T.extend();
			//addStep("fenwickextend");
			pause();
		}

		// TODO mark the new node
		// TODO split into insertion and value propagation
		T.root.insert(X);
		//addNote("fenwickinsert");
		pause();
		
		addNote("done");
	}

}
