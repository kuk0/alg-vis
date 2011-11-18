package algvis.aatree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;

public class AAInsert extends Algorithm {
	AA T;
	AANode v;
	int K;

	public AAInsert(AA T, int x) {
		super(T);
		this.T = T;
		T.v = v = new AANode(T, K = x);
		v.bgColor(Colors.INSERT);
		setHeader("insertion");
	}

	@Override
	public void run() {
		BSTNode w = T.root;
		if (T.root == null) {
			T.root = v;
			v.goToRoot();
			setText("newroot");
			mysuspend();
			v.bgColor(Colors.NORMAL);
			T.v = null;
		} else {
			v.goAboveRoot();
			setText("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					setText("alreadythere");
					v.goDown();
					v.bgColor(Colors.NOTFOUND);
					return;
				} else if (w.key < K) {
					setText("bstinsertright", K, w.key);
					if (w.right != null) {
						w = w.right;
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					setText("bstinsertleft", K, w.key);
					if (w.left != null) {
						w = w.left;
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

			v.bgColor(Colors.NORMAL);
			T.v = null;
			// bubleme nahor
			while (w != null) {
				w.mark();
				setText("aaok");
				// skew
				if (w.left != null
						&& ((AANode) w.left).level == ((AANode) w).level) {
					setText("aaskew");
					mysuspend();
					w.unmark();
					w = w.left;
					w.mark();
					w.setArc();
					mysuspend();
					w.noArc();
					T.rotate(w);
					T.reposition();
				}
				// split
				BSTNode r = w.right;
				if (r != null && r.right != null
						&& ((AANode) r.right).level == ((AANode) w).level) {
					setText("aasplit");
					w.unmark();
					w = r;
					w.mark();
					w.setArc();
					mysuspend();
					w.noArc();
					T.rotate(w);
					((AANode) w).level++;
					T.reposition();
				}
				mysuspend();
				w.unmark();
				w = w.parent;
			}
		}
		T.reposition();
		setText("done");
	}
}
