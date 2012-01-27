package algvis.binomialheap;

public class BinHeapInsert extends BinHeapAlg {
	int i;

	public BinHeapInsert(BinomialHeap H, int i, int x) {
		super(H);
		this.i = i;
		H.root[0] = H.min[0] = new BinHeapNode(H, x);
		if (H.root[i] != null) {
			H.root[0].x = H.root[i].x;
		}
	}

	@Override
	public void run() {
		H.reposition();
		mysuspend();
		// meld
		if (H.root[i] == null) {
			H.root[i] = H.min[i] = H.root[0];
			H.root[0] = null;
			H.reposition();
			mysuspend();
			return;
		}

		meld(i);
	}
}
