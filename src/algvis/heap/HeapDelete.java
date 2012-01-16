package algvis.heap;

import algvis.bst.BSTNode;
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
		BSTNode v, w;

		int n = H.n, k = 1 << 10;
		while ((k & n) == 0) {
			k >>= 1;
		}
		k >>= 1;
		w = H.root;
		while (k > 0) {
			w = ((n & k) == 0) ? w.left : w.right;
			k >>= 1;
		}
		H.v = w;
		--H.n;
		if ((n & 1) == 0) {
			w.parent.left = null;
		} else {
			w.parent.right = null;
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
			if (v.left != null) {
				w = v.left;
			}
			if (v.right != null && ((HeapNode) v.right).prec(w)) {
				w = v.right;
			}
			if (w == null || ((HeapNode) v).prec(w)) {
				break;
			}
			H.v = new HeapNode((HeapNode) v);
			H.v.setState(Node.ALIVE);
			H.v2 = new HeapNode((HeapNode) w);
			H.v2.setState(Node.ALIVE);
			v.key = Node.NOKEY;
			w.key = Node.NOKEY;
			H.v.goTo(w);
			H.v2.goTo(v);
			mysuspend();
			v.key = H.v2.key;
			w.key = H.v.key;
			v.bgcolor = H.v2.bgcolor;
			w.bgcolor = H.v.bgcolor;
			H.v = null;
			H.v2 = null;
			v = w;
		}

		addStep("done");
	}
}
