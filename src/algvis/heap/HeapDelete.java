package algvis.heap;

import algvis.core.Node;

public class HeapDelete extends HeapAlg {
	public HeapDelete(Heap H) {
		super(H);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (H.n == 0) {
			addStep("heapempty");
			return;
		}
		if (H.n == 1) {
			H.v = H.root;
			H.root = null;
			--H.n;
			H.v.goDown();
			mysuspend();
			return;
		}
		HeapNode v, w;

		int n = H.n, k = 1 << 10;
		while ((k & n) == 0) {
			k >>= 1;
		}
		k >>= 1;
		w = H.root;
		while (k > 0) {
			w = ((n & k) == 0) ? w.getLeft() : w.getRight();
			k >>= 1;
		}
		H.v = w;
		--H.n;
		if ((n & 1) == 0) {
			w.getParent().setLeft(null);
		} else {
			w.getParent().setRight(null);
		}
		H.v.goToRoot();
		H.reposition();
		mysuspend();

		H.root.key = H.v.key;
		H.v = null;
		if (H.minHeap) {
			addStep("minheapbubbledown");
		} else {
			addStep("maxheapbubbledown");
		}
		// mysuspend();

		v = H.root;
		while (true) {
			w = null;
			if (v.getLeft() != null) {
				w = v.getLeft();
			}
			if (v.getRight() != null && v.getRight().prec(w)) {
				w = v.getRight();
			}
			if (w == null || v.prec(w)) {
				break;
			}
			H.v = new HeapNode(v);
			H.v2 = new HeapNode(w);
			v.key = Node.NOKEY;
			w.key = Node.NOKEY;
			H.v.goTo(w);
			H.v2.goTo(v);
			mysuspend();
			v.key = H.v2.key;
			w.key = H.v.key;
			v.setColor(H.v2.getColor());
			w.setColor(H.v.getColor());
			H.v = null;
			H.v2 = null;
			v = w;
		}

		addStep("done");
	}
}
