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
		L.v = v = new SkipNode(L, x);
		K = x;
		p = new SkipNode[L.height];
	}

	public SkipNode find() {
		SkipNode w = L.root;
		v.goToRoot();
		mysuspend();

		for (int i = L.height - 1;; --i) {
			while (w.right.key < K) {
				addStep("skipnext");
				w = w.right;
				v.goTo(w);
				mysuspend();
			}
			addStep("skipdown");
			p[i] = w;
			if (w.down == null) {
				break;
			}
			w = w.down;
			v.goTo(w);
			mysuspend();
		}
		return w;
	}
}
