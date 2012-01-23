package algvis.redblacktree;

import algvis.core.Algorithm;

public class RBInsert extends Algorithm {
	RB T;
	RBNode v;
	int K;

	public RBInsert(RB T, int x) {
		super(T);
		this.T = T;
		T.v = v = new RBNode(T, K = x);
		setHeader("insertion");
	}

	@Override
	public void run() {
		RBNode w = (RBNode) T.root;
		if (T.root == T.NULL) {
			v.setLeft(v.setRight(v.setParent(T.NULL)));
			T.root = v;
			v.goToRoot();
			addStep("newroot");
			mysuspend();
		} else {
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
					if (w.getRight() != T.NULL) {
						w = w.getRight();
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					addStep("bstinsertleft", K, w.key);
					if (w.getLeft() != T.NULL) {
						w = w.getLeft();
					} else {
						w.linkLeft(v);
						break;
					}
				}
				v.goAbove(w);
				mysuspend();
			}
			v.setLeft(v.setRight(T.NULL));

			T.reposition();
			mysuspend();

			// bubleme nahor
			w = v;
			RBNode pw = w.getParent();
			while (!w.isRoot() && pw.red) {
				w.mark();
				boolean isleft = pw.isLeft();
				RBNode ppw = pw.getParent(), y = (isleft ? ppw.getRight() : ppw
						.getLeft());
				if (y.red) {
					// case 1
					addStep("rbinsertcase1");
					mysuspend();
					pw.red = false;
					y.red = false;
					ppw.red = true;
					w.unmark();
					w = ppw;
					w.mark();
					pw = w.getParent();
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
						w = w.getParent();
						w.mark();
					}
					pw = w.getParent();
					// case 3
					addStep("rbinsertcase3");
					mysuspend();
					w.red = false;
					pw.red = true;
					T.rotate(w);
					mysuspend();
					w.unmark();
					break;
				}
			}
		}
		w.unmark();
		((RBNode) T.root).red = false;
		T.v = null;
		T.reposition();
		addStep("done");
	}
}
