package algvis.redblacktree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.core.NodeColor;

public class RBDelete extends Algorithm {
	RB T;
	BSTNode v;
	int K;

	public RBDelete(RB T, int x) {
		super(T);
		this.T = T;
		v = T.setV(new BSTNode(T, K = x));
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
			RBNode d = (RBNode) T.getRoot();
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
				addNote("notfound");
				return;
			}

			RBNode u = d, w = (u.getLeft() != null) ? u.getLeft() : u
					.getRight2();
			T.NULL.setParent(u.getParent2());
			d.setColor(NodeColor.FOUND);
			if (d.isLeaf()) { // case I - list
				addStep("bstdeletecase1");
				mysuspend();
				if (d.isRoot()) {
					T.setRoot(null);
				} else if (d.isLeft()) {
					d.getParent().setLeft(null);
				} else {
					d.getParent().setRight(null);
				}
				v.goDown();

			} else if (d.getLeft() == null || d.getRight() == null) {
				// case IIa - 1 syn
				addStep("bstdeletecase2");
				mysuspend();
				BSTNode s = (d.getLeft() == null) ? d.getRight() : d.getLeft();
				if (d.isRoot()) {
					T.setRoot(s);
					s.setParent(null);
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
				v = T.setV(new BSTNode(T, -Node.INF));
				v.setColor(NodeColor.FIND);
				v.goTo(s);
				mysuspend();
				while (s.getLeft() != null) {
					s = s.getLeft();
					v.goTo(s);
					mysuspend();
				}
				u = s;
				w = u.getRight2();
				T.NULL.setParent(u.getParent2());
				v = T.setV(s);
				((RBNode) v).setRed(d.isRed());
				if (s.isLeft()) {
					s.getParent().linkLeft(u.getRight());
				} else {
					s.getParent().linkRight(u.getRight());
				}
				v.goNextTo(d);
				mysuspend();
				if (d.getParent() == null) {
					v.setParent(null);
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
				v.calc();
				T.setV(d);
				d.goDown();
			} // end case III

			if (!u.isRed()) {
				// bubleme nahor
				while (w.getParent2() != T.NULL && !w.isRed()) {
					T.NULL.setRed(false);
					if (w.getParent2().getLeft2() == w) {
						RBNode s = w.getParent2().getRight2();
						if (s.isRed()) {
							addStep("rbdelete1");
							mysuspend();
							s.setRed(false);
							w.getParent2().setRed(true);
							T.rotate(s);
						} else if (!s.getLeft2().isRed() && !s.getRight2().isRed()) {
							addStep("rbdelete2");
							mysuspend();
							s.setRed(true);
							w = w.getParent2();
						} else if (!s.getRight2().isRed()) {
							addStep("rbdelete3");
							mysuspend();
							s.getLeft2().setRed(false);
							s.setRed(true);
							T.rotate(s.getLeft());
						} else {
							addStep("rbdelete4");
							mysuspend();
							s.setRed(s.getParent2().isRed());
							w.getParent2().setRed(false);
							s.getRight2().setRed(false);
							T.rotate(s);
							w = (RBNode) T.getRoot();
						}
					} else {
						RBNode s = w.getParent2().getLeft2();
						if (s.isRed()) {
							addStep("rbdelete1");
							mysuspend();
							s.setRed(false);
							w.getParent2().setRed(true);
							T.rotate(s);
						} else if (!s.getRight2().isRed() && !s.getLeft2().isRed()) {
							addStep("rbdelete2");
							mysuspend();
							s.setRed(true);
							w = w.getParent2();
						} else if (!s.getLeft2().isRed()) {
							s.getRight2().setRed(false);
							addStep("rbdelete3");
							mysuspend();
							s.setRed(true);
							T.rotate(s.getRight2());
						} else {
							addStep("rbdelete4");
							mysuspend();
							s.setRed(s.getParent2().isRed());
							w.getParent2().setRed(false);
							s.getLeft2().setRed(false);
							T.rotate(s);
							w = (RBNode) T.getRoot();
						}
					}
					mysuspend();
				}
				w.setRed(false);
			}

			T.reposition();
			addStep("done");
		}
	}
}
