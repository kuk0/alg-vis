package algvis.avltree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;
import algvis.core.Node;

public class AVLDelete extends Algorithm {
	AVL T;
	BSTNode v;
	int K;

	public AVLDelete(AVL T, int x) {
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
				if (d.key == K) { // found
					v.bgColor(Colors.FOUND);
					break;
				} else if (d.key < K) { // right
					addStep("bstfindright", K, d.key);
					d = d.right;
					if (d != null) {
						v.goTo(d);
					} else {
						v.goRight();
						break;
					}
				} else { // left
					addStep("bstfindleft", K, d.key);
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

			BSTNode w = d.parent;
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
				v = T.v = new AVLNode(T, -Node.INF);
				v.bgColor(Colors.FIND);
				v.goTo(s);
				mysuspend();
				while (s.left != null) {
					s = s.left;
					v.goTo(s);
					mysuspend();
				}
				w = s.parent;
				if (w == d) {
					w = v;
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
				v.calc();
				T.v = d;
				d.goDown();
			} // end case III

			addStep("avldeletebal");
			mysuspend();
			// bubleme nahor
			while (w != null) {
				w.mark();
				w.calc();
				addStep("avlupdatebal");
				mysuspend();
				if (((AVLNode) w).balance() == -2) {
					if (((AVLNode) w.left).balance() != +1) { // R-rot
						addStep("avlr");
						w.unmark();
						w = w.left;
						w.mark();
						w.setArc(w.parent);
						mysuspend();
						w.noArc();
						T.rotate(w);
					} else { // LR-rot
						addStep("avllr");
						w.unmark();
						w = w.left.right;
						w.mark();
						w.setArc(w.parent);
						w.parent.setArc(w.parent.parent);
						mysuspend();
						w.noArc();
						w.parent.noArc();
						T.rotate(w);
						mysuspend();
						T.rotate(w);
					}
					mysuspend();
				} else if (((AVLNode) w).balance() == +2) {
					if (((AVLNode) w.right).balance() != -1) { // L-rot
						addStep("avll");
						w.unmark();
						w = w.right;
						w.mark();
						w.setArc(w.parent);
						mysuspend();
						w.noArc();
						T.rotate(w);
					} else { // RL-rot
						addStep("avlrl");
						w.unmark();
						w = w.right.left;
						w.mark();
						w.setArc(w.parent);
						w.parent.setArc(w.parent.parent);
						mysuspend();
						w.noArc();
						w.parent.noArc();
						T.rotate(w);
						mysuspend();
						T.rotate(w);
					}
					mysuspend();
				}
				w.unmark();
				w = w.parent;
			}

			T.reposition();
			addStep("done");
		}
	}
}
