package algvis.treap;

import algvis.core.Algorithm;

public class TreapInsert extends Algorithm {
	Treap T;
	TreapNode v;
	int K;

	public TreapInsert(Treap T, int x) {
		super(T);
		this.T = T;
		T.setV(v = new TreapNode(T, K = x));
		setHeader("insertion");
	}

	@Override
	public void run() {
		if (T.getRoot() == null) {
			T.setRoot(v);
			v.goToRoot();
			addStep("newroot");
			mysuspend();
		} else {
			TreapNode w = (TreapNode)T.getRoot();
			v.goAboveRoot();
			addStep("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					addStep("alreadythere");
					v.goDown();
					return;
				} else if (w.key < K) {
					addStep("bstinsertright", K, w.key);
					if (w.getRight() != null) {
						w = w.getRight();
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					addStep("bstinsertleft", K, w.key);
					if (w.getLeft() != null) {
						w = w.getLeft();
					} else {
						w.linkLeft(v);
						break;
					}
				}
				v.goAbove(w);
				mysuspend();
			}
			T.reposition();
			mysuspend();
			// bubleme nahor
			addStep("treapbubbleup");
			while (!v.isRoot() && v.getParent().p < v.p) {
				T.rotate(v);
				mysuspend();
			}
		}
		addStep("done");
		T.setV(null);
	}
}
