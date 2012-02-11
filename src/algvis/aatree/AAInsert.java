package algvis.aatree;

import algvis.core.Algorithm;
import algvis.core.NodeColor;

public class AAInsert extends Algorithm {
	AA T;
	AANode v;
	int K;

	public AAInsert(AA T, int x) {
		super(T);
		this.T = T;
		T.setV(v = (AANode) T.setV(new AANode(T, K = x)));
		v.setColor(NodeColor.INSERT);
		setHeader("insertion");
	}

	@Override
	public void run() {
		AANode w = (AANode) T.getRoot();
		if (T.getRoot() == null) {
			T.setRoot(v);
			v.goToRoot();
			addStep("newroot");
			mysuspend();
			v.setColor(NodeColor.NORMAL);
			T.setV(null);
		} else {
			v.goAboveRoot();
			addStep("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					addStep("alreadythere");
					v.goDown();
					v.setColor(NodeColor.NOTFOUND);
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

			v.setColor(NodeColor.NORMAL);
			T.setV(null);
			// bubleme nahor
			while (w != null) {
				w.mark();
				addStep("aaok");
				// skew
				if (w.getLeft() != null
						&& w.getLeft().getLevel() == w.getLevel()) {
					addStep("aaskew");
					mysuspend();
					w.unmark();
					w = w.getLeft();
					w.mark();
					w.setArc();
					mysuspend();
					w.noArc();
					T.rotate(w);
					T.reposition();
				}
				// split
				AANode r = w.getRight();
				if (r != null && r.getRight() != null
						&& r.getRight().getLevel() == w.getLevel()) {
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
				w = w.getParent();
			}
		}
		T.reposition();
		addStep("done");
	}
}
