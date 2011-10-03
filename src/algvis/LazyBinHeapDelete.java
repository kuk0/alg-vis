package algvis;

public class LazyBinHeapDelete extends Algorithm {
	LazyBinomialHeap H;
	int i;

	public int lg(int n) {
		return (int) Math.ceil(Math.log(n) / Math.log(2));
	}

	public LazyBinHeapDelete(LazyBinomialHeap H, int i) {
		super(H.M);
		this.H = H;
		this.i = i;
		H.cleanup = new BinHeapNode[lg(H.size(i)+1) + 1];
	}

	@Override
	public void run() {
		if (H.root[i] == null) {
			// empty
			H.cleanup = null;
			return;
		}
		mysuspend();
		// delete min
		BinHeapNode v, w, next;
		H.d = H.min[i];
		H.min[i] = null;
		if (H.root[i] == H.d) {
			if (H.d.right == H.d) {
				H.root[i] = null;
			} else {
				H.root[i] = H.d.right;
			}
		}
		H.d.unlink();
		H.d.goDown();

		v = w = H.d.child;
		H.d.child = null;
		if (w != null) {
			// reverse
			do {
				w.parent = null;
				BinHeapNode tl = w.left, tr = w.right;
				w.left = tr;
				w.right = tl;
				w = tr;
			} while (w != v);
			w = w.right;
			// link
			if (H.root[i] == null) {
				H.root[i] = w;
			} else {
				H.root[i].linkAll(w);
			}
		}
		H.reposition();
		mysuspend();

		// cleanup
		if (H.root[i] != null) {
			int h;
			w = H.root[i];
			do {
				w.mark();
				mysuspend();
				h = w.rank;
				v = H.cleanup[h];
				next = w.right;
				if (next == H.root[i]) {
					next = null;
				}
				w.unmark();
				while (v != null) {
					if (H.root[i] == w) {
						H.root[i] = w.right; 
					}
					w.unlink();
					if (v.less(w)) {
						v.linkChild(w);
						w = v;
					} else {
						v.linkRight(w);
						if (H.root[i] == v) {
							H.root[i] = w;
						}
						//mysuspend();
						v.unlink();
						w.linkChild(v);
						v = w;
					}
					//mysuspend();
					H.cleanup[h] = null;
					++h;
					v = H.cleanup[h];
					H.reposition();
					mysuspend();
				}
				H.cleanup[h] = w;
				w = next;
			} while (w != null);
		}
		H.cleanup = null;

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
	}
}
