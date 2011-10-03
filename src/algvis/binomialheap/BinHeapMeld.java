package algvis.binomialheap;

public class BinHeapMeld extends BinHeapAlg {
	int i, j;

	public BinHeapMeld(BinomialHeap H, int i, int j) {
		super(H);
		this.i = i;
		this.j = j;
	}

	@Override
	public void run() {
		if (i == j) {
			return;
		}
		if (H.root[i] == null) {
			H.root[i] = H.root[j];
			H.min[i] = H.min[j];
			H.root[j] = H.min[j] = null;
			if (H.root[i] != null) {
				H.root[i].highlightTree();
			}
			H.reposition();
			// heap #1 is empty; done;
			return;
		}
		if (H.root[j] == null) {
			// heap #2 is empty; done;
			return;
		}
		H.root[0] = H.root[j];
		H.min[0] = H.min[j];
		H.root[j] = H.min[j] = null;
		H.reposition();

		meld(i);
	}
}
