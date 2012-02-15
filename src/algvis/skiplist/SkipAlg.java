package algvis.skiplist;

import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.core.NodeColor;

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
				w.succColor(NodeColor.DARKER, true, w.right.key);
				w = w.right;
				v.goTo(w);
				mysuspend();
			}
			w.right.succColor(NodeColor.DARKER, true, Node.INF+1);
			addStep("skipdown");
			p[i] = w;
			if (w.down == null) {
				break;
			}
			w = w.down;
			v.goTo(w);
			mysuspend();
		}
		mysuspend();
		L.root.succColor(NodeColor.NORMAL, true, Node.INF+1);
		return w;
	}
}
