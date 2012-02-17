package algvis.scapegoattree;

import algvis.core.Algorithm;

public class GBAlg extends Algorithm {
	GBTree T;
	GBNode v;
	int K;

	public GBAlg(GBTree T, int x) {
		super(T);
		this.T = T;
		v = (GBNode) T.setV(new GBNode(T, K = x));
	}

	public GBNode compr(GBNode r, int c) {
		GBNode w = r, x = (c > 0) ? r.getRight() : r;
		w.mark();
		mysuspend();
		for (int i = 0; i < c; ++i) {
			w.unmark();
			w = w.getRight();
			T.rotate(w);
			w = w.getRight();
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
