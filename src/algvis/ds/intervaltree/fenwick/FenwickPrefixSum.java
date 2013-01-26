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
		setHeader("findsum", 1, idxmax);
		
		addNote("fenwicksumnote");
		
		int sum = 0;
		int idx = idxmax;
		int idxmin = idxmax;
		while(idx > 0) {
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
			addNote("fenwicksumnextidx", getPrettyIdx(idx, idxmax));
			
			// Move to next interval (disjunct from current one)
			int newidx = idx - (idx & -idx);
			idx = newidx;			
		}
		
		
		addNote("fenwicksumdone", sum);
	}
	
	private static String[] getPrettyIdx(int idx, int idxmax)
	{
		// Get the original length to make it uniform
		int length = Integer.toBinaryString(idxmax).length();
		String zeros = new String(new char[length]).replace('\0', '0');
		
		// Old idx binary string
		String sidx = (zeros + Integer.toBinaryString(idx));
		sidx = sidx.substring(sidx.length() - length);
		
		// New idx binary string
		int newidx = idx - (idx & -idx);
		String snewidx = (zeros + Integer.toBinaryString(newidx));
		snewidx = snewidx.substring(snewidx.length() - length);
		
		// Find the index of the last "1"
		int i = 0;
		for(int d = idx & -idx; d > 0; i++, d /= 2);		
		
		// Highlight the changed digit
		sidx = sidx.substring(0, length - i) + "<u>1</u>" + sidx.substring(length - i + 1);
		snewidx = snewidx.substring(0, length - i) + "<u>0</u>" + snewidx.substring(length - i + 1);
		
		return new String [] {sidx, idx+"", snewidx, newidx+""};		
	}

}
