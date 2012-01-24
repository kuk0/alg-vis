package algvis.btree;

import algvis.core.Algorithm;
import algvis.core.NodeColor;

public class BInsert extends Algorithm {
	BTree T;
	BNode v;
	int K;

	public BInsert(BTree T, int x) {
		super(T);
		this.T = T;
		v = T.v = new BNode(T, K = x);
		v.setColor(NodeColor.INSERT);
		setHeader("insertion");
	}

	@Override
	public void run() {
		if (T.root == null) {
			T.root = v;
			v.goAboveRoot();
			addStep("newroot");
			mysuspend();
			v.setColor(NodeColor.NORMAL);
		} else {
			BNode w = T.root;
			v.goAbove(w);
			addStep("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.isIn(K)) {
					addStep("alreadythere");
					v.goDown();
					return;
				}
				if (w.isLeaf()) {
					break;
				}
				int p = w.search(K);
				if (p == 0) {
					addStep("bfind0", K, w.key[0]);
				} else if (p == w.numKeys) {
					addStep("bfindn", w.key[w.numKeys - 1], K, w.numKeys + 1);
				} else {
					addStep("bfind", w.key[p - 1], K, w.key[p], p + 1);
				}
				w = w.c[p];
				v.goAbove(w);
				mysuspend();
			}

			addStep("binsertleaf");
			w.addLeaf(K);
			if (w.numKeys >= T.order) {
				w.setColor(NodeColor.NOTFOUND);
			}
			T.v = null;
			mysuspend();

			while (w.numKeys >= T.order) {
				addStep("bsplit");
				int o = (w.parent != null) ? w.order() : -1;
				w = w.split();
				if (w.parent == null) {
					break;
				}
				w.parent.c[o] = w;
				mysuspend();
				w.goBelow(w.parent);
				mysuspend();
				w.parent.add(o, w);
				w = w.parent;
				if (w.numKeys >= T.order) {
					w.setColor(NodeColor.NOTFOUND);
				}
				T.reposition();
				mysuspend();
			}
			if (w.isRoot()) {
				T.root = w;
			}
			T.reposition();
		}
	}
}
