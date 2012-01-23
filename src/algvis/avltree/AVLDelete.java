package algvis.avltree;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.core.Node;

public class AVLDelete extends Algorithm {
	AVL T;
	AVLNode v;
	int K;

	public AVLDelete(AVL T, int x) {
		super(T);
		this.T = T;
		v = (AVLNode) T.setNodeV(new AVLNode(T, K = x));
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
			AVLNode d = (AVLNode) T.root;
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
				finish();
				return;
			}

			AVLNode w = d.getParent();
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
				AVLNode s = (d.getLeft() == null) ? d.getRight() : d.getLeft();
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
				AVLNode s = d.getRight();
				v = (AVLNode) T.setNodeV(new AVLNode(T, -Node.INF));
				v.setColor(NodeColor.FIND);
				v.goTo(s);
				mysuspend();
				while (s.getLeft() != null) {
					s = s.getLeft();
					v.goTo(s);
					mysuspend();
				}
				w = s.getParent();
				if (w == d) {
					w = s;
				}
				v = (AVLNode) T.setNodeV(s);
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
				v.calc();
				T.setNodeV(d);
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
				if (w.balance() == -2) {
					if (w.getLeft().balance() != +1) { // R-rot
						addStep("avlr");
						w.unmark();
						w = w.getLeft();
						w.mark();
						w.setArc(w.getParent());
						mysuspend();
						w.noArc();
						T.rotate(w);
					} else { // LR-rot
						addStep("avllr");
						w.unmark();
						w = w.getLeft().getRight();
						w.mark();
						w.setArc(w.getParent());
						w.getParent().setArc(w.getParent().getParent());
						mysuspend();
						w.noArc();
						w.getParent().noArc();
						T.rotate(w);
						mysuspend();
						T.rotate(w);
					}
					mysuspend();
				} else if (w.balance() == +2) {
					if (w.getRight().balance() != -1) { // L-rot
						addStep("avll");
						w.unmark();
						w = w.getRight();
						w.mark();
						w.setArc(w.getParent());
						mysuspend();
						w.noArc();
						T.rotate(w);
					} else { // RL-rot
						addStep("avlrl");
						w.unmark();
						w = w.getRight().getLeft();
						w.mark();
						w.setArc(w.getParent());
						w.getParent().setArc(w.getParent().getParent());
						mysuspend();
						w.noArc();
						w.getParent().noArc();
						T.rotate(w);
						mysuspend();
						T.rotate(w);
					}
					mysuspend();
				}
				w.unmark();
				w = w.getParent();
			}

			T.reposition();
			addStep("done");
		}
		finish();
	}
}
