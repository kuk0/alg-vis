package algvis.redblacktree;

import algvis.core.Algorithm;

public class RBInsert extends Algorithm {
	RB T;
	RBNode v;
	int K;

	public RBInsert(RB T, int x) {
		super(T);
		this.T = T;
		v = (RBNode) T.setV(new RBNode(T, K = x));
		setHeader("insertion");
	}

	@Override
	public void run() {
		RBNode w = (RBNode) T.getRoot();
		if (T.getRoot() == null) {
			// v.setLeft(v.setRight(v.setParent(T.NULL)));
			T.setRoot(v);
			v.goToRoot();
			addStep("newroot");
			mysuspend();
		} else {
			v.goAboveRoot();
			addStep("bst-insert-start");
			mysuspend();

			while (true) {
				if (w.key == K) {
					addStep("alreadythere");
					v.goDown();
					return;
				} else if (w.key < K) {
					addStep("bst-insert-right", K, w.key);
					if (w.getRight() != null) {
						w = w.getRight();
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					addStep("bst-insert-left", K, w.key);
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
			// v.setLeft(v.setRight(T.NULL));

			T.reposition();
			mysuspend();

			// bubleme nahor
			w = v;
			RBNode pw = w.getParent2();
			while (!w.isRoot() && pw.isRed()) {
				w.mark();
				boolean isleft = pw.isLeft();
				RBNode ppw = pw.getParent2(), y = (isleft ? ppw.getRight()
						: ppw.getLeft());
				if (y == null)
					y = T.NULL;
				if (y.isRed()) {
					// case 1
					addStep("rbinsertcase1");
					mysuspend();
					pw.setRed(false);
					y.setRed(false);
					ppw.setRed(true);
					w.unmark();
					w = ppw;
					w.mark();
					pw = w.getParent2();
					mysuspend();
				} else {
					// case 2
					if (isleft != w.isLeft()) {
						addStep("rbinsertcase2");
						mysuspend();
						T.rotate(w);
						mysuspend();
					} else {
						w.unmark();
						w = w.getParent2();
						w.mark();
					}
					pw = w.getParent2();
					// case 3
					addStep("rbinsertcase3");
					mysuspend();
					w.setRed(false);
					pw.setRed(true);
					T.rotate(w);
					mysuspend();
					w.unmark();
					break;
				}
			}
		}
		if (w != null)
			w.unmark();
		((RBNode) T.getRoot()).setRed(false);
		T.setV(null);
		T.reposition();
		addStep("done");
	}
}
