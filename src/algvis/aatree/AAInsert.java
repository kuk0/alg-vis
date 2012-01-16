package algvis.aatree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;

public class AAInsert extends Algorithm {
	AA T;
	BSTNode v;
	int K;

	public AAInsert(AA T, int x) {
		super(T);
		this.T = T;
		v = T.setNodeV(new AANode(T, K = x));
		v.bgColor(Colors.INSERT);
		setHeader("insertion");
	}

	@Override
	public void run() {
		BSTNode w = T.root;
		if (T.root == null) {
			T.setRoot(v);
			v.goToRoot();
			addStep("newroot");
			mysuspend();
			v.bgColor(Colors.NORMAL);
			T.setNodeV(null);
		} else {
			v.goAboveRoot();
			addStep("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					addStep("alreadythere");
					v.goDown();
					v.bgColor(Colors.NOTFOUND);
					finish();
					return;
				} else if (w.key < K) {
					addStep("bstinsertright", K, w.key);
					if (w.right != null) {
						w = w.right;
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					addStep("bstinsertleft", K, w.key);
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
				addStep("aaok");
				// skew
				if (w.left != null && w.left.getLevel() == w.getLevel()) {
					addStep("aaskew");
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
					addStep("aasplit");
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
		addStep("done");
		finish();
	}
}
