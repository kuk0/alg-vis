package algvis;

public class LazyBinHeapMeld extends Algorithm {
	LazyBinomialHeap H;
	int i, j;

	public LazyBinHeapMeld(LazyBinomialHeap H, int i, int j) {
		super(H.M);
		this.H = H;
		this.i = i;
		this.j = j;
	}

	@Override
	public void run() {
		if (i == j) {
			return;
		}
		if (H.root[j] != null) {
			H.root[j].highlightTree();
		}
		if (H.root[i] == null) {
			// heap #1 is empty; done;
			H.root[i] = H.root[j];
			H.min[i] = H.min[j];
			H.root[j] = H.min[j] = null;
		} else if (H.root[j] == null) {
			// heap #2 is empty; done;
		} else {
			H.root[i].linkAll(H.root[j]);
			if (H.min[j].less(H.min[i])) {
				H.min[i] = H.min[j];
			}
			H.root[j] = H.min[j] = null;
		}
		H.reposition();
		mysuspend();
	}
}
