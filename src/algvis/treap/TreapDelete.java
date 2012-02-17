package algvis.treap;

import algvis.core.Algorithm;
import algvis.core.NodeColor;

public class TreapDelete extends Algorithm {
	Treap T;
	TreapNode v;
	int K;

	public TreapDelete(Treap T, int x) {
		super(T);
		this.T = T;
		T.setV(v = new TreapNode(T, K = x));
		v.setColor(NodeColor.DELETE);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (T.getRoot() == null) {
			v.goToRoot();
			addStep("empty");
			mysuspend();
			v.goDown();
			v.setColor(NodeColor.NOTFOUND);
			addStep("notfound");
			return;
		} else {
			TreapNode d = (TreapNode)T.getRoot();
			v.goTo(d);
			addStep("bstdeletestart");
			mysuspend();

			while (true) {
				if (d.key == K) { // found
					v.setColor(NodeColor.FOUND);
					break;
				} else if (d.key < K) { // right
					addStep("bstfindright", K, d.key);
					d = d.getRight();
					if (d != null) {
						v.goTo(d);
					} else {
						v.goRight();
						break;
					}
				} else { // left
					addStep("bstfindleft", K, d.key);
					d = d.getLeft();
					if (d != null) {
						v.goTo(d);
					} else {
						v.goLeft();
						break;
					}
				}
				mysuspend();
			}

			if (d == null) { // notfound
				addStep("notfound");
				return;
			}

			d.setColor(NodeColor.FOUND);
			T.setV(null);
			addStep("treapbubbledown");
			// prebubleme k listu
			while (!d.isLeaf()) {
				if (d.getLeft() == null) {
					T.rotate(d.getRight());
					// text.setPage("text/treap/delete/left.html");
				} else if (d.getRight() == null) {
					T.rotate(d.getLeft());
					// text.setPage("text/treap/delete/right.html");
				} else if (d.getRight().p > d.getLeft().p) {
					T.rotate(d.getRight());
					// text.setPage("text/treap/delete/left.html");
				} else {
					T.rotate(d.getLeft());
					// text.setPage("text/treap/delete/right.html");
				}
				mysuspend();
			}
			T.setV(d);
			addStep("treapdeletecase1");
			mysuspend();
			if (d.isRoot()) {
				T.setRoot(null);
			} else if (d.isLeft()) {
				d.getParent().setLeft(null);
			} else {
				d.getParent().setRight(null);
			}
			d.goDown();

			T.reposition();
			addStep("done");
		}
	}
}
