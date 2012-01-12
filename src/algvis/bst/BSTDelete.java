package algvis.bst;

import algvis.core.Algorithm;
import algvis.core.Colors;
import algvis.core.Node;

public class BSTDelete extends Algorithm {
	BST T;
	BSTNode v;
	int K;

	public BSTDelete(BST T, int x) { // Buttons B,
		super(T);
		this.T = T;
		v = T.setNodeV(new BSTNode(T, K = x));
		v.getReady(Colors.DELETE);
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
			finish();
			return;
		} else {
			BSTNode d = T.root;
			v.goTo(d);
			setText("bstdeletestart");
			mysuspend();

			while (true) {
				if (d.key == v.key) { // found
					v.bgColor(Colors.FOUND);
					break;
				} else if (d.key < K) { // right
					if (d.right == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(d.right);
					}
					setText("bstfindright", K, d.key);
					mysuspend();
					v.noArrow();
					d = d.right;
					if (d != null) {
						v.goTo(d);
					} else {
						setText("notfound");
						v.goRight();
						break;
					}
				} else { // left
					if (d.left == null) {
						v.pointInDir(135);
					} else {
						v.pointAbove(d.left);
					}
					setText("bstfindleft", K, d.key);
					mysuspend();
					v.noArrow();
					d = d.left;
					if (d != null) {
						v.goTo(d);
					} else {
						setText("notfound");
						v.goLeft();
						break;
					}
				}
				mysuspend();
			}

			if (d == null) { // notfound
				finish();
				return;
			}

			d.bgColor(Colors.FOUND);
			if (d.isLeaf()) { // case I - list
				setText("bstdeletecase1");
				mysuspend();
				if (d.isRoot()) {
					T.setRoot(null);
				} else if (d.isLeft()) {
					d.parent.unlinkLeft();
				} else {
					d.parent.unlinkRight();
				}
				v.goDown();

			} else if (d.left == null || d.right == null) { // case IIa - 1 syn
				setText("bstdeletecase2");
				mysuspend();
				BSTNode s;
				if (d.left == null) {
					s = d.right;
					d.unlinkRight();
				} else {
					s = d.left;
					d.unlinkLeft();
				}
				if (d.isRoot()) {
					T.setRoot(s);
				} else {
					if (d.isLeft()) {
						d.parent.linkLeft(s);
					} else {
						d.parent.linkRight(s);
					}
				}
				v.goDown();

			} else { // case III - 2 synovia
				setText("bstdeletecase3");
				BSTNode s = d.right;
				v = T.setNodeV(new BSTNode(T, -Node.INF));
				v.getReady(Colors.FIND);
				v.goTo(s);
				mysuspend();
				while (s.left != null) {
					s = s.left;
					v.goTo(s);
					mysuspend();
				}
				v = T.setNodeV(s);
				if (s.isLeft()) {
					s.parent.linkLeft(s.right);
				} else {
					s.parent.linkRight(s.right);
				}
				v.goNextTo(d);
				mysuspend();
				if (d.parent == null) {
					T.setRoot(v);
				} else {
					if (d.isLeft()) {
						d.parent.linkLeft(v);
					} else {
						d.parent.linkRight(v);
					}
				}
				v.linkLeft(d.left);
				v.linkRight(d.right);
				v.goTo(d);
				T.setNodeV(d);
				d.goDown();
			} // end case III

			T.reposition();
			setText("done");
		}
		finish();
	}
}
