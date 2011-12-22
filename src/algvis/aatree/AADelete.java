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
		super(T);
		this.T = T;
		v = T.setNodeV(new BSTNode(T, K = x, T.up()));
		v.setState(Node.ALIVE);
		v.bgColor(Colors.DELETE);
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
				if (d.key == K) { // found
					v.bgColor(Colors.FOUND);
					break;
				} else if (d.key < K) { // right
					setText("bstfindright", K, d.key);
					d = d.right;
					if (d != null) {
						v.goTo(d);
					} else {
						v.goRight();
						break;
					}
				} else { // left
					setText("bstfindleft", K, d.key);
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
				setText("notfound");
				finish();
				return;
			}

			BSTNode w = d.parent;
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
				BSTNode s = (d.left == null) ? d.right : d.left;
				if (d.isRoot()) {
					T.setRoot(s);
					s.unsetParent();
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
				int lev = d.getLevel();
				BSTNode s = d.right;
				v = T.setNodeV(new AANode(T, -Node.INF, T.up()));
				v.setState(Node.ALIVE);
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
					w = s;
				}
				v = T.setNodeV(s);
				if (s.isLeft()) {
					s.parent.linkLeft(s.right);
				} else {
					s.parent.linkRight(s.right);
				}
				v.goNextTo(d);
				v.setLevel(lev);
				mysuspend();
				if (d.parent == null) {
					v.unsetParent();
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
				v.calc();
				T.setNodeV(d);
				d.goDown();
			} // end case III

			// bubleme nahor
			T.reposition();
			mysuspend();
			while (w != null) {
				int ll = (w.left == null) ? 0 : w.left.getLevel(), rl = (w.right == null) ? 0
						: w.right.getLevel(), wl = w.getLevel();
				setText("aaok");
				w.mark();
				if (ll < wl - 1 || rl < wl - 1) {
					wl--;
					w.setLevel(w.getLevel() - 1);
					if (rl > wl) {
						w.right.setLevel(wl);
					}
					// skew
					if (w.left != null && w.left.getLevel() == w.getLevel()) {
						setText("aaskew");
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
						BSTNode r = w.right;
						if (r.left != null && r.left.getLevel() == r.getLevel()) {
							setText("aaskew2");
							r.left.setArc(r);
							mysuspend();
							r.left.noArc();
							mysuspend();
							T.rotate(r.left);
							T.reposition();
						}
						if (w.right.right != null) {
							r = w.right.right;
							if (r.left != null
									&& r.left.getLevel() == r.getLevel()) {
								setText("aaskew3");
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
							&& r.right.getLevel() == w.getLevel()) {
						setText("aasplit");
						r.setArc();
						mysuspend();
						r.noArc();
						w.unmark();
						w = r;
						w.mark();
						T.rotate(w);
						w.setLevel(w.getLevel() + 1);
						T.reposition();
					}

					// mysuspend();
					if (w != null && w.right != null) {
						r = w.right.right;
						if (r != null && r.right != null
								&& r.right.getLevel() == w.right.getLevel()) {
							setText("aasplit2");
							r.setArc();
							mysuspend();
							r.noArc();
							T.rotate(r);
							r.setLevel(r.getLevel() + 1);
							T.reposition();
						}
					}
					mysuspend();
				}
				w.unmark();
				w = w.parent;
			}

			T.reposition();
			setText("done");
		}
		finish();
	}
}
