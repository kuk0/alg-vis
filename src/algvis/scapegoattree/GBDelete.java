package algvis.scapegoattree;

import algvis.bst.BSTNode;
import algvis.core.Colors;

public class GBDelete extends GBAlg {
	public GBDelete(GBTree T, int x) {
		super(T, x);
		v.bgColor(Colors.DELETE);
		setHeader("deletion");
	}

	public BSTNode compr(BSTNode r, int c) {
		BSTNode w = r, x = (c > 0) ? r.right : r;
		w.mark();
		mysuspend();
		for (int i = 0; i < c; ++i) {
			w.unmark();
			w = w.right;
			T.rotate(w);
			w = w.right;
			if (w != null) {
				w.mark();
			}
			mysuspend();
		}
		if (w != null) {
			w.unmark();
		}
		return x;
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
		} else {
			BSTNode w = T.root;
			v.goTo(w);
			addStep("bstfindstart");
			mysuspend();
			while (true) {
				if (w.key == K) {
					if (((GBNode) w).deleted) {
						addStep("gbdeletedeleted");
						v.bgColor(Colors.NOTFOUND);
						v.goDown();
					} else {
						addStep("gbdeletemark");
						((GBNode) w).deleted = true;
						w.bgColor(GBNode.DELETED);
						++T.del;
						T.v = null;
					}
					break;
				} else if (w.key < K) {
					addStep("bstfindright", K, w.key);
					w = w.right;
					if (w != null) {
						v.goTo(w);
					} else { // notfound
						addStep("notfound");
						v.bgColor(Colors.NOTFOUND);
						v.goRight();
						break;
					}
				} else {
					addStep("bstfindleft", K, w.key);
					w = w.left;
					if (w != null) {
						v.goTo(w);
					} else { // notfound
						addStep("notfound");
						v.bgColor(Colors.NOTFOUND);
						v.goLeft();
						break;
					}
				}
				mysuspend();
			}

			// rebuilding
			BSTNode b = T.root;
			if (b.size < 2 * T.del) {
				addStep("gbdeleterebuild");
				BSTNode r = b;
				int s = 0;
				r.mark();
				mysuspend();
				// to vine
				addStep("gbrebuild1");
				while (r != null) {
					if (r.left == null) {
						r.unmark();
						if (((GBNode) r).deleted) {
							--T.del;
							if (b == r) {
								b = r.right;
							}
							T.v = r;
							if (r.parent == null) {
								T.root = r = r.right;
								if (r != null) {
									r.parent = null;
								}
							} else {
								r.parent.linkRight(r = r.right);
							}
							T.v.goDown();
						} else {
							r = r.right;
							++s;
						}
						if (r != null) {
							r.mark();
						}
					} else {
						if (b == r) {
							b = r.left;
						}
						r.unmark();
						r = r.left;
						r.mark();
						T.rotate(r);
					}
					T.reposition();
					mysuspend();
				}

				// to tree
				addStep("gbrebuild2");
				int c = 1;
				for (int i = 0, l = (int) Math.floor(T.lg(s + 1)); i < l; ++i) {
					c *= 2;
				}
				c = s + 1 - c;

				b = compr(b, c);
				s -= c;
				while (s > 1) {
					b = compr(b, s /= 2);
				}
			}
		}
	}
}
