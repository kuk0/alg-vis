package algvis.scapegoattree;

import algvis.core.NodeColor;

public class GBInsert extends GBAlg {
	public GBInsert(GBTree T, int x) {
		super(T, x);
		v.setColor(NodeColor.INSERT);
		setHeader("insertion");
	}

	@Override
	public void run() {
		if (T.root == null) {
			T.root = v;
			v.goToRoot();
			addStep("newroot");
			mysuspend();
			v.setColor(NodeColor.NORMAL);
		} else {
			GBNode w = (GBNode) T.root;
			v.goAboveRoot();
			addStep("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					if (w.deleted) {
						addStep("gbinsertunmark");
						w.deleted = false;
						w.setColor(NodeColor.NORMAL);
						--T.del;
						T.v = null;
					} else {
						addStep("alreadythere");
						v.goDown();
						v.setColor(NodeColor.NOTFOUND);
					}
					return;
				} else if (w.key < K) {
					addStep("bstinsertright", K, w.key);
					if (w.getRight() != null) {
						w = w.getRight();
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					addStep("bstinsertleft", K, w.key);
					if (w.getLeft() != null) {
						w = w.getLeft();
					} else {
						w.linkLeft(v);
						break;
					}
				}
				v.goAbove(w);
				mysuspend();
			}
			v.setColor(NodeColor.NORMAL);
			T.reposition();
			mysuspend();

			GBNode b = null;
			while (w != null) {
				w.calc();
				if (w.height > Math.ceil(T.alpha * T.lg(w.size)) && b == null) {
					b = w;
				}
				w = w.getParent();
			}

			// rebuilding
			if (b != null) {
				GBNode r = b;
				int s = 0;
				addStep("gbtoohigh");
				r.mark();
				mysuspend();
				// to vine
				addStep("gbrebuild1");
				while (r != null) {
					if (r.getLeft() == null) {
						r.unmark();
						if (r.deleted) {
							--T.del;
							if (b == r) {
								b = r.getRight();
							}
							T.v = r;
							if (r.getParent() == null) {
								T.root = r = r.getRight();
								if (r != null) {
									r.setParent(null);
								}
							} else {
								r.getParent().linkRight(r = r.getRight());
							}
							T.v.goDown();
						} else {
							r = r.getRight();
							++s;
						}
						if (r != null) {
							r.mark();
						}
					} else {
						if (b == r) {
							b = r.getLeft();
						}
						r.unmark();
						r = r.getLeft();
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
		T.reposition();
		addStep("done");
		T.v = null;
	}
}
