package algvis;

public class BinHeapDelete extends BinHeapAlg {
	int i;

	public BinHeapDelete(BinomialHeap H, int i) {
		super(H);
		this.i = i;
	}

	@Override
	public void run() {
		if (H.root[i] == null) {
			// empty - done;
			return;
		}
		BinHeapNode v, w;
		H.d = H.min[i];
		if (H.root[i] == H.d) {
			if (H.d.right == H.d) {
				H.root[i] = null;
			} else {
				H.root[i] = H.d.right;
			}
		}
		H.d.unlink();
		H.d.goDown();

		// find new min
		v = w = H.min[i] = H.root[i];
		if (H.root[i] != null) {
			do {
				if (w.less(H.min[i])) {
					H.min[i] = w;
				}
				w = w.right;
			} while (w != v);
		}

		H.root[0] = v = w = H.d.child;
		H.d.child = null;
		H.reposition();
		if (w == null) {
			mysuspend();
			// no children - done;
			return;
		}
		// reverse & find min
		H.min[0] = w;
		do {
			w.parent = null;
			if (w.less(H.min[0])) {
				H.min[0] = w;
			}
			BinHeapNode tl = w.left, tr = w.right;
			w.left = tr;
			w.right = tl;
			w = tr;
		} while (w != v);
		H.root[0] = w = v = w.right;
		H.reposition();
		mysuspend();

		if (H.root[i] == null) {
			H.root[i] = H.root[0];
			H.min[i] = H.min[0];
			H.root[0] = null;
			H.min[0] = null;
			H.reposition();
			// heap #1 is empty; done;
			return;
		}

		meld(i);
	}
}
