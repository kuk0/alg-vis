package algvis.skiplist;

import algvis.core.Algorithm;

public class SkipAlg extends Algorithm {
	SkipList L;
	SkipNode v;
	SkipNode p[];
	int K;

	public SkipAlg(SkipList L, int x) {
		super(L);
		this.L = L;
		L.setV(v = new SkipNode(L, x));
		K = x;
		p = new SkipNode[L.height];
	}

	public SkipNode find() {
		SkipNode w = L.getRoot();
		v.goToRoot();
		mysuspend();

		for (int i = L.height - 1;; --i) {
			while (w.getRight().key < K) {
				addStep("skipnext");
				w = w.getRight();
				v.goTo(w);
				mysuspend();
			}
			addStep("skipdown");
			p[i] = w;
			if (w.getDown() == null) {
				break;
			}
			w = w.getDown();
			v.goTo(w);
			mysuspend();
		}
		mysuspend();
		return w;
	}
}
