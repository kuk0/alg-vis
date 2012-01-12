package algvis.redblacktree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;

public class RBInsert extends Algorithm {
	RB T;
	RBNode v;
	int K;

	public RBInsert(RB T, int x) {
		super(T);
		this.T = T;
		T.v = v = new RBNode(T, K = x);
		v.getReady();
		setHeader("insertion");
	}

	@Override
	public void run() {
		BSTNode w = T.root;
		if (T.root == T.NULL) {
			v.left = v.right = v.parent = T.NULL;
			T.root = v;
			v.goToRoot();
			setText("newroot");
			mysuspend();
		} else {
			v.goAboveRoot();
			setText("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					setText("alreadythere");
					v.goDown();
					return;
				} else if (w.key < K) {
					setText("bstinsertright", K, w.key);
					if (w.right != T.NULL) {
						w = w.right;
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					setText("bstinsertleft", K, w.key);
					if (w.left != T.NULL) {
						w = w.left;
					} else {
						w.linkLeft(v);
						break;
					}
				}
				v.goAbove(w);
				mysuspend();
			}
			v.left = v.right = T.NULL;

			T.reposition();
			mysuspend();

			// bubleme nahor
			w = v;
			RBNode pw = (RBNode) w.parent;
			while (!w.isRoot() && pw.red) {
				w.mark();
				boolean isleft = pw.isLeft();
				RBNode ppw = (RBNode) pw.parent, y = (RBNode) (isleft ? ppw.right
						: ppw.left);
				if (y.red) {
					// case 1
					setText("rbinsertcase1");
					mysuspend();
					pw.red = false;
					y.red = false;
					ppw.red = true;
					w.unmark();
					w = ppw;
					w.mark();
					pw = (RBNode) w.parent;
					mysuspend();
				} else {
					// case 2
					if (isleft != w.isLeft()) {
						setText("rbinsertcase2");
						mysuspend();
						T.rotate(w);
						mysuspend();
					} else {
						w.unmark();
						w = w.parent;
						w.mark();
					}
					pw = (RBNode) w.parent;
					// case 3
					setText("rbinsertcase3");
					mysuspend();
					((RBNode) w).red = false;
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
		setText("done");
	}
}
