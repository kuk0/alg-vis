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
		super(T.M);
		this.T = T;
		v = T.v = new BSTNode(T, K = x);
		v.bgColor(Colors.DELETE);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (T.root == T.NULL) {
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
					if (d != T.NULL) {
						v.goTo(d);
					} else {
						v.goRight();
						break;
					}
				} else { // left
					setText("bstfindleft", K, d.key);
					d = d.left;
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
				setText("notfound");
				return;
			}

			BSTNode u = d, w = (u.left != T.NULL) ? u.left : u.right;
			T.NULL.parent = u.parent;
			d.bgColor(Colors.FOUND);
			if (d.isLeaf()) { // case I - list
				setText("bstdeletecase1");
				mysuspend();
				if (d.isRoot()) {
					T.root = T.NULL;
				} else if (d.isLeft()) {
					d.parent.left = T.NULL;
				} else {
					d.parent.right = T.NULL;
				}
				v.goDown();

			} else if (d.left == T.NULL || d.right == T.NULL) { // case IIa - 1
				// syn
				setText("bstdeletecase2");
				mysuspend();
				BSTNode s = (d.left == T.NULL) ? d.right : d.left;
				if (d.isRoot()) {
					T.root = s;
					s.parent = T.NULL;
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
				setText("bstdeletecase3");
				BSTNode s = d.right;
				v = T.v = new RBNode(T, -Node.INF);
				v.bgColor(Colors.FIND);
				v.goTo(s);
				mysuspend();
				while (s.left != T.NULL) {
					s = s.left;
					v.goTo(s);
					mysuspend();
				}
				u = s;
				w = u.right;
				T.NULL.parent = u.parent;
				if (u.parent == d) {
					T.NULL.parent = v;
				}
				v.key = s.key;
				((RBNode) v).red = ((RBNode) d).red;
				if (s.right != T.NULL) {
					s.right.parent = s.parent;
				}
				if (s.isLeft()) {
					s.parent.left = s.right;
				} else {
					s.parent.right = s.right;
				}
				v.goNextTo(d);
				mysuspend();
				if (d.parent == T.NULL) {
					v.parent = T.NULL;
					T.root = v;
				} else {
					if (d.isLeft()) {
						d.parent.linkleft(v);
					} else {
						d.parent.linkright(v);
					}
				}
				v.linkleft(d.left);
				v.linkright(d.right);
				v.goTo(d);
				v.calc();
				T.v = d;
				d.goDown();
			} // end case III

			T.NULL.left = T.NULL.right = T.NULL;
			if (!((RBNode) u).red) {
				// bubleme nahor
				while (w.parent != T.NULL && !((RBNode) w).red) {
					if (w.isLeft()) {
						RBNode s = (RBNode) w.parent.right;
						if (s.red) {
							setText("rbdelete1");
							mysuspend();
							(s).red = false;
							((RBNode) w.parent).red = true;
							T.rotate(s);
						} else if (!((RBNode) s.left).red
								&& !((RBNode) s.right).red) {
							setText("rbdelete2");
							mysuspend();
							s.red = true;
							w = w.parent;
						} else if (!((RBNode) s.right).red) {
							setText("rbdelete3");
							mysuspend();
							((RBNode) s.left).red = false;
							s.red = true;
							T.rotate(s.left);
						} else {
							setText("rbdelete4");
							mysuspend();
							s.red = ((RBNode) s.parent).red;
							((RBNode) w.parent).red = false;
							((RBNode) s.right).red = false;
							T.rotate(s);
							w = T.root;
						}
					} else {
						RBNode s = (RBNode) w.parent.left;
						if (s.red) {
							setText("rbdelete1");
							mysuspend();
							(s).red = false;
							((RBNode) w.parent).red = true;
							T.rotate(s);
						} else if (!((RBNode) s.right).red
								&& !((RBNode) s.left).red) {
							setText("rbdelete2");
							mysuspend();
							s.red = true;
							w = w.parent;
						} else if (!((RBNode) s.left).red) {
							((RBNode) s.right).red = false;
							setText("rbdelete3");
							mysuspend();
							s.red = true;
							T.rotate(s.right);
						} else {
							setText("rbdelete4");
							mysuspend();
							s.red = ((RBNode) s.parent).red;
							((RBNode) w.parent).red = false;
							((RBNode) s.left).red = false;
							T.rotate(s);
							w = T.root;
						}
					}
					mysuspend();
				}
				((RBNode) w).red = false;
			}

			T.reposition();
			setText("done");
		}
	}
}
