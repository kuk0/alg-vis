package algvis.treap;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;

public class TreapDelete extends Algorithm {
	Treap T;
	BSTNode v;
	int K;

	public TreapDelete(Treap T, int x) {
		super(T.M);
		this.T = T;
		v = T.v = new BSTNode(T, K = x);
		v.bgColor(Colors.DELETE);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (T.root == null) {
			v.goToRoot();
			setText("empty");
			mysuspend();
			v.goDown();
			v.bgColor(Colors.NOTFOUND);
			setText("notfound");
			return;
		} else {
			BSTNode d = T.root;
			v.goTo(d);
			setText("bstdeletestart");
			mysuspend();

			while (true) {
				if (d.key == K) { // found
					v.bgColor(Colors.FOUND);
					break;
				} else if (d.key < K) { // right
					setText("bstfindright", K, d.key);
					d = d.right;
					if (d != null) {
						v.goTo(d);
					} else {
						v.goRight();
						break;
					}
				} else { // left
					setText("bstfindleft", K, d.key);
					d = d.left;
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
				setText("notfound");
				return;
			}

			d.bgColor(Colors.FOUND);
			T.v = null;
			setText("treapbubbledown");
			// prebubleme k listu
			while (!d.isLeaf()) {
				if (d.left == null) {
					T.rotate(d.right);
					// text.setPage("text/treap/delete/left.html");
				} else if (d.right == null) {
					T.rotate(d.left);
					// text.setPage("text/treap/delete/right.html");
				} else if (((TreapNode) d.right).p > ((TreapNode) d.left).p) {
					T.rotate(d.right);
					// text.setPage("text/treap/delete/left.html");
				} else {
					T.rotate(d.left);
					// text.setPage("text/treap/delete/right.html");
				}
				mysuspend();
			}
			T.v = d;
			setText("treapdeletecase1");
			mysuspend();
			if (d.isRoot()) {
				T.root = null;
			} else if (d.isLeft()) {
				d.parent.left = null;
			} else {
				d.parent.right = null;
			}
			d.goDown();

			T.reposition();
			setText("done");
		}
	}
}
