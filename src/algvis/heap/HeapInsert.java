package algvis.heap;

import algvis.bst.BSTNode;

public class HeapInsert extends HeapAlg {
	public HeapInsert(Heap H, int x) {
		super(H);
		H.v = v = new HeapNode(H, x);
		setHeader("insertion");
	}

	@Override
	public void run() {
		if (H.n == 1000) {
			addStep("heapfull");
			H.v = null;
			return;
		}
		BSTNode w;

		// link
		++H.n;
		int n = H.n, k = 1 << 10;
		if (n == 1) {
			H.root = w = v;
			v.goToRoot();
			mysuspend();
		} else {
			while ((k & n) == 0) {
				k >>= 1;
			}
			k >>= 1;
			w = H.root;
			while (k > 1) {
				w = ((n & k) == 0) ? w.left : w.right;
				k >>= 1;
			}
			if ((k & n) == 0) {
				w.linkLeft(v);
			} else {
				w.linkRight(v);
			}
			H.reposition();
			mysuspend();
		}
		H.v = null;

		// mysuspend();
		bubbleup(v);
	}
}
