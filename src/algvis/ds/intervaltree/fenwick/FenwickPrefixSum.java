package algvis.ds.intervaltree.fenwick;

import algvis.core.Algorithm;
import algvis.ui.VisPanel;

public class FenwickPrefixSum extends Algorithm {

	private FenwickTree T;
	private int idxmax;
	protected FenwickPrefixSum(FenwickTree t, int idxmax) {
		super(t.panel);
		this.T = t;
		this.idxmax = idxmax;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		// TODO custom header?
		setHeader("findsum", 1, idxmax);
		
		// TODO rewrite and translate messages
		
		int sum = 0;
		int idx = idxmax;
		while(idx > 0) {
			// Find node by idx and move up until we find a real one
			FenwickNode node = T.root.findByIdx(idx);
			while (node.isEvenOrFake()) {
				node = node.getParent();
			}
			
			// Add to sum
			sum += node.getStoredValue();
			addStep("fenwicksumstep", idx, node.getStoredValue());
			node.mark();
			pause();
			node.unmark();
			
			// Move to next interval (disjunct from current one)
			int newidx = idx - (idx & -idx);
			addStep("fenwicksumnextidx", newidx, idx, -idx);
			idx = newidx;			
		}
		
		
		addNote("fenwicksumdone", sum);
	}

}
