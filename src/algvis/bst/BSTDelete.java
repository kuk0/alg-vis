package algvis.bst;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.core.Node;

public class BSTDelete extends Algorithm {
	BST T;
	BSTNode v;
	int K;

	public BSTDelete(BST T, int x) { // Buttons B,
		super(T);
		this.T = T;
		v = T.setNodeV(new BSTNode(T, K = x));
		v.setColor(NodeColor.DELETE);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (T.root == null) {
			v.goToRoot();
			addStep("empty");
			mysuspend();
			v.goDown();
			v.setColor(NodeColor.NOTFOUND);
			addStep("notfound");
			finish();
			return;
		} else {
			BSTNode d = T.root;
			v.goTo(d);
			addStep("bstdeletestart");
			mysuspend();

			while (true) {
				if (d.key == v.key) { // found
					v.setColor(NodeColor.FOUND);
					break;
				} else if (d.key < K) { // right
					if (d.getRight() == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(d.getRight());
					}
					addStep("bstfindright", K, d.key);
					mysuspend();
					v.noArrow();
					d = d.getRight();
					if (d != null) {
						v.goTo(d);
					} else {
						addStep("notfound");
						v.goRight();
						break;
					}
				} else { // left
					if (d.getLeft() == null) {
						v.pointInDir(135);
					} else {
						v.pointAbove(d.getLeft());
					}
					addStep("bstfindleft", K, d.key);
					mysuspend();
					v.noArrow();
					d = d.getLeft();
					if (d != null) {
						v.goTo(d);
					} else {
						addStep("notfound");
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

			d.setColor(NodeColor.FOUND);
			if (d.isLeaf()) { // case I - list
				addStep("bstdeletecase1");
				mysuspend();
				if (d.isRoot()) {
					T.setRoot(null);
				} else if (d.isLeft()) {
					d.getParent().unlinkLeft();
				} else {
					d.getParent().unlinkRight();
				}
				v.goDown();

			} else if (d.getLeft() == null || d.getRight() == null) { // case
																		// IIa -
																		// 1 syn
				addStep("bstdeletecase2");
				mysuspend();
				BSTNode s;
				if (d.getLeft() == null) {
					s = d.getRight();
					d.unlinkRight();
				} else {
					s = d.getLeft();
					d.unlinkLeft();
				}
				if (d.isRoot()) {
					T.setRoot(s);
				} else {
					if (d.isLeft()) {
						d.getParent().linkLeft(s);
					} else {
						d.getParent().linkRight(s);
					}
				}
				v.goDown();

			} else { // case III - 2 synovia
				addStep("bstdeletecase3");
				BSTNode s = d.getRight();
				v = T.setNodeV(new BSTNode(T, -Node.INF));
				v.setColor(NodeColor.FIND);
				v.goTo(s);
				mysuspend();
				while (s.getLeft() != null) {
					s = s.getLeft();
					v.goTo(s);
					mysuspend();
				}
				v = T.setNodeV(s);
				if (s.isLeft()) {
					s.getParent().linkLeft(s.getRight());
				} else {
					s.getParent().linkRight(s.getRight());
				}
				v.goNextTo(d);
				mysuspend();
				if (d.getParent() == null) {
					T.setRoot(v);
				} else {
					if (d.isLeft()) {
						d.getParent().linkLeft(v);
					} else {
						d.getParent().linkRight(v);
					}
				}
				v.linkLeft(d.getLeft());
				v.linkRight(d.getRight());
				v.goTo(d);
				T.setNodeV(d);
				d.goDown();
			} // end case III

			T.reposition();
			addStep("done");
		}
		finish();
	}
}
