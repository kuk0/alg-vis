package algvis.ds.intervaltree.fenwick;

import algvis.core.Algorithm;

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
		setHeader("findsum", 1, idxmax);
		addNote("fenwicksumnote");

		int sum = 0;
		int idx = idxmax;
		int idxmin = idxmax;
		// If we get to idx 0 the entire interval is summed
		while (idx > 0) {
			// Find node by idx and move up until we find a real one
			FenwickNode node = T.root.findByIdx(idx);
			while (node.isEvenOrFake()) {
				node = node.getParent();
			}

			// Step explanation
			addStep("fenwicksumstep", idx, node.getStoredValue());
			// Show the current sum and it's interval
			sum += node.getStoredValue();
			idxmin = node.rangeMin;
			addNote("fenwicksumstepnote", idxmin, idxmax, sum);
			node.mark();
			pause();
			node.unmark();

			// Show the new idx calculation
			addNote("fenwicksumnextidx",
					FenwickIdx.formatNextSumIdx(idx, idxmax));

			// Move to next interval (disjunct from current one)
			int newidx = idx - (idx & -idx);
			idx = newidx;
		}

		addNote("fenwicksumdone", sum);
	}
}
