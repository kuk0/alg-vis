package algvis.redblacktree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;
import algvis.core.Node;

public class RBDelete extends Algorithm {
	RB T;
	BSTNode v;
	int K;

	public RBDelete(RB T, int x) {
		super(T);
		this.T = T;
		v = T.v = new RBNode(T, K = x);
		v.bgColor(Colors.DELETE);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (T.root == T.NULL) {
			v.goToRoot();
			addStep("empty");
			mysuspend();
			v.goDown();
			v.bgColor(Colors.NOTFOUND);
			addStep("notfound");
			return;
		} else {
			RBNode d = (RBNode) T.root;
			v.goTo(d);
			addStep("bstdeletestart");
			mysuspend();

			while (true) {
				if (d.key == K) { // found
					v.bgColor(Colors.FOUND);
					break;
				} else if (d.key < K) { // right
					addStep("bstfindright", K, d.key);
					d = d.getRight();
					if (d != T.NULL) {
						v.goTo(d);
					} else {
						v.goRight();
						break;
					}
				} else { // left
					addStep("bstfindleft", K, d.key);
					d = d.getLeft();
					if (d != T.NULL) {
						v.goTo(d);
					} else {
						v.goLeft();
						break;
					}
				}
				mysuspend();
			}

			if (d == T.NULL) { // notfound
				addStep("notfound");
				return;
			}

			RBNode u = d, w = (u.getLeft() != T.NULL) ? u.getLeft() : u
					.getRight();
			T.NULL.setParent(u.getParent());
			d.bgColor(Colors.FOUND);
			if (d.isLeaf()) { // case I - list
				addStep("bstdeletecase1");
				mysuspend();
				if (d.isRoot()) {
					T.root = T.NULL;
				} else if (d.isLeft()) {
					d.getParent().setLeft(T.NULL);
				} else {
					d.getParent().setRight(T.NULL);
				}
				v.goDown();

			} else if (d.getLeft() == T.NULL || d.getRight() == T.NULL) { // case
																			// IIa
																			// -
																			// 1
				// syn
				addStep("bstdeletecase2");
				mysuspend();
				BSTNode s = (d.getLeft() == T.NULL) ? d.getRight() : d
						.getLeft();
				if (d.isRoot()) {
					T.root = s;
					s.setParent(T.NULL);
				} else {
					s.setParent(d.getParent());
					if (d.isLeft()) {
						d.getParent().setLeft(s);
					} else {
						d.getParent().setRight(s);
					}
				}
				v.goDown();

			} else { // case III - 2 synovia
				addStep("bstdeletecase3");
				RBNode s = d.getRight();
				v = T.v = new RBNode(T, -Node.INF);
				v.bgColor(Colors.FIND);
				v.goTo(s);
				mysuspend();
				while (s.getLeft() != T.NULL) {
					s = s.getLeft();
					v.goTo(s);
					mysuspend();
				}
				u = s;
				w = u.getRight();
				T.NULL.setParent(u.getParent());
				if (u.getParent() == d) {
					T.NULL.setParent(v);
				}
				v.key = s.key;
				((RBNode) v).red = d.red;
				if (s.getRight() != T.NULL) {
					s.getRight().setParent(s.getParent());
				}
				if (s.isLeft()) {
					s.getParent().setLeft(s.getRight());
				} else {
					s.getParent().setRight(s.getRight());
				}
				v.goNextTo(d);
				mysuspend();
				if (d.getParent() == T.NULL) {
					v.setParent(T.NULL);
					T.root = v;
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
				v.calc();
				T.v = d;
				d.goDown();
			} // end case III

			T.NULL.setLeft(T.NULL.setRight(T.NULL));
			if (!u.red) {
				// bubleme nahor
				while (w.getParent() != T.NULL && !w.red) {
					if (w.isLeft()) {
						RBNode s = w.getParent().getRight();
						if (s.red) {
							addStep("rbdelete1");
							mysuspend();
							(s).red = false;
							w.getParent().red = true;
							T.rotate(s);
						} else if (!s.getLeft().red && !s.getRight().red) {
							addStep("rbdelete2");
							mysuspend();
							s.red = true;
							w = w.getParent();
						} else if (!s.getRight().red) {
							addStep("rbdelete3");
							mysuspend();
							s.getLeft().red = false;
							s.red = true;
							T.rotate(s.getLeft());
						} else {
							addStep("rbdelete4");
							mysuspend();
							s.red = s.getParent().red;
							w.getParent().red = false;
							s.getRight().red = false;
							T.rotate(s);
							w = (RBNode) T.root;
						}
					} else {
						RBNode s = w.getParent().getLeft();
						if (s.red) {
							addStep("rbdelete1");
							mysuspend();
							s.red = false;
							w.getParent().red = true;
							T.rotate(s);
						} else if (!s.getRight().red && !s.getLeft().red) {
							addStep("rbdelete2");
							mysuspend();
							s.red = true;
							w = w.getParent();
						} else if (!s.getLeft().red) {
							s.getRight().red = false;
							addStep("rbdelete3");
							mysuspend();
							s.red = true;
							T.rotate(s.getRight());
						} else {
							addStep("rbdelete4");
							mysuspend();
							s.red = s.getParent().red;
							w.getParent().red = false;
							s.getLeft().red = false;
							T.rotate(s);
							w = (RBNode) T.root;
						}
					}
					mysuspend();
				}
				w.red = false;
			}

			T.reposition();
			addStep("done");
		}
	}
}
