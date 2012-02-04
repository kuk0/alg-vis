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
		v = T.v = new BSTNode(T, K = x);
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
			return;
		} else {
			RBNode d = (RBNode) T.root;
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
					T.root = null;
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
					T.root = s;
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
				v = T.v = new RBNode(T, -Node.INF);
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
				v.key = s.key;
				((RBNode) v).red = d.red;
				w.setParent(s.getParent());
				if (s.isLeft()) {
					s.getParent().setLeft(u.getRight());
				} else {
					s.getParent().setRight(u.getRight());
				}
				v.goNextTo(d);
				mysuspend();
				if (d.getParent() == null) {
					v.setParent(null);
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

			if (!u.red) {
				// bubleme nahor
				while (w.getParent2() != T.NULL && !w.red) {
					T.NULL.red = false;
					if (w.getParent2().getLeft2() == w) {
						RBNode s = w.getParent2().getRight2();
						if (s.red) {
							addStep("rbdelete1");
							mysuspend();
							s.red = false;
							w.getParent2().red = true;
							T.rotate(s);
						} else if (!s.getLeft2().red && !s.getRight2().red) {
							addStep("rbdelete2");
							mysuspend();
							s.red = true;
							w = w.getParent2();
						} else if (!s.getRight2().red) {
							addStep("rbdelete3");
							mysuspend();
							s.getLeft2().red = false;
							s.red = true;
							T.rotate(s.getLeft());
						} else {
							addStep("rbdelete4");
							mysuspend();
							s.red = s.getParent2().red;
							w.getParent2().red = false;
							s.getRight2().red = false;
							T.rotate(s);
							w = (RBNode) T.root;
						}
					} else {
						RBNode s = w.getParent2().getLeft2();
						if (s.red) {
							addStep("rbdelete1");
							mysuspend();
							s.red = false;
							w.getParent2().red = true;
							T.rotate(s);
						} else if (!s.getRight2().red && !s.getLeft2().red) {
							addStep("rbdelete2");
							mysuspend();
							s.red = true;
							w = w.getParent2();
						} else if (!s.getLeft2().red) {
							s.getRight2().red = false;
							addStep("rbdelete3");
							mysuspend();
							s.red = true;
							T.rotate(s.getRight2());
						} else {
							addStep("rbdelete4");
							mysuspend();
							s.red = s.getParent2().red;
							w.getParent2().red = false;
							s.getLeft2().red = false;
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
