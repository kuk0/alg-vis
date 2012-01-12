package algvis.aatree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;
import algvis.core.Node;

public class AAInsert extends Algorithm {
	AA T;
	BSTNode v;
	int K;

	public AAInsert(AA T, int x) {
		super(T);
		this.T = T;
		v = T.setNodeV(new AANode(T, K = x));
		v.getReady(Colors.INSERT);
		setHeader("insertion");
	}

	@Override
	public void run() {
		BSTNode w = T.root;
		if (T.root == null) {
			T.setRoot(v);
			v.goToRoot();
			setText("newroot");
			mysuspend();
			v.bgColor(Colors.NORMAL);
			T.setNodeV(null);
		} else {
			v.goAboveRoot();
			setText("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					setText("alreadythere");
					v.goDown();
					v.bgColor(Colors.NOTFOUND);
					finish();
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
			T.setNodeV(null);
			// bubleme nahor
			while (w != null) {
				w.mark();
				setText("aaok");
				// skew
				if (w.left != null && w.left.getLevel() == w.getLevel()) {
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
						&& r.right.getLevel() == w.getLevel()) {
					setText("aasplit");
					w.unmark();
					w = r;
					w.mark();
					w.setArc();
					mysuspend();
					w.noArc();
					T.rotate(w);
					w.setLevel(w.getLevel() + 1);
					T.reposition();
				}
				mysuspend();
				w.unmark();
				w = w.parent;
			}
		}
		T.reposition();
		setText("done");
		finish();
	}
}
