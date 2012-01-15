package algvis.aatree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;
import algvis.core.Node;

public class AADelete extends Algorithm {
	AA T;
	BSTNode v;
	int K;

	public AADelete(AA T, int x) {
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
				int lev = ((AANode) d).level;
				BSTNode s = d.right;
				v = T.v = new AANode(T, -Node.INF);
				v.getReady(Colors.FIND);
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
				((AANode) v).level = lev;
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

			// bubleme nahor
			T.reposition();
			mysuspend();
			while (w != null) {
				int ll = (w.left == null) ? 0 : ((AANode) w.left).level, rl = (w.right == null) ? 0
						: ((AANode) w.right).level, wl = ((AANode) w).level;
				addStep("aaok");
				w.mark();
				if (ll < wl - 1 || rl < wl - 1) {
					wl--;
					((AANode) w).level--;
					if (rl > wl) {
						((AANode) w.right).level = wl;
					}
					// skew
					if (w.left != null
							&& ((AANode) w.left).level == ((AANode) w).level) {
						addStep("aaskew");
						mysuspend();
						w.unmark();
						w = w.left;
						w.mark();
						w.setArc();
						mysuspend();
						w.noArc();
						T.rotate(w);
						T.reposition();
					}

					if (w.right != null) {
						T.skew(w.right);
						AANode r = (AANode) w.right;
						if (r.left != null
								&& ((AANode) r.left).level == (r.level)) {
							addStep("aaskew2");
							r.left.setArc(r);
							mysuspend();
							r.left.noArc();
							mysuspend();
							T.rotate(r.left);
							T.reposition();
						}
						if (w.right.right != null) {
							r = (AANode) w.right.right;
							if (r.left != null
									&& ((AANode) r.left).level == (r.level)) {
								addStep("aaskew3");
								r.left.setArc(r);
								mysuspend();
								r.left.noArc();
								T.rotate(r.left);
								T.reposition();
							}
						}
					}

					BSTNode r = w.right;
					if (r != null && r.right != null
							&& ((AANode) r.right).level == ((AANode) w).level) {
						addStep("aasplit");
						r.setArc();
						mysuspend();
						r.noArc();
						w.unmark();
						w = r;
						w.mark();
						T.rotate(w);
						((AANode) w).level++;
						T.reposition();
					}

					// mysuspend();
					if (w != null && w.right != null) {
						r = w.right.right;
						if (r != null
								&& r.right != null
								&& ((AANode) r.right).level == ((AANode) w.right).level) {
							addStep("aasplit2");
							r.setArc();
							mysuspend();
							r.noArc();
							T.rotate(r);
							((AANode) r).level++;
							T.reposition();
						}
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
