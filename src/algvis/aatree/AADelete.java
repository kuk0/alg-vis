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
		v = T.setNodeV(new BSTNode(T, K = x));
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
			finish();
			return;
		} else {
			AANode d = (AANode) T.root;
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

			AANode w = d.getParent();
			d.bgColor(Colors.FOUND);
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
				AANode s = (d.getLeft() == null) ? d.getRight() : d.getLeft();
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
				int lev = d.getLevel();
				AANode s = d.getRight();
				v = T.setNodeV(new AANode(T, -Node.INF));
				v.bgColor(Colors.FIND);
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
				v = T.setNodeV(s);
				if (s.isLeft()) {
					s.getParent().linkLeft(s.getRight());
				} else {
					s.getParent().linkRight(s.getRight());
				}
				v.goNextTo(d);
				v.setLevel(lev);
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

			// bubleme nahor
			T.reposition();
			mysuspend();
			while (w != null) {
				int ll = (w.getLeft() == null) ? 0 : w.getLeft().getLevel(), rl = (w
						.getRight() == null) ? 0 : w.getRight().getLevel(), wl = w
						.getLevel();
				addStep("aaok");
				w.mark();
				if (ll < wl - 1 || rl < wl - 1) {
					wl--;
					w.setLevel(w.getLevel() - 1);
					if (rl > wl) {
						w.getRight().setLevel(wl);
					}
					// skew
					if (w.getLeft() != null
							&& w.getLeft().getLevel() == w.getLevel()) {
						addStep("aaskew");
						mysuspend();
						w.unmark();
						w = w.getLeft();
						w.mark();
						w.setArc();
						mysuspend();
						w.noArc();
						T.rotate(w);
						T.reposition();
					}

					if (w.getRight() != null) {
						T.skew(w.getRight());
						AANode r = w.getRight();
						if (r.getLeft() != null
								&& r.getLeft().getLevel() == r.getLevel()) {
							addStep("aaskew2");
							r.getLeft().setArc(r);
							mysuspend();
							r.getLeft().noArc();
							mysuspend();
							T.rotate(r.getLeft());
							T.reposition();
						}
						if (w.getRight().getRight() != null) {
							r = w.getRight().getRight();
							if (r.getLeft() != null
									&& r.getLeft().getLevel() == r.getLevel()) {
								addStep("aaskew3");
								r.getLeft().setArc(r);
								mysuspend();
								r.getLeft().noArc();
								T.rotate(r.getLeft());
								T.reposition();
							}
						}
					}

					AANode r = w.getRight();
					if (r != null && r.getRight() != null
							&& r.getRight().getLevel() == w.getLevel()) {
						addStep("aasplit");
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
					if (w != null && w.getRight() != null) {
						r = w.getRight().getRight();
						if (r != null
								&& r.getRight() != null
								&& r.getRight().getLevel() == w.getRight()
										.getLevel()) {
							addStep("aasplit2");
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
				w = w.getParent();
			}

			T.reposition();
			addStep("done");
		}
		finish();
	}
}
