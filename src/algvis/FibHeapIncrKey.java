package algvis;

public class FibHeapIncrKey extends Algorithm {
	int delta, i;
	BinomialHeap H;
	BinHeapNode v;
	
	public FibHeapIncrKey(BinomialHeap H, BinHeapNode v, int delta, int i) {
		super(H.M);
		this.H = H;
		this.v = v;
		this.delta = delta;
		this.i = i;
		//setHeader("insertion");
	}

	@Override
	public void run() {
		if (H.minHeap) {
			v.key -= delta;
			if (v.key < 1) v.key = 1;
		} else {
			v.key += delta;
			if (v.key > InputField.MAX) v.key = InputField.MAX;
		}
		BinHeapNode w = v.parent;
		//if (w == null) return;
		while (w != null) {
			v.unlink();
			v.unmarkCut();
			H.root[i].linkLeft(v);
			if (v.less(H.min[i])) {
				H.min[i] = v;
			}
			H.reposition();
			mysuspend();
			if (w.cut) {
				v = w;
				w = v.parent;
			} else {
				w.markCut();
				H.reposition();
				break;
			}
		}
	}
}
