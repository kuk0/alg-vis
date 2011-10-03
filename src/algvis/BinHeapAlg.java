package algvis;

public class BinHeapAlg extends Algorithm {
	BinomialHeap H;

	public BinHeapAlg(BinomialHeap H) {
		super(H.M);
		this.H = H;
	}

	public void meld(int i) {
		BinHeapNode v = H.root[i];
		v.mark();
		if ((H.min[0]).less(H.min[i])) {
			H.min[i] = H.min[0];
			// text: nove minimum
		} else {
			// stare min.
		}
		H.min[0] = null;
		mysuspend();
		while (true) {
			if (H.root[0] != null && v.size > H.root[0].size) {
				// pripojime vlavo
				BinHeapNode u = H.root[0];
				if (H.root[0].right == H.root[0]) {
					H.root[0] = null;
				} else {
					H.root[0] = H.root[0].right;
				}
				u.unlink();
				u.highlightTree(u);
				v.linkLeft(u);
				v.unmark();
				v = H.root[i] = u;
				v.mark();
			} else if (H.root[0] != null && v.size <= H.root[0].size
					&& (v.right == H.root[i] || H.root[0].size < v.right.size)) {
				// pripojime vpravo
				BinHeapNode u = H.root[0];
				if (H.root[0].right == H.root[0]) {
					H.root[0] = null;
				} else {
					H.root[0] = H.root[0].right;
				}
				u.unlink();
				u.highlightTree(u);
				v.linkRight(u);
			} else if (v.left.size == v.size && v.left != v
					&& (v.right == H.root[i] || v.size < v.right.size)) {
				// spojime 2 rovnakej velkosti
				BinHeapNode u = v.left;
				if (u.less(v)) { // napojime v pod u
					v.unlink();
					u.linkChild(v);
					v.unmark();
					v = u;
					v.mark();
				} else { // napojime u pod v
					if (H.root[i] == u) {
						H.root[i] = v;
					}
					u.unlink();
					v.linkChild(u);
				}
			} else if (v.right != H.root[i]) {
				// posunieme sa
				v.unmark();
				v = v.right;
				v.mark();
			} else {
				// koncime
				v.unmark();
				break;
			}
			H.reposition();
			mysuspend();
		}
	}
}
