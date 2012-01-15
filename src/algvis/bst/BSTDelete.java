package algvis.bst;

import algvis.core.Algorithm;
import algvis.core.Colors;
import algvis.core.Node;

public class BSTDelete extends Algorithm {
	BST T;
	BSTNode v;
	int K;

	public BSTDelete(BST T, int x) { // Buttons B,
		super(T.M);
		this.T = T;
		v = T.v = new BSTNode(T, K = x);
		v.getReady(Colors.DELETE);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (T.root == null) {
			v.goToRoot();
			addStep("empty");
			mysuspend();
			v.goDown();
			v.bgColor(Colors.NOTFOUND);
			addStep("notfound");
			return;
		} else {
			BSTNode d = T.root;
			v.goTo(d);
			addStep("bstdeletestart");
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
					addStep("bstfindright", K, d.key);
					mysuspend();
					v.noArrow();
					d = d.right;
					if (d != null) {
						v.goTo(d);
					} else {
						v.goRight();
						break;
					}
				} else { // left
					if (d.left == null) {
						v.pointInDir(135);
					} else {
						v.pointAbove(d.left);
					}
					addStep("bstfindleft", K, d.key);
					mysuspend();
					v.noArrow();
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
				addStep("notfound");
				return;
			}

			d.bgColor(Colors.FOUND);
			if (d.isLeaf()) { // case I - list
				addStep("bstdeletecase1");
				mysuspend();
				if (d.isRoot()) {
					T.root = null;
				} else if (d.isLeft()) {
					d.parent.left = null;
				} else {
					d.parent.right = null;
				}
				v.goDown();

			} else if (d.left == null || d.right == null) { // case IIa - 1 syn
				addStep("bstdeletecase2");
				mysuspend();
				BSTNode s = (d.left == null) ? d.right : d.left;
				if (d.isRoot()) {
					T.root = s;
					s.parent = null;
				} else {
					s.parent = d.parent;
					if (d.isLeft()) {
						d.parent.left = s;
					} else {
						d.parent.right = s;
					}
				}
				v.goDown();

			} else { // case III - 2 synovia
				addStep("bstdeletecase3");
				BSTNode s = d.right;
				v = T.v = new BSTNode(T, -Node.INF);
				v.getReady(Colors.FIND);
				v.goTo(s);
				mysuspend();
				while (s.left != null) {
					s = s.left;
					v.goTo(s);
					mysuspend();
				}
				v.key = s.key;
				v.bgColor(Colors.NORMAL);
				if (s.right != null) {
					s.right.parent = s.parent;
				}
				if (s.isLeft()) {
					s.parent.left = s.right;
				} else {
					s.parent.right = s.right;
				}
				v.goNextTo(d);
				mysuspend();
				if (d.parent == null) {
					v.parent = null;
					T.root = v;
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
				T.v = d;
				d.goDown();
			} // end case III

			T.reposition();
			addStep("done");
		}
	}
}
