package algvis.scapegoattree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;

public class GBAlg extends Algorithm {
	GBTree T;
	BSTNode v;
	int K;

	public GBAlg(GBTree T, int x) {
		super(T.M);
		this.T = T;
		T.v = v = new GBNode(T, K = x);
	}

	public BSTNode compr(BSTNode r, int c) {
		BSTNode w = r, x = (c > 0) ? r.right : r;
		w.mark();
		mysuspend();
		for (int i = 0; i < c; ++i) {
			w.unmark();
			w = w.right;
			T.rotate(w);
			w = w.right;
			if (w != null) {
				w.mark();
			}
			mysuspend();
		}
		if (w != null) {
			w.unmark();
		}
		return x;
	}

}
